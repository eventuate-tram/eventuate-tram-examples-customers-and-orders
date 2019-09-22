provider "github" {
  organization = "${var.git_username}"
  token = "${var.git_pat}"
}


resource "aws_ecr_repository" "customer" {
  name = "${var.prefix}-customer-service"
}

resource "aws_ecr_repository" "order" {
  name = "${var.prefix}-order-service"
}

resource "aws_ecr_repository" "orderhistory" {
  name = "${var.prefix}-orderhistory-service"
}

resource "aws_s3_bucket" "source" {
  bucket        = "${var.prefix}-eventuate-source"
  acl           = "private"
  force_destroy = true
}

resource "aws_iam_role" "codebuild_role" {
  name = "${var.prefix}-codebuild-role"
  assume_role_policy = "${file("${path.module}/codebuild_role.json")}"
}

data "template_file" "codebuild_policy" {
  template = "${file("${path.module}/codebuild_policy.json")}"
  vars {
    aws_s3_bucket_arn = "${aws_s3_bucket.source.arn}"
  }
}

resource "aws_iam_role_policy" "codebuild_policy" {
  name = "${var.prefix}-codebuild-policy"
  role = "${aws_iam_role.codebuild_role.id}"
  policy = "${data.template_file.codebuild_policy.rendered}"
}

data "template_file" "buildspec" {
  template = "${file("${path.module}/buildspec.yml")}"

  vars {
    repository_url_customer = "${aws_ecr_repository.customer.repository_url}"
    repository_url_order = "${aws_ecr_repository.order.repository_url}"
    repository_url_orderhistory = "${aws_ecr_repository.orderhistory.repository_url}"
    region = "${var.region}"
    cluster_name = "${aws_ecs_cluster.cluster.name}"
    subnet_id = "${aws_subnet.public-subnet1.id}"
    security_group_ids = "${aws_security_group.sg-ecs.id})}"
  }
}

resource "aws_codebuild_project" "eventuate_build" {
  name = "${var.prefix}-eventuate-codebuild"
  build_timeout = "50"
  service_role = "${aws_iam_role.codebuild_role.arn}"

  artifacts {
    type = "CODEPIPELINE"
  }

  environment {
    compute_type = "BUILD_GENERAL1_MEDIUM"
    image = "aws/codebuild/standard:2.0"
    type = "LINUX_CONTAINER"
    privileged_mode = true

  }

  source {
    type = "CODEPIPELINE"
    buildspec = "${data.template_file.buildspec.rendered}"
  }
}

resource "aws_codepipeline" "pipeline" {
  name     = "${var.prefix}-app-pipeline"
  role_arn = "${aws_iam_role.codepipeline_role.arn}"

  artifact_store {
    location = "${aws_s3_bucket.source.bucket}"
    type     = "S3"
  }

  stage {
    name = "Source"


    action {
      name             = "Source"
      category         = "Source"
      owner            = "ThirdParty"
      provider         = "GitHub"
      version          = "1"
      output_artifacts = ["source"]


      configuration {
        Owner      = "${var.git_username}"
        Repo       = "eventuate-tram-examples-customers-and-orders"
        Branch     = "master"
        OAuthToken = "${var.git_pat}"

      }
    }
  }

  stage {
    name = "Build"

    action {
      name             = "Build"
      category         = "Build"
      owner            = "AWS"
      provider         = "CodeBuild"
      version          = "1"

      input_artifacts  = ["source"]
      output_artifacts = ["imagedefinitions"]

      configuration {
        ProjectName = "${aws_codebuild_project.eventuate_build.name}"
      }
    }
  }

  stage {
    name = "DeployOrderService"

    action {
      name            = "DeployOrder"
      category        = "Deploy"
      owner           = "AWS"
      provider        = "ECS"
      input_artifacts = ["imagedefinitions"]
      version         = "1"

      configuration {
        ClusterName = "${aws_ecs_cluster.cluster.name}"
        ServiceName = "${aws_ecs_service.svc_order.name}"
        FileName    = "imagedefinitions1.json"
      }
    }
  }

  stage {
    name = "DeployCustomerService"

    action {
      name            = "DeployCustomer"
      category        = "Deploy"
      owner           = "AWS"
      provider        = "ECS"
      input_artifacts = ["imagedefinitions"]
      version         = "1"

      configuration {
        ClusterName = "${aws_ecs_cluster.cluster.name}"
        ServiceName = "${aws_ecs_service.svc_customer.name}"
        FileName    = "imagedefinitions2.json"
      }
    }
  }

  stage {
    name = "DeployOrderHistoryService"

    action {
      name            = "DeployOrderHistory"
      category        = "Deploy"
      owner           = "AWS"
      provider        = "ECS"
      input_artifacts = ["imagedefinitions"]
      version         = "1"

      configuration {
        ClusterName = "${aws_ecs_cluster.cluster.name}"
        ServiceName = "${aws_ecs_service.svc_orderhistory.name}"
        FileName    = "imagedefinitions3.json"
      }
    }
  }

}

locals {
  webhook_secret = "super-secret"
}

resource "aws_codepipeline_webhook" "pipeline_webhook" {
  name            = "${var.prefix}-app-webhook-github"
  authentication  = "GITHUB_HMAC"
  target_action   = "Source"
  target_pipeline = "${aws_codepipeline.pipeline.name}"
  authentication_configuration {
    secret_token = "${local.webhook_secret}"
  }

  filter {
    json_path    = "$.ref"
    match_equals = "refs/heads/{Branch}"
  }
}

resource "github_repository_webhook" "git_webhook" {
  repository = "${data.github_repository.repo.name}"

  configuration {
    url          = "${aws_codepipeline_webhook.pipeline_webhook.url}"
    content_type = "form"
    insecure_ssl = true
    secret       = "${local.webhook_secret}"
  }

  events = ["push"]
}

data "github_repository" "repo" {
  name = "eventuate-tram-examples-customers-and-orders"
}

resource "aws_iam_role" "codepipeline_role" {
  name = "${var.prefix}-codepipeline-role"
  assume_role_policy = "${file("${path.module}/codepipeline_role.json")}"
}

data "template_file" "codepipeline_policy" {
  template = "${file("${path.module}/codepipeline.json")}"
  vars {
    aws_s3_bucket_arn = "${aws_s3_bucket.source.arn}"
  }
}

resource "aws_iam_role_policy" "codepipeline_policy" {
  name = "${var.prefix}-codepipeline_policy"
  role = "${aws_iam_role.codepipeline_role.id}"
  policy = "${data.template_file.codepipeline_policy.rendered}"
}

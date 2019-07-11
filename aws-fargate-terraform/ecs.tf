resource "aws_ecs_cluster" "cluster" {
  name = "eventuate-cluster"
}

resource "aws_ecs_service" "svc_cdc" {
  name            = "svc-cdc"
  launch_type     = "FARGATE"
  cluster         = "${aws_ecs_cluster.cluster.id}"
  task_definition = "${aws_ecs_task_definition.task-cdc.arn}"
  desired_count   = 1

  depends_on = [
    "aws_iam_role_policy.ecs_service_role_policy",
  ]

  lifecycle {
    create_before_destroy = true

    ignore_changes = [
      "desired_count",
    ]
  }

  network_configuration {
    security_groups = [
      "${aws_security_group.sg-ecs.id}",
    ]

    subnets = [
      "${aws_subnet.public-subnet.id}",
      "${aws_subnet.public-subnet1.id}",
    ]

    assign_public_ip = true
  }
}

data "template_file" "cdc_task_definition" {
  template = "${file("${path.module}/ecs_cdc.json")}"

  vars {
    db_url       = "jdbc:mysql://${aws_db_instance.mysql_instance.endpoint}/${aws_db_instance.mysql_instance.name}"
    db_pwd       = "${aws_db_instance.mysql_instance.password}"
    db_user      = "${aws_db_instance.mysql_instance.username}"
    zookeeper_connection_string = "${aws_msk_cluster.eventuate.zookeeper_connect_string}"
    eventuate_bootstrap_brokers = "${aws_msk_cluster.eventuate.bootstrap_brokers}"
    logs_region  = "${var.region}"
    logs_group   = "${aws_cloudwatch_log_group.logs_eventuate.name}"
  }
}

resource "aws_ecs_task_definition" "task-cdc" {
  family                = "cdc"
  container_definitions = "${data.template_file.cdc_task_definition.rendered}"

  requires_compatibilities = [
    "FARGATE",
  ]

  memory             = "2048"
  cpu                = "256"
  network_mode       = "awsvpc"
  execution_role_arn = "${aws_iam_role.ecs_execution_role.arn}"
  task_role_arn      = "${aws_iam_role.ecs_execution_role.arn}"
}

resource "aws_iam_role" "ecs_execution_role" {
  name               = "ecs_task_execution_role"
  assume_role_policy = "${file("${path.module}/ecs-task-execution-role.json")}"
}

resource "aws_iam_role_policy" "ecs_execution_role_policy" {
  name   = "ecs_execution_role_policy"
  policy = "${file("${path.module}/ecs-execution-role-policy.json")}"
  role   = "${aws_iam_role.ecs_execution_role.id}"
}

data "aws_iam_policy_document" "ecs_service_role" {
  statement {
    effect = "Allow"

    actions = [
      "sts:AssumeRole",
    ]

    principals {
      type = "Service"

      identifiers = [
        "ecs.amazonaws.com",
      ]
    }
  }
}

resource "aws_iam_role" "ecs_role" {
  name               = "ecs_role"
  assume_role_policy = "${data.aws_iam_policy_document.ecs_service_role.json}"
}

data "aws_iam_policy_document" "ecs_service_policy" {
  statement {
    effect = "Allow"

    resources = [
      "*",
    ]

    actions = [
      "elasticloadbalancing:Describe*",
      "elasticloadbalancing:DeregisterInstancesFromLoadBalancer",
      "elasticloadbalancing:RegisterInstancesWithLoadBalancer",
      "ec2:Describe*",
      "ec2:AuthorizeSecurityGroupIngress",
    ]
  }
}

resource "aws_iam_role_policy" "ecs_service_role_policy" {
  name   = "ecs_service_role_policy"
  policy = "${data.aws_iam_policy_document.ecs_service_policy.json}"
  role   = "${aws_iam_role.ecs_role.id}"
}

resource "aws_cloudwatch_log_group" "logs_eventuate" {
  name = "/ecs/cdc"
}

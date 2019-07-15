resource "aws_ecs_service" "svc_orderhistory" {
  name            = "${var.prefix}-svc-orderhistory"
  launch_type     = "FARGATE"
  cluster         = "${aws_ecs_cluster.cluster.id}"
  task_definition = "${aws_ecs_task_definition.task-orderhistory.arn}"
  desired_count   = 1

  depends_on = [
    "aws_iam_role_policy.ecs_service_role_policy","aws_alb_listener.services_listner", "aws_docdb_cluster_instance.docdb_instance"
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
      "${aws_subnet.public-subnet1.id}",
      "${aws_subnet.public-subnet2.id}",
    ]

    assign_public_ip = true
  }

  load_balancer {
    container_name = "orderhistory"
    container_port = 8080
    target_group_arn = "${aws_alb_target_group.orderhistory_target_group.arn}"
  }
  health_check_grace_period_seconds = 120
}

data "template_file" "orderhistory_task_definition" {
  template = "${file("${path.module}/ecs_orderhistory_definition.json")}"

  vars {
    db_url                      = "jdbc:mysql://${aws_db_instance.mysql_instance.endpoint}/${aws_db_instance.mysql_instance.name}"
    db_pwd                      = "${aws_db_instance.mysql_instance.password}"
    db_user                     = "${aws_db_instance.mysql_instance.username}"
    zookeeper_connection_string = "${join(",", sort(split(",", aws_msk_cluster.eventuate.zookeeper_connect_string)))}"
    eventuate_bootstrap_brokers = "${join(",", sort(split(",", aws_msk_cluster.eventuate.bootstrap_brokers)))}"
    logs_region                 = "${var.region}"
    logs_group                  = "${aws_cloudwatch_log_group.logs_orderhistory_service.name}"
    mongo_uri                   = "mongodb://${aws_docdb_cluster.docdb_cluster.master_username}:${aws_docdb_cluster.docdb_cluster.master_password}@${aws_docdb_cluster.docdb_cluster.endpoint}/customers_and_orders"
  }
}

resource "aws_ecs_task_definition" "task-orderhistory" {
  family                = "orderhistory"
  container_definitions = "${data.template_file.orderhistory_task_definition.rendered}"

  requires_compatibilities = [
    "FARGATE",
  ]

  cpu                = "256"
  memory             = "2048"

  network_mode       = "awsvpc"
  execution_role_arn = "${aws_iam_role.ecs_execution_role.arn}"
  task_role_arn      = "${aws_iam_role.ecs_execution_role.arn}"
}

resource "aws_cloudwatch_log_group" "logs_orderhistory_service" {
  name = "/ecs/orderhistory"
}

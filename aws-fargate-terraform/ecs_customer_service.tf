resource "aws_ecs_service" "svc_customer" {
  name            = "svc-customer"
  launch_type     = "FARGATE"
  cluster         = "${aws_ecs_cluster.cluster.id}"
  task_definition = "${aws_ecs_task_definition.task-customer.arn}"
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
      "${aws_security_group.sg_customer.id}",
    ]

    subnets = [
      "${aws_subnet.public-subnet.id}",
      "${aws_subnet.public-subnet1.id}",
    ]

    assign_public_ip = true
  }
}

data "template_file" "customer_task_definition" {
  template = "${file("${path.module}/ecs_customer_definition.json")}"

  vars {
    db_url                      = "jdbc:mysql://${aws_db_instance.mysql_instance.endpoint}/${aws_db_instance.mysql_instance.name}"
    db_pwd                      = "${aws_db_instance.mysql_instance.password}"
    db_user                     = "${aws_db_instance.mysql_instance.username}"
    zookeeper_connection_string = "${aws_msk_cluster.eventuate.zookeeper_connect_string}"
    eventuate_bootstrap_brokers = "${aws_msk_cluster.eventuate.bootstrap_brokers_tls}"
    logs_region                 = "${var.region}"
    logs_group                  = "${aws_cloudwatch_log_group.logs_customer_service.name}"
  }
}

resource "aws_ecs_task_definition" "task-customer" {
  family                = "customer"
  container_definitions = "${data.template_file.customer_task_definition.rendered}"

  requires_compatibilities = [
    "FARGATE",
  ]

  memory             = "2048"
  cpu                = "1024"
  network_mode       = "awsvpc"
  execution_role_arn = "${aws_iam_role.ecs_execution_role.arn}"
  task_role_arn      = "${aws_iam_role.ecs_execution_role.arn}"
}

resource "aws_cloudwatch_log_group" "logs_customer_service" {
  name = "/ecs/customer"
}

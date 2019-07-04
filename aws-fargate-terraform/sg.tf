resource "aws_security_group" "sg-ecs" {
  name        = "ecs-fargate"
  description = "only 8080 inbound"
  vpc_id      = "${aws_vpc.vpc-eventuate.id}"

  ingress {
    from_port = 9092
    to_port   = 9092
    protocol  = "TCP"

    cidr_blocks = "${var.ecs_ingress_cidr}"
  }

  ingress {
    from_port = 2181
    to_port   = 2181
    protocol  = "TCP"

    cidr_blocks = "${var.ecs_ingress_cidr}"
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"

    cidr_blocks = [
      "0.0.0.0/0",
    ]
  }

  tags {
    Name = "ecs-eventuate"
  }
}

resource "aws_security_group" "sg-rds" {
  name        = "sgrds"
  description = "RDS security group"
  vpc_id      = "${aws_vpc.vpc-eventuate.id}"

  ingress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    self      = true
  }

  ingress {
    from_port = 3306
    to_port   = 3306
    protocol  = "tcp"

    security_groups = [
      "${aws_security_group.sg-ecs.id}",
    ]

    cidr_blocks = "${var.rds_ingress_cidr}"
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"

    cidr_blocks = [
      "0.0.0.0/0"
    ]
  }

  tags {
    Name = "rds-eventuate"
  }
}

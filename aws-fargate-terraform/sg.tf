resource "aws_security_group" "sg-ecs" {
  name        = "ecs-cdc"
  description = "open 8080 to 8099 inbound"
  vpc_id      = "${aws_vpc.vpc-eventuate.id}"

  ingress {
    from_port = 8080
    to_port   = 8099
    protocol  = "TCP"

    cidr_blocks = "${var.ecs_ingress_cidr}"
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
    Name = "ecs-eventuate"
  }
}

resource "aws_security_group" "sg_customer" {
  name        = "ecs-customer"
  description = "open 8080 to 8082 inbound"
  vpc_id      = "${aws_vpc.vpc-eventuate.id}"

  ingress {
    from_port = 8080
    to_port   = 8082
    protocol  = "TCP"

    cidr_blocks = "${var.ecs_ingress_cidr}"
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
    Name = "ecs-customer_service"
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
      "0.0.0.0/0",
    ]
  }

  tags {
    Name = "rds-eventuate"
  }
}

resource "aws_security_group" "kafka" {
  name        = "kafka"
  description = "only 90092, 2181 inbound"
  vpc_id      = "${aws_vpc.vpc-eventuate.id}"

  ingress {
    from_port   = 9094
    to_port     = 9094
    protocol    = "TCP"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 2181
    to_port     = 2181
    protocol    = "TCP"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags {
    Name = "kafka-eventuate"
  }
}


resource "aws_security_group" "sg-ecs" {
  name        = "${var.prefix}-ecs-cdc"
  description = "open 8080 to 8099 inbound"
  vpc_id      = "${aws_vpc.vpc-eventuate.id}"

  ingress {
    from_port = 8080
    to_port   = 8099
    protocol  = "TCP"

    security_groups = [
      "${aws_security_group.sg-alb.id}",
    ]

    cidr_blocks = "${var.ingress_cidr}"
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

resource "aws_security_group" "sg-rds" {
  name        = "${var.prefix}-sgrds"
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

    cidr_blocks = "${var.ingress_cidr}"
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

resource "aws_security_group" "sg_kafka" {
  name        = "${var.prefix}-sgkafka"
  description = "only 9092-9094, 2181 inbound"
  vpc_id      = "${aws_vpc.vpc-eventuate.id}"

  ingress {
    from_port   = 9092
    to_port     = 9094
    protocol    = "TCP"
    cidr_blocks = "${var.ingress_cidr}"
    security_groups = ["${aws_security_group.sg-ecs.id}"]
  }


  ingress {
    from_port   = 2181
    to_port     = 2181
    protocol    = "TCP"
    cidr_blocks = "${var.ingress_cidr}"
    security_groups = ["${aws_security_group.sg-ecs.id}"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags {
    Name = "sg-kafka"
  }
}

resource "aws_security_group" "sg-alb" {
  name = "${var.prefix}-ecs-alb"
  description = "only 80 inbound"
  vpc_id = "${aws_vpc.vpc-eventuate.id}"
  ingress {
    from_port    = 80
    to_port      = 80
    protocol     = "TCP"
    cidr_blocks  = "${var.ingress_cidr}"
  }
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags {
    Name = "ecs-alb"
  }
}

resource "aws_security_group" "sg-docdb" {
  name        = "${var.prefix}-sgdocdb"
  description = "DocDB security group"
  vpc_id      = "${aws_vpc.vpc-eventuate.id}"

  ingress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    self      = true
  }

  ingress {
    from_port = 27017
    to_port   = 27017
    protocol  = "tcp"

    security_groups = [
      "${aws_security_group.sg-ecs.id}",
    ]

    cidr_blocks = "${var.ingress_cidr}"
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

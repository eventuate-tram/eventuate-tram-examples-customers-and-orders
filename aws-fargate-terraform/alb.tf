resource "aws_alb" "lb_cdc" {
  name                       = "lb-cdc"
  subnets                    = ["${aws_subnet.public-subnet1.id}", "${aws_subnet.public-subnet2.id}"]
  security_groups            = ["${aws_security_group.sg-alb.id}"]
  load_balancer_type         = "application"
  idle_timeout               = 60
  internal                   = false
  enable_deletion_protection = false

  tags {
    Name = "CDC"
  }
}

resource "aws_alb_listener" "cdc_listner" {
  load_balancer_arn = "${aws_alb.lb_cdc.arn}"
  port              = 80
  protocol          = "HTTP"
  depends_on        = ["aws_alb_target_group.cdc_target_group", "aws_alb.lb_cdc"]

    default_action {
        target_group_arn = "${aws_alb_target_group.cdc_target_group.arn}"
        type             = "forward"
    }
}

resource "aws_alb_target_group" "cdc_target_group" {
  name        = "cdc-target-group"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = "${aws_vpc.vpc-eventuate.id}"
  target_type = "ip"

  lifecycle {
    create_before_destroy = true
  }

  health_check {
    healthy_threshold = 2
    path = "/actuator/health"
    timeout = 10
    interval = 30
    port = "8080"
  }
}

resource "aws_alb" "lb_customer" {
  name                       = "lb-customer"
  subnets                    = ["${aws_subnet.public-subnet2.id}", "${aws_subnet.public-subnet1.id}"]
  security_groups            = ["${aws_security_group.sg-alb.id}"]
  load_balancer_type         = "application"
  idle_timeout               = 60
  internal                   = false
  enable_deletion_protection = false

  tags {
    Name = "customer_elb"
  }

}

resource "aws_alb_listener" "customer_listner" {
    load_balancer_arn = "${aws_alb.lb_customer.arn}"
    port              = 80
    protocol          = "HTTP"
    depends_on        = ["aws_alb_target_group.customer_target_group"]

    default_action {
        target_group_arn = "${aws_alb_target_group.customer_target_group.arn}"
        type             = "forward"
    }
}

resource "aws_alb_target_group" "customer_target_group" {
  name        = "customer-target-group"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = "${aws_vpc.vpc-eventuate.id}"
  target_type = "ip"

  lifecycle {
    create_before_destroy = true
  }

  health_check {
    healthy_threshold = 2
    path = "/actuator/health"
    timeout = 30
    interval = 60
    port = "8080"
  }
}

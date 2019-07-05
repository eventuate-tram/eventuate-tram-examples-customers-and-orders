provider "aws" {
  region     = "${var.region}"
  access_key = "${var.access_key}"
  secret_key = "${var.secret_key}"
  version    = "~> 1.60"
}

resource "aws_db_instance" "mysql_instance" {
  name                 = "eventuate"
  identifier           = "eventuate-mysql"
  allocated_storage    = 20
  storage_type         = "gp2"
  port                 = 3306
  instance_class       = "db.t2.micro"
  engine               = "MySQL"
  engine_version       = "5.7.22"
  availability_zone    = "${var.az1}"
  username             = "${var.rds_username}"
  password             = "${var.rds_pwd}"
  db_subnet_group_name = "${aws_db_subnet_group.rds-subnet.name}"
  skip_final_snapshot  = true
  publicly_accessible  = true
  parameter_group_name = "${aws_db_parameter_group.mysql_parameter_group.name}"
  vpc_security_group_ids = ["${aws_security_group.sg-rds.id}"]

  provisioner "local-exec" {
    command = <<EOF
        mysqlsh --user=${aws_db_instance.mysql_instance.username} --password=${aws_db_instance.mysql_instance.password} --host ${aws_db_instance.mysql_instance.address} --sql  < 1.initialize-database.sql
        mysqlsh --user=${aws_db_instance.mysql_instance.username} --password=${aws_db_instance.mysql_instance.password} --host ${aws_db_instance.mysql_instance.address} --sql  < 2.initialize-database.sql
        EOF
  }
}

resource "aws_db_parameter_group" "mysql_parameter_group" {
  family = "mysql5.7"
  name   = "mysql-cdc"
}

output "mysql_endpoint" {
  value = "${aws_db_instance.mysql_instance.endpoint}"
}

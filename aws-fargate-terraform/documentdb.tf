resource "aws_docdb_cluster" "docdb_cluster" {
  cluster_identifier = "${var.prefix}-cluster-eventuate"
  availability_zones = ["${var.az1}", "${var.az2}", "${var.az3}"]
  master_username    = "${var.docdb_username}"
  master_password    = "${var.rds_pwd}"
  engine             = "docdb"
  db_subnet_group_name = "${aws_db_subnet_group.rds-subnet.name}"
  vpc_security_group_ids = ["${aws_security_group.sg-docdb.id}"]
  skip_final_snapshot = true
  db_cluster_parameter_group_name = "${aws_docdb_cluster_parameter_group.docdb.name}"
}

resource "aws_docdb_cluster_instance" "docdb_instance" {
  count = 1
  identifier = "${var.prefix}-instance-${count.index}"
  cluster_identifier = "${aws_docdb_cluster.docdb_cluster.cluster_identifier}"
  instance_class = "db.r4.large"
}

resource "aws_docdb_cluster_parameter_group" "docdb" {
  family      = "docdb3.6"
  name        = "${var.prefix}-docdb-pg"
  description = "docdb cluster parameter group"

  parameter {
    name  = "tls"
    value = "disabled"
  }
}

output "mongo_uri" {
  value = "${aws_docdb_cluster.docdb_cluster.endpoint}"
}

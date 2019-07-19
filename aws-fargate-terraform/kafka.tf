resource "aws_msk_cluster" "eventuate" {
  cluster_name           = "eventuate"
  kafka_version          = "2.1.0"
  number_of_broker_nodes = 3

  broker_node_group_info {
    instance_type   = "kafka.m5.large"
    ebs_volume_size = "100"

    client_subnets = [
      "${aws_subnet.public-subnet1.id}",
      "${aws_subnet.public-subnet2.id}",
      "${aws_subnet.public-subnet3.id}"
    ]

    security_groups = ["${aws_security_group.sg_kafka.id}"]
  }

  encryption_info {
    encryption_at_rest_kms_key_arn = "${aws_kms_key.kms.arn}"

    encryption_in_transit = {
      client_broker = "TLS_PLAINTEXT"
    }
  }

  configuration_info {
    arn      = "${aws_msk_configuration.msk.arn}"
    revision = "${aws_msk_configuration.msk.latest_revision}"
  }

  tags = {
    Name = "eventuate"
  }
}

resource "aws_msk_configuration" "msk" {
  kafka_versions = ["2.1.0"]
  name           = "dev-example"

  server_properties = <<PROPERTIES
auto.create.topics.enable = true
delete.topic.enable = true
PROPERTIES
}

resource "aws_kms_key" "kms" {
  description = "example"
}

output "zookeeper_connect_string" {
  value = "${aws_msk_cluster.eventuate.zookeeper_connect_string}"
}

output "bootstrap_brokers_tls" {
  description = "TLS connection host:port pairs"
  value       = "${aws_msk_cluster.eventuate.bootstrap_brokers_tls}"
}

output "bootstrap_brokers" {
  description = "TLS connection host:port pairs"
  value       = "${aws_msk_cluster.eventuate.bootstrap_brokers}"
}


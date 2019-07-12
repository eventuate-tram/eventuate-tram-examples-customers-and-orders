resource "aws_vpc" "vpc-eventuate" {
  cidr_block           = "${var.vpcCIDRblock}"
  instance_tenancy     = "${var.instanceTenancy}"
  enable_dns_support   = "${var.dnsSupport}"
  enable_dns_hostnames = "${var.dnsHostNames}"

  tags {
    Name = "vpc-app-dev"
  }
}

resource "aws_subnet" "public-subnet3" {
  vpc_id                  = "${aws_vpc.vpc-eventuate.id}"
  cidr_block              = "${var.public_subnet_cidr3}"
  map_public_ip_on_launch = true
  availability_zone       = "${var.az1}"

  tags = {
    Name = "Pubilc Subnet"
  }
}

resource "aws_subnet" "public-subnet1" {
  vpc_id                  = "${aws_vpc.vpc-eventuate.id}"
  cidr_block              = "${var.public_subnet_cidr1}"
  map_public_ip_on_launch = true
  availability_zone       = "${var.az2}"

  tags = {
    Name = "Pubilc Subnet"
  }
}

resource "aws_subnet" "public-subnet2" {
  vpc_id                  = "${aws_vpc.vpc-eventuate.id}"
  cidr_block              = "${var.public_subnet_cidr2}"
  map_public_ip_on_launch = true
  availability_zone       = "${var.az3}"

  tags = {
    Name = "Pubilc Subnet"
  }
}

resource "aws_route_table" "public" {
  vpc_id = "${aws_vpc.vpc-eventuate.id}"

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.igw_eventuate.id}"
  }

  tags {
    Name = "Public Subnet"
  }
}

resource "aws_route_table_association" "public3" {
  subnet_id      = "${aws_subnet.public-subnet3.id}"
  route_table_id = "${aws_route_table.public.id}"
}

resource "aws_route_table_association" "public1" {
  subnet_id      = "${aws_subnet.public-subnet1.id}"
  route_table_id = "${aws_route_table.public.id}"
}

resource "aws_route_table_association" "public2" {
  subnet_id      = "${aws_subnet.public-subnet2.id}"
  route_table_id = "${aws_route_table.public.id}"
}

resource "aws_db_subnet_group" "rds-subnet" {
  subnet_ids = ["${aws_subnet.public-subnet1.id}", "${aws_subnet.public-subnet2.id}", "${aws_subnet.public-subnet3.id}"]
}

resource "aws_internet_gateway" "igw_eventuate" {
  vpc_id = "${aws_vpc.vpc-eventuate.id}"

  tags {
    Name = "My VPC Internet Gateway"
  }
}

resource "aws_eip" "nat" {}

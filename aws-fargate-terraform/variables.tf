variable "region" {
  default = "us-east-2"
}

variable "access_key" {
  default = ""
}

variable "secret_key" {
  default = ""
}

variable "instanceTenancy" {
  default = "default"
}

variable "dnsSupport" {
  default = true
}

variable "dnsHostNames" {
  default = true
}

variable "vpcCIDRblock" {
  default = "10.0.0.0/16"
}

variable "public_subnet_cidr3" {
  description = "CIDR for the Public Subnet"
  default     = "10.0.0.0/24"
}

variable "public_subnet_cidr1" {
  description = "CIDR for the Public Subnet"
  default     = "10.0.3.0/24"
}

variable "public_subnet_cidr2" {
  description = "CIDR for the Public Subnet"
  default     = "10.0.2.0/24"
}

variable "destinationCIDRblock" {
  default = "0.0.0.0/0"
}

variable "mapPublicIP" {
  default = true
}

variable "az1" {
  default = "us-east-2a"
}

variable "az2" {
  default = "us-east-2b"
}

variable "az3" {
  default = "us-east-2c"
}

variable "rds_username" {
  default = "admin"
}

variable "rds_pwd" {
  default = "Eventuate123"
}

variable "ingress_cidr" {
  default = [
    "0.0.0.0/0",
  ]

  type = "list"
}

variable "prefix" {
  default = "dev"
}

variable "docdb_username" {
  default = "eventuateadmin"
}

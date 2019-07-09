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

variable "public_subnet_cidr" {
  description = "CIDR for the Public Subnet"
  default     = "10.0.0.0/24"
}

variable "public_subnet_cidr1" {
  description = "CIDR for the Public Subnet"
  default     = "10.0.1.0/24"
}

variable "destinationCIDRblock" {
  default = "0.0.0.0/0"
}

variable "ingressCIDRblock" {
  type = "list"

  default = [
    "0.0.0.0/0",
  ]
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

variable "rds_username" {
  default = "admin"
}

variable "rds_pwd" {
  default = "Eventuate123"
}

variable "ecs_ingress_cidr" {
  default = [
    "0.0.0.0/0"
  ]
  type = "list"
}

variable "rds_ingress_cidr" {
  default = [
    "0.0.0.0/0"
  ]
  type = "list"
}
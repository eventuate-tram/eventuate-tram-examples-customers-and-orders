variable "project" {
  default = "io-eventuate-cox"
}

variable "env" {
  default = "dev"
}

variable "location" {
  default = "eastus"
}


//aks
variable "default_node_size" {
  default = "Standard_A4_v2"
}

variable "node_min_count" {
  default = 1
}

variable "node_max_count" {
  default = 3
}

variable "enable_auto_scaling" {
  default = true
  type    = bool
}

//sql
variable "sql_admin_user" {
  default = "sql_admin"
}


provider "azurerm" {
  version = "2.8.0"
  features {}
}

provider "local" {
  version = "~>1.4"
}

provider "random" {
   version = "~> 2.2"
}

provider "kubernetes" {
  version = "~> 1.11"
  host                   = azurerm_kubernetes_cluster.default.kube_config.0.host
  client_certificate     = base64decode(azurerm_kubernetes_cluster.default.kube_config.0.client_certificate)
  client_key             = base64decode(azurerm_kubernetes_cluster.default.kube_config.0.client_key)
  cluster_ca_certificate = base64decode(azurerm_kubernetes_cluster.default.kube_config.0.cluster_ca_certificate)
}

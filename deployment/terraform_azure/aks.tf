resource "azurerm_resource_group" "default" {
  name     = "${var.project}-rg-${var.env}"
  location = var.location
}
resource "azurerm_kubernetes_cluster" "default" {
  name                = "${var.project}-aks-${var.env}"
  resource_group_name = azurerm_resource_group.default.name
  location            = azurerm_resource_group.default.location
  dns_prefix          = "${var.project}-${var.env}"

  role_based_access_control {
    enabled = true

  }

  default_node_pool {
    name       = "defaultnp"
    node_count = 1
    vm_size    = var.default_node_size
  }

  identity {
    type = "SystemAssigned"
  }
  service_principal {
    client_id     = "msi"
    client_secret = var.client_secret
  }

  addon_profile {
    oms_agent {
      enabled                    = true
      log_analytics_workspace_id = azurerm_log_analytics_workspace.aks_workspace.id
    }
    http_application_routing {
      enabled = false
    }
    aci_connector_linux {
      enabled = false
    }
    kube_dashboard {
      enabled = true
    }
    azure_policy {
      enabled = false
    }
  }

  tags = {
    project     = var.project
    environment = var.env
  }
}

resource "azurerm_log_analytics_workspace" "aks_workspace" {
  name                = "${var.project}-law-${var.env}"
  resource_group_name = azurerm_resource_group.default.name
  location            = azurerm_resource_group.default.location
  sku                 = "PerGB2018"
  retention_in_days   = 60
}

resource "local_file" "aks_config" {
  filename = "config"
  content  = azurerm_kubernetes_cluster.default.kube_config_raw
}

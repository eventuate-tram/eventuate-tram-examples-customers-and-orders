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
    name                = "defaultnp"
    min_count           = var.node_min_count
    max_count           = var.node_max_count
    vm_size             = var.default_node_size
    enable_auto_scaling = var.enable_auto_scaling
  }

  identity {
    type = "SystemAssigned"
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

# data "azurerm_subscription" "default" {
#   subscription_id = "542999bf-c144-445a-8159-79184e978e0b"
# }

# resource "azurerm_role_assignment" "contributor" {
#   scope                            = data.azurerm_subscription.default.id
#   role_definition_name             = "Contributor"
#   principal_id                     = azurerm_kubernetes_cluster.default.kubelet_identity.0.object_id
#   skip_service_principal_aad_check = true
# }

resource "azurerm_log_analytics_workspace" "aks_workspace" {
  name                = "${var.project}-law-${var.env}"
  resource_group_name = azurerm_resource_group.default.name
  location            = azurerm_resource_group.default.location
  sku                 = "PerGB2018"
  retention_in_days   = 60
}

resource "local_file" "aks_config" {
  filename = "aks_kubectl_config"
  content  = azurerm_kubernetes_cluster.default.kube_config_raw
}

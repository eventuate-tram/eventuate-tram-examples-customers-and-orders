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

resource "random_id" "random_suffix" {
  byte_length = 5
}

resource "azurerm_log_analytics_workspace" "aks_workspace" {
  name                = "${var.project}-law-${var.env}-${random_id.random_suffix.hex}"
  resource_group_name = azurerm_resource_group.default.name
  location            = azurerm_resource_group.default.location
  sku                 = "PerGB2018"
  retention_in_days   = 60
}

resource "local_file" "aks_config" {
  filename = "aks_kubectl_config"
  content  = azurerm_kubernetes_cluster.default.kube_config_raw
}

resource "kubernetes_namespace" "eventuate" {
  metadata {
    name = "eventuate-tram-examples-customers-and-orders"
  }
}

resource "kubernetes_config_map" "mongo" {
  metadata {
    name      = "mongodb-config"
    namespace = "eventuate-tram-examples-customers-and-orders"
  }
  data = {
    connection_string = "${join("/", slice(split("/", azurerm_cosmosdb_account.db.connection_strings[0]), 0, 3))}/customers_and_orders?ssl=true"
  }
  depends_on = [local_file.aks_config, kubernetes_namespace.eventuate]
}

resource "kubernetes_config_map" "sql_server" {
  metadata {
    name      = "sql-config"
    namespace = "eventuate-tram-examples-customers-and-orders"
  }
  data = {
    connection_string = "jdbc:sqlserver://${azurerm_sql_server.eventuate_server.fully_qualified_domain_name}:1433;databaseName=eventuate"
    sql_password      = random_password.sql_password.result
    sql_user          = "${var.sql_admin_user}@${azurerm_sql_server.eventuate_server.name}"
    sql_driver        = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
  }
  depends_on = [local_file.aks_config, kubernetes_namespace.eventuate]
}

resource "kubernetes_job" "initialize_db" {
  metadata {
    name = "initialize-db"
  }

  spec {
    template {
      metadata {}
      spec {
        container {
          name    = "mssql-sqlcmd"
          image   = "mcr.microsoft.com/mssql-tools"
          command = ["/bin/bash", "-c"]
          args    = ["curl --fail -s https://raw.githubusercontent.com/eventuate-foundation/eventuate-common/0.10.0.RELEASE/mssql/1.setup.sql > 1.setup.sql; curl --fail -s https://raw.githubusercontent.com/eventuate-foundation/eventuate-common/0.10.0.RELEASE/mssql/2.setup.sql > 2.setup.sql; /opt/mssql-tools/bin/sqlcmd -S ${azurerm_sql_server.eventuate_server.fully_qualified_domain_name} -d ${azurerm_sql_database.eventuate.name} -U ${var.sql_admin_user}@${azurerm_sql_server.eventuate_server.name} -P '${random_password.sql_password.result}' -I -i 1.setup.sql; /opt/mssql-tools/bin/sqlcmd -S ${azurerm_sql_server.eventuate_server.fully_qualified_domain_name} -d ${azurerm_sql_database.eventuate.name} -U ${var.sql_admin_user}@${azurerm_sql_server.eventuate_server.name} -P '${random_password.sql_password.result}' -I -i 2.setup.sql;"]
        }
        restart_policy = "Never"
      }
    }
    backoff_limit = 2
  }
}

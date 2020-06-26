resource "azurerm_sql_server" "eventuate_server" {
  name                         = "${var.project}-ss-${var.env}-${random_id.random_suffix.hex}"
  resource_group_name          = azurerm_resource_group.default.name
  location                     = azurerm_resource_group.default.location
  version                      = "12.0"
  administrator_login          = var.sql_admin_user
  administrator_login_password = random_password.sql_password.result

  tags = {
    environment = var.env
  }
}

resource "random_password" "sql_password" {
  length = 12
  special = true
}

resource "azurerm_sql_database" "eventuate" {
  name                = "eventuate"
  resource_group_name = azurerm_resource_group.default.name
  location            = azurerm_resource_group.default.location
  server_name         = azurerm_sql_server.eventuate_server.name
  requested_service_objective_name = "S0"

  tags = {
    environment = var.env
  }

  provisioner "local-exec" {
    command = <<EOF
        set -e
        curl --fail -s https://raw.githubusercontent.com/eventuate-foundation/eventuate-common/0.10.0.RELEASE/mssql/1.setup.sql > 1.setup.sql
        curl --fail -s https://raw.githubusercontent.com/eventuate-foundation/eventuate-common/0.10.0.RELEASE/mssql/2.setup.sql > 2.setup.sql
        docker run -v $(pwd):/scripts -i mcr.microsoft.com/mssql-tools  bash -c "/opt/mssql-tools/bin/sqlcmd -S ${azurerm_sql_server.eventuate_server.fully_qualified_domain_name} -d ${azurerm_sql_database.eventuate.name} -U ${var.sql_admin_user}@${azurerm_sql_server.eventuate_server.name} -P '${random_password.sql_password.result}' -I -i /scripts/1.setup.sql;"
        docker run -v $(pwd):/scripts -i mcr.microsoft.com/mssql-tools  bash -c "/opt/mssql-tools/bin/sqlcmd -S ${azurerm_sql_server.eventuate_server.fully_qualified_domain_name} -d ${azurerm_sql_database.eventuate.name} -U ${var.sql_admin_user}@${azurerm_sql_server.eventuate_server.name} -P '${random_password.sql_password.result}' -I -i /scripts/2.setup.sql;"
        EOF
  }
}

resource "azurerm_sql_firewall_rule" "public" {
  name                = "public"
  resource_group_name = azurerm_resource_group.default.name
  server_name         = azurerm_sql_server.eventuate_server.name
  start_ip_address    = "0.0.0.0"
  end_ip_address      = "255.255.255.255"
}

output "connection_string" {
    value = "jdbc:sqlserver://${azurerm_sql_server.eventuate_server.fully_qualified_domain_name}:1433;database=${azurerm_sql_database.eventuate.name};user=${var.sql_admin_user}@${azurerm_sql_server.eventuate_server.name};password=${random_password.sql_password.result};encrypt=true;trustServerCertificate=false;"
}

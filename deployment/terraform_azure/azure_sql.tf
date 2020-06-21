resource "azurerm_sql_server" "eventuate_server" {
  name                         = "${var.project}-ss-${var.env}-${random_id.random_suffix.hex}"
  resource_group_name          = azurerm_resource_group.default.name
  location                     = azurerm_resource_group.default.location
  version                      = "12.0"
  administrator_login          = var.sql_admin_user
  administrator_login_password = var.sql_admin_password

  tags = {
    environment = var.env
  }
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
        sqlcmd -S ${azurerm_sql_server.eventuate_server.fully_qualified_domain_name} -d ${azurerm_sql_database.eventuate.name} -U ${var.sql_admin_user}@${azurerm_sql_server.eventuate_server.name} -P ${var.sql_admin_password} -I -i ../../aws-fargate-terraform/1.initialize-database.sql;
        sqlcmd -S ${azurerm_sql_server.eventuate_server.fully_qualified_domain_name} -d ${azurerm_sql_database.eventuate.name} -U ${var.sql_admin_user}@${azurerm_sql_server.eventuate_server.name} -P ${var.sql_admin_password} -I -i ../../aws-fargate-terraform/2.initialize-database.sql;
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
    value = "jdbc:sqlserver://${azurerm_sql_server.eventuate_server.fully_qualified_domain_name}:1433;database=${azurerm_sql_database.eventuate.name};user=${var.sql_admin_user}@${azurerm_sql_server.eventuate_server.name};password=${var.sql_admin_password};encrypt=true;trustServerCertificate=false;"
}

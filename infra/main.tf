terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 4.0"
    }
  }
}

provider "azurerm" {
  features {}
}

# 1. Resource Group 
resource "azurerm_resource_group" "main" {
  name     = var.resource_group_name
  location = var.location
}

# 2. Container Registry 
resource "azurerm_container_registry" "main" {
  name                = "notev2acr"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  sku                 = "Basic"
  admin_enabled       = true
}

# 3. Log Analytics Workspace 
resource "azurerm_log_analytics_workspace" "main" {
  name                = "notev2-logs"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  sku                 = "PerGB2018"
  retention_in_days   = 30
}

# 4. Container Apps Environment 
resource "azurerm_container_app_environment" "main" {
  name                       = "notev2-env"
  resource_group_name        = azurerm_resource_group.main.name
  location                   = azurerm_resource_group.main.location
  log_analytics_workspace_id = azurerm_log_analytics_workspace.main.id
}

# 5. Postgres Flexible Server – deine Datenbank
resource "azurerm_postgresql_flexible_server" "main" {
  name                   = "notev2-db"
  resource_group_name    = azurerm_resource_group.main.name
  location               = azurerm_resource_group.main.location
  version                = "17"
  administrator_login    = var.db_admin_username
  administrator_password = var.db_admin_password
  storage_mb             = 32768
  sku_name               = "B_Standard_B1ms" 
  zone                   = "1"
}

resource "azurerm_postgresql_flexible_server_database" "notesdb" {
  name      = "notesdb"
  server_id = azurerm_postgresql_flexible_server.main.id
}

# Firewall-Regel: erlaubt Azure-internen Services (deine Container App) den Zugriff auf die DB
resource "azurerm_postgresql_flexible_server_firewall_rule" "allow_azure" {
  name             = "AllowAzureServices"
  server_id        = azurerm_postgresql_flexible_server.main.id
  start_ip_address = "0.0.0.0"
  end_ip_address   = "0.0.0.0"
}

# 6. Backend Container App
resource "azurerm_container_app" "backend" {
  name                         = "notev2-backend"
  container_app_environment_id = azurerm_container_app_environment.main.id
  resource_group_name          = azurerm_resource_group.main.name
  revision_mode                = "Single"

  registry {
    server               = azurerm_container_registry.main.login_server
    username             = azurerm_container_registry.main.admin_username
    password_secret_name = "acr-password"
  }

  secret {
    name  = "acr-password"
    value = azurerm_container_registry.main.admin_password
  }

  secret {
    name  = "jwt-secret"
    value = var.jwt_secret
  }

  secret {
    name  = "db-password"
    value = var.db_admin_password
  }

  template {
    container {
      name   = "backend"
      image  = "${azurerm_container_registry.main.login_server}/notev2-backend:latest"
      cpu    = 0.5
      memory = "1Gi"

      env {
        name  = "SPRING_PROFILES_ACTIVE"
        value = "prod"
      }
      env {
        name  = "SPRING_DATASOURCE_URL"
        value = "jdbc:postgresql://${azurerm_postgresql_flexible_server.main.fqdn}:5432/notesdb"
      }
      env {
        name  = "SPRING_DATASOURCE_USERNAME"
        value = var.db_admin_username
      }
      env {
        name        = "SPRING_DATASOURCE_PASSWORD"
        secret_name = "db-password"
      }
      env {
        name        = "JWT_SECRET"
        secret_name = "jwt-secret"
      }
    }
  }

  ingress {
    external_enabled = true
    target_port      = 8080
    traffic_weight {
      percentage      = 100
      latest_revision = true
    }
  }
}

# 7. Frontend Container App
resource "azurerm_container_app" "frontend" {
  name                         = "notev2-frontend"
  container_app_environment_id = azurerm_container_app_environment.main.id
  resource_group_name          = azurerm_resource_group.main.name
  revision_mode                = "Single"

  registry {
    server               = azurerm_container_registry.main.login_server
    username             = azurerm_container_registry.main.admin_username
    password_secret_name = "acr-password"
  }

  secret {
    name  = "acr-password"
    value = azurerm_container_registry.main.admin_password
  }

  template {
    container {
      name   = "frontend"
      image  = "${azurerm_container_registry.main.login_server}/notev2-frontend:latest"
      cpu    = 0.25
      memory = "0.5Gi"
    }
  }

  ingress {
    external_enabled = true
    target_port      = 80
    traffic_weight {
      percentage      = 100
      latest_revision = true
    }
  }
}
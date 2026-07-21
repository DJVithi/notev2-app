output "backend_url" {
  description = "URL des Backends"
  value       = azurerm_container_app.backend.latest_revision_fqdn
}

output "frontend_url" {
  description = "URL des Frontends"
  value       = azurerm_container_app.frontend.latest_revision_fqdn
}

output "acr_login_server" {
  description = "Adresse der Container Registry (zum Docker-Push)"
  value       = azurerm_container_registry.main.login_server
}
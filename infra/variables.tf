variable "resource_group_name" {
  description = "Name der Azure Resource Group"
  type        = string
  default     = "notev2-rg"
}

variable "location" {
  description = "Azure-Region"
  type        = string
  default     = "northeurope"
}

variable "db_admin_username" {
  description = "Admin-Username für Postgres"
  type        = string
  default     = "notev2admin"
}

variable "db_admin_password" {
  description = "Admin-Passwort für Postgres"
  type        = string
  sensitive   = true
}

variable "jwt_secret" {
  description = "Secret zum Signieren der JWTs"
  type        = string
  sensitive   = true
}
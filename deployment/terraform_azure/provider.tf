provider "azurerm" {
  version = "2.5.0"
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
}

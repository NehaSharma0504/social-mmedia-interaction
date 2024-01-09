terraform {
  required_version = ">= 0.12"
   required_providers {
    kubectl = {
      source  = "gavinbunney/kubectl"
      version = ">= 1.7.0"
    }
    helm = {
      source  = "hashicorp/helm"
      version = "= 2.5.1"
    }
  }
}
provider "aws" {
  region                  = "us-east-1"
  profile                 = "default"
}
provider "http" {}

#provider "kubernetes" {
#      config_path = "/var/lib/jenkins/.kube/config"
#    }


#! /bin/bash -e

 cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

 terraform init
 terraform apply -auto-approve

 # KUBECONFIG=./deployment/terraform_azure/aks_kubectl_config skaffold deploy -p aks-ingress

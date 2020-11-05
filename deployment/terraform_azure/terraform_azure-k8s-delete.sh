#! /bin/bash -e

export KUBECONFIG=./deployment/terraform_azure/aks_kubectl_config

kubectl delete namespace eventuate-tram-examples-customers-and-orders

#! /bin/bash -e

export KUBECONFIG=./deployment/terraform_azure/aks_kubectl_config
export CUSTOMER_SERVICE_HOST=$(kubectl get svc -n eventuate-tram-examples-customers-and-orders customer-service -o json | jq -r .status.loadBalancer.ingress[0].ip)
export ORDER_SERVICE_HOST=$(kubectl get svc -n eventuate-tram-examples-customers-and-orders order-service -o json | jq -r .status.loadBalancer.ingress[0].ip)
export ORDER_HISTORY_SERVICE_HOST=$(kubectl get svc -n eventuate-tram-examples-customers-and-orders order-history-service -o json | jq -r .status.loadBalancer.ingress[0].ip)
export USE_PORTS=1

./aws-fargate-terraform/invoke-api.sh

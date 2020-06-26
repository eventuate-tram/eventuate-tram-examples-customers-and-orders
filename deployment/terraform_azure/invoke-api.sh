#! /bin/bash -e

export KUBECONFIG=./deployment/terraform_azure/aks_kubectl_config

export HOST=$(kubectl get svc -n nginx-ingress -o json | jq -r '.items[0].status.loadBalancer.ingress[0].ip')

./aws-fargate-terraform/invoke-api.sh

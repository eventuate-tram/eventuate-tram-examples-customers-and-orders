#! /bin/bash -e

export KUBECONFIG=./deployment/terraform_azure/aks_kubectl_config

KUBECTL_APPLY="kubectl apply --namespace eventuate-tram-examples-customers-and-orders -f"

kubectl apply -f deployment/kubernetes/ingress/namespace.yaml
kubectl apply -f deployment/kubernetes/ingress/nginx-ingress.yaml

${KUBECTL_APPLY} deployment/kubernetes/infrastructure-services/zipkin.yaml
${KUBECTL_APPLY} deployment/kubernetes/infrastructure-services/zookeeper.yaml

./deployment/terraform_azure/wait-for-deployment.sh role=zookeeper

${KUBECTL_APPLY} deployment/kubernetes/infrastructure-services/kafka.yaml

./deployment/terraform_azure/wait-for-deployment.sh role=kafka-service

${KUBECTL_APPLY} deployment/kubernetes/infrastructure-services/cdc-service.yaml
${KUBECTL_APPLY} deployment/kubernetes/application-services

./deployment/terraform_azure/wait-for-deployment.sh svc=cdc-service svc=customer-service svc=order-history-service svc=order-service

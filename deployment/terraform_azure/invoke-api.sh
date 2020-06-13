#! /bin/bash -e

export KUBECONFIG=./deployment/terraform_azure/aks_kubectl_config
export CUSTOMER_SERVICE_HOST=$(kubectl get svc -n eventuate-tram-examples-customers-and-orders customer-service -o json | jq -r .status.loadBalancer.ingress[0].ip)

if [ -z "$CUSTOMER_SERVICE_HOST" ] || [ "$CUSTOMER_SERVICE_HOST" == "null" ] ; then
    echo CUSTOMER_SERVICE_HOST is $CUSTOMER_SERVICE_HOST
fi

export ORDER_SERVICE_HOST=$(kubectl get svc -n eventuate-tram-examples-customers-and-orders order-service -o json | jq -r .status.loadBalancer.ingress[0].ip)

if [ -z "$ORDER_SERVICE_HOST" ] || [ "$ORDER_SERVICE_HOST" == "null" ] || [ "$ORDER_SERVICE_HOST" == '"null"' ] ; then
    echo ORDER_SERVICE_HOST is $ORDER_SERVICE_HOST
fi

echo ORDER_SERVICE_HOST is $ORDER_SERVICE_HOST

export ORDER_HISTORY_SERVICE_HOST=$(kubectl get svc -n eventuate-tram-examples-customers-and-orders order-history-service -o json | jq -r .status.loadBalancer.ingress[0].ip)

if [ -z "$ORDER_HISTORY_SERVICE_HOST" ] || [ "$ORDER_HISTORY_SERVICE_HOST" == "null" ] ; then
    echo ORDER_HISTORY_SERVICE_HOST is $ORDER_HISTORY_SERVICE_HOST
fi

export USE_PORTS=1

./aws-fargate-terraform/invoke-api.sh

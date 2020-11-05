#! /bin/bash -e

for SELECTOR in $* ; do 

echo -n waiting for $SELECTOR...

export KUBECONFIG=./deployment/terraform_azure/aks_kubectl_config

while [[ $(kubectl --namespace eventuate-tram-examples-customers-and-orders get po -l $SELECTOR -o 'jsonpath={..status.conditions[?(@.type=="Ready")].status}') != "True" ]]; do
  echo -n .
  sleep 1
done

echo done

done

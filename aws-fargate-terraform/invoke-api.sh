#! /bin/bash -e

HOST=$(terraform output lb_services_dns)

if [ -z "$HOST" ] ; then
  echo lb_services_dns is empty
  exit 99
fi

echo $HOST

echo Creating customer ...

CREATE_CUSTOMER=$(curl -f -X POST --header "Content-Type: application/json" --header "Accept: */*" -d "{
  \"creditLimit\": {
    \"amount\": 50
  },
  \"name\": \"Chris\"
}" "http://${HOST}/customers")

echo $CREATE_CUSTOMER

CUSTOMER_ID=$(echo $CREATE_CUSTOMER | jq -r .customerId)

echo $CUSTOMER_ID

echo Creating Order ...

CREATE_ORDER_RESPONSE=$(curl -f -X POST --header "Content-Type: application/json" --header "Accept: */*" -d "{
  \"customerId\": $CUSTOMER_ID,
  \"orderTotal\": {
    \"amount\": 23
  }
}" "http://${HOST}/orders")

echo $CREATE_ORDER_RESPONSE

ORDER_ID=$(echo $CREATE_ORDER_RESPONSE | jq -r .orderId)

echo $ORDER_ID

STATE=

echo Querying view for Order Status ...

until [ "$STATE" = "APPROVED" ] ; do
  GET_ORDER_RESPONSE=$(curl -X GET --header "Accept: */*" "http://${HOST}/customers/${CUSTOMER_ID}")
  echo $GET_ORDER_RESPONSE

  STATE=$(echo $GET_ORDER_RESPONSE | jq -r ".orders | .[\"$ORDER_ID\"] | .state" )

  echo $STATE

  sleep 1
done

echo 
echo "    SUCCESS!!!"

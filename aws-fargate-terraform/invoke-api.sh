#! /bin/bash -e

if [ -z "$HOST" ] ; then
  HOST=$(terraform output lb_services_dns)
fi

if [ -z "$HOST" ] ; then
  echo lb_services_dns is empty
  exit 99
fi

if [ ! -z "$USE_PORTS" ] ; then
  CUSTOMER_SERVICE_PORT=:8082
  ORDER_SERVICE_PORT=:8081
  ORDER_HISTORY_SERVICE_PORT=:8083
fi

echo $HOST $CUSTOMER_SERVICE_PORT $ORDER_SERVICE_PORT $ORDER_HISTORY_SERVICE_PORT

echo Creating customer ...

CREATE_CUSTOMER=$(curl -f -X POST --header "Content-Type: application/json" --header "Accept: */*" -d "{
  \"creditLimit\": {
    \"amount\": 50
  },
  \"name\": \"Chris\"
}" "http://${HOST}${CUSTOMER_SERVICE_PORT}/customers")

echo $CREATE_CUSTOMER

CUSTOMER_ID=$(echo $CREATE_CUSTOMER | jq -r .customerId)

echo $CUSTOMER_ID

echo Creating Order ...

CREATE_ORDER_RESPONSE=$(curl -f -X POST --header "Content-Type: application/json" --header "Accept: */*" -d "{
  \"customerId\": $CUSTOMER_ID,
  \"orderTotal\": {
    \"amount\": 23
  }
}" "http://${HOST}${ORDER_SERVICE_PORT}/orders")

echo $CREATE_ORDER_RESPONSE

ORDER_ID=$(echo $CREATE_ORDER_RESPONSE | jq -r .orderId)

echo $ORDER_ID

STATE=

echo Querying view for Order Status ...

until [ "$STATE" = "APPROVED" ] ; do
  GET_ORDER_RESPONSE=$(curl -X GET --header "Accept: */*" "http://${HOST}${ORDER_HISTORY_SERVICE_PORT}/customers/${CUSTOMER_ID}")
  echo $GET_ORDER_RESPONSE

  STATE=$(echo $GET_ORDER_RESPONSE | jq -r ".orders | .[\"$ORDER_ID\"] | .state" )

  echo $STATE

  sleep 1
done

echo
echo "    SUCCESS!!!"

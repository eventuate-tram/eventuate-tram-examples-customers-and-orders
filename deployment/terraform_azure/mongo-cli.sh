#! /bin/bash -e

MONGO_URL=$(cd deployment/terraform_azure/ ; terraform output mongodb | sed -e 's?mongodb://??')

if [ -z "$MONGO_URL" ] ; then
  echo mongodb is empty
  exit 99
fi

echo $MONGO_URL

idandpw=$(echo $MONGO_URL | cut -d@ -f1)
hostandport=$(echo $MONGO_URL | cut -d@ -f2 | sed -e 's/?ssl.*//')

echo $idandpw
echo $hostandport

id=$(echo $idandpw | cut -d: -f1)
pwd=$(echo $idandpw | cut -d: -f2)

docker run -it --rm mongo:3.0.4 mongo -u "$id" -p "$pwd" -ssl -sslAllowInvalidCertificates "$hostandport"

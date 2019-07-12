#! /bin/bash -e

 ARN=$(aws kafka list-configurations | jq -r '.Configurations[0].Arn')

if [ -z "$ARN" ] ; then
  echo No configuration to import
  exit 0
fi

terraform import aws_msk_configuration.msk ${ARN?}

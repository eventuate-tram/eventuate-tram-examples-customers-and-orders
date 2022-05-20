
property_value() {
  property=${1?}
  sed -e "/^$property=/!d" -e "s/$property=//" < gradle.properties
}

export JAR_DIR=target
export SERVICE_IMAGE_VERSION=$(property_value version)
export EVENTUATE_MESSAGING_KAFKA_IMAGE_VERSION=$(property_value eventuateMessagingKafkaImageVersion)
export EVENTUATE_COMMON_VERSION=$(property_value eventuateCommonImageVersion)
export EVENTUATE_CDC_VERSION=$(property_value eventuateCdcImageVersion)
export EVENTUATE_JAVA_BASE_IMAGE_VERSION=$(property_value eventuateExamplesBaseImageVersion)


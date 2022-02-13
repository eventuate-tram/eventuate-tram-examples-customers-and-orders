
export EVENTUATE_MESSAGING_KAFKA_IMAGE_VERSION=$(sed -e '/^eventuateMessagingKafkaImageVersion=/!d' -e 's/eventuateMessagingKafkaImageVersion=//' < gradle.properties)
export EVENTUATE_COMMON_VERSION=$(sed -e '/^eventuateCommonImageVersion=/!d' -e 's/eventuateCommonImageVersion=//' < gradle.properties)
export EVENTUATE_CDC_VERSION=$(sed -e '/^eventuateCdcImageVersion=/!d' -e 's/eventuateCdcImageVersion=//' < gradle.properties)
export EVENTUATE_JAVA_BASE_IMAGE_VERSION=$(sed -e '/^eventuateExamplesBaseImageVersion=/!d' -e 's/eventuateExamplesBaseImageVersion=//' < gradle.properties)


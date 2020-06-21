USE eventuate;

DROP Table IF Exists eventuate.message;
DROP Table IF Exists eventuate.received_messages;

CREATE TABLE eventuate.message (
  id VARCHAR(767) PRIMARY KEY,
  destination VARCHAR(1000) NOT NULL,
  headers VARCHAR(1000) NOT NULL,
  payload VARCHAR(1000) NOT NULL,
  published SMALLINT DEFAULT 0,
  creation_time BIGINT
);

CREATE INDEX message_published_idx ON eventuate.message(published, id);

CREATE TABLE eventuate.received_messages (
  consumer_id VARCHAR(767),
  message_id VARCHAR(767),
  PRIMARY KEY(consumer_id, message_id),
  creation_time BIGINT
);

CREATE TABLE eventuate.offset_store(
  client_name VARCHAR(255) NOT NULL PRIMARY KEY,
  serialized_offset VARCHAR(255)
);

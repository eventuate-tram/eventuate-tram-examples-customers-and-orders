USE eventuate;

CREATE TABLE new_message (
  id VARCHAR(767),
  dbid BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  destination LONGTEXT NOT NULL,
  headers LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  payload LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  published SMALLINT DEFAULT 0,
  creation_time BIGINT
);

INSERT INTO new_message (id, destination, headers, payload, published, creation_time)
    SELECT id, destination, headers, payload, published, creation_time FROM message;

DROP TABLE message;

ALTER TABLE new_message RENAME TO message;

CREATE INDEX message_published_idx ON message(published, dbid);

create table new_events (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  event_id varchar(1000),
  event_type varchar(1000),
  event_data varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  entity_type VARCHAR(1000) NOT NULL,
  entity_id VARCHAR(1000) NOT NULL,
  triggering_event VARCHAR(1000),
  metadata VARCHAR(1000),
  published TINYINT DEFAULT 0
);

INSERT INTO new_events (event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)
    SELECT event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published FROM events;

DROP TABLE events;

ALTER TABLE new_events RENAME TO events;

CREATE INDEX events_idx ON events(entity_type, entity_id, id);

CREATE INDEX events_published_idx ON events(published, id);

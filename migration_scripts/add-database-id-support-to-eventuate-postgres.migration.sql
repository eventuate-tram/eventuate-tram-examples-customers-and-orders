CREATE TABLE eventuate.new_message (
  id VARCHAR(1000),
  dbid bigserial PRIMARY KEY,
  destination TEXT NOT NULL,
  headers TEXT NOT NULL,
  payload TEXT NOT NULL,
  published SMALLINT DEFAULT 0,
  creation_time BIGINT
);

INSERT INTO eventuate.new_message (id, destination, headers, payload, published, creation_time)
    SELECT id, destination, headers, payload, published, creation_time FROM eventuate.message;

DROP TABLE eventuate.message;

ALTER TABLE eventuate.new_message RENAME TO message;

CREATE TABLE eventuate.new_events (
  id bigserial PRIMARY KEY,
  event_id VARCHAR(1000),
  event_type VARCHAR(1000),
  event_data VARCHAR(1000) NOT NULL,
  entity_type VARCHAR(1000) NOT NULL,
  entity_id VARCHAR(1000) NOT NULL,
  triggering_event VARCHAR(1000),
  metadata VARCHAR(1000),
  published SMALLINT DEFAULT 0
);

INSERT INTO eventuate.new_events (event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)
    SELECT event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published FROM eventuate.events;

DROP TABLE eventuate.events;

ALTER TABLE eventuate.new_events RENAME TO events;

CREATE INDEX events_idx ON eventuate.events(entity_type, entity_id, id);
CREATE INDEX events_published_idx ON eventuate.events(published, id);
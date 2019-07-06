Executing this should cause a message to be published to Kafka

use eventuate;
insert into message(id,destination, headers,payload) values('x5', 'mytopic', '{ "ID" : "foo"}', '{}');

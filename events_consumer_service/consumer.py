from kafka import KafkaConsumer
import json
import settings
from db import database

user_events = KafkaConsumer(
    settings.KAFKA_TOPIC_USERS,
    bootstrap_servers=settings.KAFKA_SERVER,
    # to deserialize kafka.producer.object into dict
    value_deserializer=lambda m: json.loads(m.decode('utf-8')),
)

link_events = KafkaConsumer(
    settings.KAFKA_TOPIC_LINKS,
    bootstrap_servers=settings.KAFKA_SERVER,
    # to deserialize kafka.producer.object into dict
    value_deserializer=lambda m: json.loads(m.decode('utf-8')),
)


def eventsManager(data):
    database.save_log(data)


for inf in user_events:
    eventsManager(inf)

for inf in link_events:
    eventsManager(inf)

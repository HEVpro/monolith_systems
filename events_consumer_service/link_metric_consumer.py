from kafka import KafkaConsumer
import json
import settings
from db import database

consumer = KafkaConsumer(
    settings.KAFKA_TOPIC_LINKS_METRIC,
    bootstrap_servers=settings.KAFKA_SERVER,
    group_id='link_metric',
    # to deserialize kafka.producer.object into dict
    value_deserializer=lambda m: json.loads(m.decode('utf-8')),
)


def eventsManager(data):
    database.save_log(data)


for inf in consumer:
    eventsManager(inf)

from kafka import KafkaConsumer
import json

import settings
from db import database

consumer = KafkaConsumer(
    *settings.KAFKA_TOPICS.split(","),
    bootstrap_servers=settings.KAFKA_SERVER,
    group_id='reporting-consumer',
    # to deserialize kafka.producer.object into dict
    value_deserializer=lambda m: json.loads(m.decode('utf-8')),
)

# TODO: MANAGE ERROR
for inf in consumer:
    database.save_log(inf)

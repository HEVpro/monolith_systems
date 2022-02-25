from kafka import KafkaConsumer
import json

import settings
from db import database

consumer = KafkaConsumer(
    settings.KAFKA_TOPIC_USERS,
    bootstrap_servers=settings.KAFKA_SERVER,
    group_id='user',
    # to deserialize kafka.producer.object into dict
    value_deserializer=lambda m: json.loads(m.decode('utf-8')),
)


for inf in consumer:
    database.save_log(inf)

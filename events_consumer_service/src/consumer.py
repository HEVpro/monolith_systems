from kafka import KafkaConsumer
import json

import settings
from db import database
from exceptions import ConsumerException

consumer = KafkaConsumer(
    *settings.KAFKA_TOPICS.split(","),
    bootstrap_servers=f'{settings.KAFKA_SERVER}:{settings.KAFKA_PORT}',
    group_id='reporting-consumer',
    # to deserialize kafka.producer.object into dict
    value_deserializer=lambda m: json.loads(m.decode('utf-8'))
)
for inf in consumer:
    try:
        database.save_log(inf)
    except:
        raise ConsumerException()

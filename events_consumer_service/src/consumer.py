from kafka import KafkaConsumer
import json

import settings
from pip._vendor.requests import ReadTimeout
from src import service


consumer = KafkaConsumer(
    *settings.KAFKA_TOPICS.split(","),
    bootstrap_servers=f'{settings.KAFKA_SERVER}:{settings.KAFKA_PORT}',
    group_id='reporting-consumer',
    # to deserialize kafka.producer.object into dict
    value_deserializer=lambda m: json.loads(m.decode('utf-8'))
)
for inf in consumer:
    try:
        service.events_service_manager(inf.topic, inf.value)
        # database.save_log(inf)
    except ReadTimeout:
        print("Query timed out:")

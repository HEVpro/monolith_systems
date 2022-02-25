import os

# KAFKA
KAFKA_TOPICS = os.environ.get('KAFKA_TOPICS', "User,Link,LinkMetric")
KAFKA_SERVER = "kafka:19092"

# DB
MONGO_HOST = os.environ.get('MONGO_HOST', 'mongodb://localhost')
MONGO_PORT = os.environ.get('MONGO_PORT', '27017')
MONGO_DATABASE = os.environ.get('MONGO_DATABASE', 'events_log')

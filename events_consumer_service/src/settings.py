import os

# KAFKA
KAFKA_TOPICS = os.environ.get('KAFKA_TOPICS', "User,Link,LinkMetric")
KAFKA_SERVER = os.environ.get('KAFKA_ADVERTISED_HOST_NAME', "kafka")
KAFKA_PORT = os.environ.get('KAFKA_ADVERTISED_PORT', "19092")

# SCYLLA
SCYLLA_HOST = 'reporting-scylla-db'
SCYLLA_PORT = 9042
# ELASTIC




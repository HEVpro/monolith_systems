from datetime import datetime

from cassandra.cluster import Cluster
from cassandra import ConsistencyLevel

from src import settings

cluster = Cluster(
    contact_points=[settings.SCYLLA_HOST],
    port=settings.SCYLLA_PORT)
session = cluster.connect(keyspace="user_logs")
session.default_consistency_level = ConsistencyLevel.QUORUM

if session:
    if cluster.is_shard_aware():
        print("connected to a scylla cluster")
    session.execute("""
            CREATE TABLE IF NOT EXISTS user_events (
                user_id text,
                username text,
                maxRequests int,
                operation text,
                event_creation_at timestamp,
                updated_at timestamp,
                created_on timestamp,
                PRIMARY KEY ((username, operation), event_creation_at))
       """)
    insert_query = "INSERT INTO user_events (user_id, username, maxRequests, operation, event_creation_at, updated_at, created_on) VALUES (?, ?, ?, ?, ?, ?, ?)";

def insert_user_event(user_id=None, username=None, max_requests=None, operation=None,
                      event_creation_at=None, updated_at=None, created_on=None):
    prepared = session.prepare(insert_query)
    print(f"\nAdding event of user {username}")
    session.execute(prepared, (user_id, username, max_requests, operation,
                      event_creation_at, updated_at, created_on))
    print("Added.\n")

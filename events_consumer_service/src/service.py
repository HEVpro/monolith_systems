import json
from datetime import datetime

from pip._vendor.requests import ReadTimeout

from db import elastic_db, scylla_db


def events_service_manager(topic, data):
    entity = json.loads(data['entity'])
    created = datetime.now()
    operation = data['operation']
    if topic == 'User':
        try:
            updated_at = datetime.strptime(entity['updatedOn'][:-3], "%Y-%m-%dT%H:%M:%S.%f")
            created_at = datetime.strptime(entity['createdOn'][:-3], "%Y-%m-%dT%H:%M:%S.%f")
            scylla_db.insert_user_event(entity['id'], entity['username'], int(entity['max_requests']), operation, created, updated_at, created_at)
        except ReadTimeout:
            print("Query timed out")
    else:
        try:
            elastic_db.save_events(topic, data)
        except ReadTimeout:
            print("Query timed out")


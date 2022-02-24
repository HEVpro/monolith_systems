import json

import pymongo

import settings

client = pymongo.MongoClient(f'{settings.MONGO_HOST}:{settings.MONGO_PORT}/')
db = client['events_log']
logs_collection = db['logs_collection']


def save_log(data):
    data_value = json.loads(data.value['entity'])
    document = {
        "topic": data.topic,
        "operation": data.value['operation'],
    }
    for item in data_value:
        document[item] = data_value[item]
    logs_collection.insert_one(document)

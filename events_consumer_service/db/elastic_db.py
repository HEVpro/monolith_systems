import json

from elasticsearch import Elasticsearch

es = Elasticsearch('http://es01:9200')
print("connected to a elasticsearch")
mapping = {
    "mappings": {
        "properties": {
            "created": {
                "type": "text"
            },
            "operation": {
                "type": "text"
            },
            "entity": {
                "type": "object"
            }
        }
    }
}
es.indices.create(index='events_log', ignore=400, body=mapping)




def save_events(topic, event):
    print(f'Adding event of topic: {topic}')
    res = es.index(index='events_log', doc_type=topic, body=json.dumps(event))
    event_id = res['_id']
    print(f'Added event: {event_id}')

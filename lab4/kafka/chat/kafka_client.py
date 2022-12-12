import json 
from datetime import datetime
from kafka import KafkaProducer, KafkaConsumer
import sys

import threading
def listen_for_messages():
    consumer = KafkaConsumer(
        'chat',
        bootstrap_servers='localhost:9092',
        auto_offset_reset='earliest'
    )
    try:
        for m in consumer:
            message = m.value.decode("utf-8")
            splitted = message.split("#") 
            if len(splitted) >= 2:
                name = splitted[0]
                text = splitted[1]
                print(f"{name}: {text}")
    except KeyboardInterrupt or IndentationError:
        sys.exit()
        
# Messages will be serialized as JSON 
def serializer(message):
    return json.dumps(message).encode('utf-8')


# Kafka Producer
producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    value_serializer=serializer
)

name = input("Enter you name: ")

t = threading.Thread(target=listen_for_messages)
t.start()

while True:
    message = input("\nEnter message: \n")
    producer.send("messages", name+"#"+message)
    producer.flush()



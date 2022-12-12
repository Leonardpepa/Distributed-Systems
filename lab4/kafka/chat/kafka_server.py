import json 
from kafka import KafkaConsumer, KafkaProducer

def serializer(message):
    # return json.dumps(message).encode('utf-8')
    return str(message).encode('utf-8')

producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    value_serializer=serializer
)

if __name__ == '__main__':
    # Kafka Consumer 
    consumer = KafkaConsumer(
        'messages',
        bootstrap_servers='localhost:9092',
        auto_offset_reset='earliest'
    )
    for message_revc in consumer:
        message = message_revc.value.decode('utf-8')
        producer.send("chat", message)
        producer.flush()
        
        
     
    
#!/usr/bin/env python
import pika
import uuid
import json

class ConnectionHandler:
    def __init__(self):
        self.connection = pika.BlockingConnection(
            pika.ConnectionParameters(host='localhost'))

        self.channel = self.connection.channel()

        self.result = self.channel.queue_declare(queue='', exclusive=True)
        self.callback_queue = self.result.method.queue

        self.channel.basic_consume(
            queue=self.callback_queue,
            on_message_callback=self.on_response,
            auto_ack=True)

    def on_response(self, ch, method, props, body):
        if self.corr_id == props.correlation_id:
            self.response = json.loads(body)

    def call(self, request):
        self.response = None
        self.corr_id = str(uuid.uuid4())
        self.channel.basic_publish(
        exchange='',
        routing_key='rpc_queue',
        properties=pika.BasicProperties(
            reply_to=self.callback_queue,
            correlation_id=self.corr_id,
        ),
        body=json.dumps(request))
        self.connection.process_data_events(time_limit=None)
        return self.response


#!/usr/bin/env python
import pika
import json
import db as db
db.initialize_db();

from server_protocol import ATM_API

def process_request(api, request):
    if request["type"] == "auth":
        return api.auth(id=request["id"], pin=request["pin"])
    if request["type"] == "register":
        return api.create(id=request["id"], pin=request["pin"], name=request["name"])
    if request["type"] == "deposit":
        return api.deposit(uid=request["uid"], amount=request["amount"])
    if request["type"] == "withdraw":
        return api.withdraw(uid=request["uid"], amount=request["amount"])
    if request["type"] == "transfer":
        return api.transfer(uid=request["uid"], id_from=request["from_id"], id_to=request["to_id"], name_to=request["name_to"],amount=request["amount"])
    if request["type"] == "balance":
        return api.balance(uid=request["uid"])
    if request["type"] == "info":
        return api.info(uid=request["uid"])
    if request["type"] == "statements":
        return api.get_statements(uid=request["uid"])
    if request["type"] == "logout":
        return api.logout(uid=request["uid"])



def main():
    
    api = ATM_API()
    
    def on_request(ch, method, props, body):
        request = json.loads(body)
        response = process_request(api, request)
        ch.basic_publish(exchange='',
                        routing_key=props.reply_to,
                        properties=pika.BasicProperties(correlation_id = \
                                                            props.correlation_id),
                        body=json.dumps(response))
        ch.basic_ack(delivery_tag=method.delivery_tag)
    
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(host='rabbitmq'))

    channel = connection.channel()

    channel.queue_declare(queue='rpc_queue')

    channel.basic_qos(prefetch_count=1)
    channel.basic_consume(queue='rpc_queue', on_message_callback=on_request)

    print(" [x] Awaiting RPC requests")
    channel.start_consuming()



if __name__ == '__main__':
    main()
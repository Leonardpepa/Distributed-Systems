#!/usr/bin/env python
import json
import db as db
db.initialize_db()
from server_protocol import ATM_API

import time
import zmq


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
    
    context = zmq.Context()
    socket = context.socket(zmq.REP)
    socket.bind("tcp://*:6666")
    print("Listening on 6666")
    
    while True:
        request = socket.recv_string()
        
        response = process_request(api, json.loads(request))
        response = str(json.dumps(response))
        socket.send_string(response)    
        
    


if __name__ == '__main__':
    main()
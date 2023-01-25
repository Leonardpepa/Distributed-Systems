#!/usr/bin/env python
import json
import db as db
db.initialize_db()
from server_protocol import ATM_API
import zerorpc


def main():
    api = ATM_API()
    s = zerorpc.Server(api)
    s.bind("tcp://0.0.0.0:4242")
    print("Listening on port 4242")
    s.run()    


if __name__ == '__main__':
    main()
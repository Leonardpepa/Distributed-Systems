#
#   Hello World client in Python
#   Connects REQ socket to tcp://localhost:5555
#   Sends "Hello" to server, expects "World" back
#

import zmq
import sys
context = zmq.Context()

#  Socket to talk to server
print("Connecting to echo server…")
socket = context.socket(zmq.REQ)
socket.connect("tcp://localhost:6666")

#  Do 10 requests, waiting each time for a response
while True:
    request = input("Enter something or (exit): ")
    
    if request == "exit":
        sys.exit()
    
    print(f"Sending request {request} …")
    socket.send_string(request)

    #  Get the reply.
    message = socket.recv_string()
    print(f"Received reply [ {message} ]")
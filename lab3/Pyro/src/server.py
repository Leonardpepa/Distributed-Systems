import Pyro4
import db
# import after db (we need the database initialized)
from server_protocol import ATM_API
from Pyro4.naming import startNS, startNSloop
import threading

def start_name_server():
    print("Starting name server")
    startNSloop()
    

def main():
    
    name_server_thread = threading.Thread(target=start_name_server)
    name_server_thread.start()
    
    daemon = Pyro4.Daemon()            # make a Pyro daemon
    
    ns = Pyro4.locateNS()              # find the name server
    
    uri = daemon.register(ATM_API)     # register the API maker as a Pyro object
    
    ns.register("ATM_API", uri)        # register the object with a name in the name server

    print("Server Is Ready To Welcome new Clients.") 
    
    daemon.requestLoop()               # start the event loop of the server to wait for calls

if __name__ == "__main__":
    print("Starting the app")
    main()
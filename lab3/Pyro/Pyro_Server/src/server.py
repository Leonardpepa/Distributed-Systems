import Pyro4
from server_protocol import ATM_API
import db

def main():
    db.initialize_db()
    daemon = Pyro4.Daemon()            # make a Pyro daemon
    ns = Pyro4.locateNS()              # find the name server
    uri = daemon.register(ATM_API)     # register the API maker as a Pyro object
    
    ns.register("ATM_API", uri)        # register the object with a name in the name server

    print("Server Is Ready To Welcome new Clients.") 
    
    daemon.requestLoop()               # start the event loop of the server to wait for calls

if __name__ == "__main__":
    main()
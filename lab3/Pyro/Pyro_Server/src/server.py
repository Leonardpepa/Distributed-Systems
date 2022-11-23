import Pyro4
from service import ATM_Service
import db

def main():
    db.initialize_db()   
    
    daemon = Pyro4.Daemon()                # make a Pyro daemon
    ns = Pyro4.locateNS()                  # find the name server
    uri = daemon.register(ATM_Service)     # register the greeting maker as a Pyro object
    ns.register("ATM_Service", uri)        # register the object with a name in the name server

    print("Server Is Ready To Welcome new Clients.") 
    
    daemon.requestLoop()                   # start the event loop of the server to wait for calls

if __name__ == "__main__":
    main()
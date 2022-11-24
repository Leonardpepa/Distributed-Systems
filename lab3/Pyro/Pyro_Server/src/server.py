import Pyro4
from service import ATM_Service
import db
import account_repository as repo
from threading import RLock

@Pyro4.behavior(instance_mode="single")
class MyThreadSafety:
    def __init__(self) -> None:
        self.locks = {}
    
    def lock(self, id: int):
        lock = self.locks.get(id)
        if lock is None:
             raise Exception(f"Exception | Lock object is None for ID: {id}")
        lock.acquire()
    
    def unlock(self, id: int):
        lock = self.locks.get(id)
        if lock is None:
            raise Exception(f"Exception | Lock object is None for ID: {id}")
        lock.release()
    
    def put_lock(self, id: int):
        if self.locks.get(id) is None:
            self.locks[id] = RLock()
    
    def _initialize_locks(self, id_list):
        for id in id_list:
            self.put_lock(id)

def main():
    db.initialize_db()
    conn, cur = db.db_connection()
    id_list = repo.get_all_accounts(cur)
    cur.close()
    conn.close()
    id_list = [int(el["id"]) for el in id_list]   
    threadSafety = MyThreadSafety()
    threadSafety._initialize_locks(id_list)
    atm_Service = ATM_Service(threadSafety)
    daemon = Pyro4.Daemon()                # make a Pyro daemon
    ns = Pyro4.locateNS()                  # find the name server
    uri = daemon.register(atm_Service)     # register the greeting maker as a Pyro object
    ns.register("ATM_Service", uri)        # register the object with a name in the name server

    print("Server Is Ready To Welcome new Clients.") 
    
    daemon.requestLoop()                   # start the event loop of the server to wait for calls

if __name__ == "__main__":
    main()
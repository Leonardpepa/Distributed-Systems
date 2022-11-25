import Pyro4
import statement_repository
import db
import account_repository as acc_repo
from account import Account
from statement import Statement
import utils
import datetime
from threading import RLock

# class that holds the locks for each account
# ensures thread safety
@Pyro4.behavior(instance_mode="single")
class MyThreadSafety:
    def __init__(self, id_list) -> None:
        self.locks = {}
        self._initialize_locks(id_list)
    
    # return the lock for the corresponding account
    def lock(self, id: int):
        lock = self.locks.get(id)
        if lock is None:
             raise Exception(f"Exception | Lock object is None for ID: {id}")
        return lock
    
    # insert a lock for the corresponding account
    def put_lock(self, id: int):
        if self.locks.get(id) is None:
            self.locks[id] = RLock()
    
    # return the locks for 2 account's based on the id
    # prevent deadlock's
    def two_face_lock(self, idA: int, idB: int):
        if idA < idB:
            return self.lock(idA), self.lock(idB)
        else:
            return self.lock(idB), self.lock(idA)
    
    def _initialize_locks(self, id_list):
        for id in id_list:
            self.put_lock(id)


 # Class that holds global locks for clients
 # needs to be gloabl to be shared between clients
threadSafety = MyThreadSafety(acc_repo.get_all_accounts())
@Pyro4.behavior(instance_mode="session")
@Pyro4.expose
class ATM_API(object):
    # connect to db
    conn, cursor = db.db_connection()
    
    def __init__(self) -> None:
        self.id = -1
    
    # udate daily limit when a day passes
    def update_daily_limit(self, id, db_date):
        if utils.day_passed(datetime.date.today(), db_date) >= 1:
            acc_repo.update_daily_limit(self.cursor, id, 900)
    
    # get account info
    def info(self, id: int):
        if self.id != id:
            return False, {"message": "Cannot find user, please login correctly."}
        
        with threadSafety.lock(id):    
            ok, result = acc_repo.read(cursor=self.cursor, id=id)
            if ok:
                self.update_daily_limit(id, result["date"])
                return True, result
            else:
                return False, {"message": "Cannot find user, please login correctly."}
    
    # login an account
    def auth(self, id: int, pin: int):  
        ok, result = acc_repo.auth(cursor=self.cursor, id=id, pin=pin)
        if ok:
            self.id = result["id"]
            self.update_daily_limit(id, result["date"])
            threadSafety.put_lock(id)
            return True, result
        
        return False, {"message": "Authentication failed, wrong credentials."}
    
    # register an account
    def create(self, id: int, pin: int, name: str):
        if len(name) == 0:
            return False, {"message": "Name cannot be empty."}
        
        ok, accountFound = acc_repo.read(cursor=self.cursor, id=id)
        
        if ok:
            return False, {"message": f"Account with ID: {id} already exists."}
        
        threadSafety.put_lock(id)
        with threadSafety.lock(id):        
            newAcc = Account(id, pin, name, 0)
            
            ok = acc_repo.create(cursor=self.cursor, acc=newAcc)
            
            if ok:
                self.id = id
                return True, {}
            else:
                return False, {"message": "Error occurred while creating the account, please try again later."}
    
    # deposit money from an  account                 
    def deposit(self, id: int, amount: float):
        with threadSafety.lock(id):
            if amount <= 0:
                return False, {"message": "Amount must be greater than 0."}
            
            ok, account = acc_repo.read(self.cursor, id)
            
            if not ok:
                return False, {"message": "Something went wrong try again, please try again later."}
            
            newAcc = Account(account["id"], None, account["name"], account["balance"] + amount, account["limit"])
            ok, result = acc_repo.update(self.cursor, newAcc)
            
            if not ok:
                return False, {"message": "Something went wrong try again, please try again later."}
            
            statement_repository.create(self.cursor, Statement(id, "deposit", f"Deposited {amount}", None))
            return True, result
    
    # withdraw money from an  account            
    def withdraw(self, id: int,  amount: float):
        if self.id != id:
            return False, {"message": "Bad Request."}
        
        with threadSafety.lock(id):        
            if amount <= 0:
                return False, {"message": "Amount must be greater than 0."}
            
            ok, account = acc_repo.read(self.cursor, id)
            
            if not ok:
                return False, {"message": "Something went wrong try again, please try again later."}
            
            if account["limit"] - amount < 0:
                limit = account["limit"]
                return False, {"message": f"Daily limit cannot be exceeded, you can only spend {limit} more for today."}
            
            if account["balance"] - amount < 0:
                return False, {"message": "Balance is not enough."}
            
            newAcc = Account(account["id"], None, account["name"], account["balance"] - amount, account["limit"] - amount)

            ok, result = acc_repo.update(self.cursor, newAcc)
            
            if not ok:
                return False, {"message": "Something went wrong try again, please try again later."}
            
            statement_repository.create(self.cursor, Statement(id, "withdraw", f"Withdraw {amount}", None))
            return True, result
    
    # tranfer money between accounts   
    def transfer(self, id_from, id_to: int, name_to: str, amount: float):
        if self.id != id_from:
            return False, {"message": "Bad Request."}
        
        if id_from == id_to:
            return False, {"message": "You cannot tranfer money to urself."}
        
        lockA, lockB = threadSafety.two_face_lock(id_from, id_to) 
        
        # lock both clients for the transaction
        with lockA:
            with lockB:
                if amount <= 0:
                    return False, {"message": "Amount must be greater than 0."}
                
                ok, acc_to_transfer = acc_repo.read(self.cursor, id_to)
                
                if not ok:
                    return False, {"message": "The account you want to transfer doesn't exist."}
                
                if acc_to_transfer["name"] != name_to:
                    return False, {"message": "Wrong account details tranfer cannot be completed."}
                
                ok, result = self.withdraw(id_from, amount)
                
                if not ok:
                    return False, result
                
                ok, result2 = self.deposit(id_to, amount)
                
                if not ok:
                    return False, result

                statement_repository.create(self.cursor, Statement(id_from, "transfer", f"{amount} transfered from ID: {id_from} to ID: {id_to}", None))
                return True, result
    
    # check the account balance
    def balance(self, id: int):
        if self.id != id:
            return False, {"message": "Bad Request."}
        
        with threadSafety.lock(id):
            ok, result = acc_repo.read(self.cursor, id)
            if not ok:
                return False, {"message": "Something went wrong try again, please try again later."}       
            return True, result
    
    # get bank statements for an account
    def get_statements(self, id: int):
        if self.id != id:
            return False, {"message": "Bad Request."}
        ok, result = statement_repository.read_by_acc_id(self.cursor, id)
        if ok:
            return True, result
        else:
            return False, {"message": "Something went wrong try again, please try again later."}

    def logout(self, id: int):
        self.id = -1
        return True, {"message": "User logged out."}
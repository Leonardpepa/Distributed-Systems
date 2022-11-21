import Pyro4
import db
from account import Account

@Pyro4.expose
class ATM_Service(object):
    # connect to db
    conn, cursor = db.db_connection()
    id = -1
    
    def create(self, id: int, pin: int, name: str):
        accountFound = db.read(cursor=self.cursor, id=id)
        
        if accountFound:
            return False, f"Account with id: {id} already exists"
        
        newAcc = Account(id, pin, name, 0)
        result = db.create(cursor=self.cursor, acc=newAcc)
        if result:
            self.id = id
            return True, id, name ,"Account created successfuly"
        else:
            return False, "Error occurred while creating the account"
    
    def auth(self, id: int, pin: int):  
        result = db.auth(cursor=self.cursor, id=id, pin=pin)
        if result:
            self.id = result[0]
            return True, result[0], result[2]
        return False, "Authentication failed"
    
    def deposit(self, amount: float):
        if amount <= 0:
            return False, "Amount cannot be <= 0"
        
        account = db.read(self.cursor, self.id)
        
        if not account:
            return False, "An error occured please try again"
        
        newAcc = Account(account[0], account[1], account[2], account[3] + amount)
        result = db.update(self.cursor, newAcc)
        
        if not result:
            return False, "An error occured please try again"
        
        return True, newAcc.balance
        
    def withdraw(self, amount: float):
        if amount <= 0:
            return False, "Amount cannot be <= 0"
        
        account = db.read(self.cursor, self.id)
        
        if not account:
            return False, "An error occured please try again"
        
        if account[3] - amount < 0:
            return False, "Balance is not enough"
        
        newAcc = Account(account[0], account[1], account[2], account[3] - amount)
        result = db.update(self.cursor, newAcc)
        
        if not result:
            return False, "Something went wrong"
        else:
            return True, newAcc.balance
    
    def balance(self):
        account = db.read(self.cursor, self.id)
        
        if not account:
            return False, "An error occured please try again"
        return True, account[3]
        
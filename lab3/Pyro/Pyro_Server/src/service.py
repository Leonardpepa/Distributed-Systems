import Pyro4
import db
from account import Account

@Pyro4.expose
class ATM_Service(object):
    # connect to db
    conn, cursor = db.db_connection()
    id = -1
    
    def info(self):
        if self.id == -1 or self.id <= 0:
            return False, {"message": "Cannot find user please login correctly."}
        ok, result =db.read(cursor=self.cursor, id=self.id)
        if ok:
            return True, result
        else:
            return False, {"message": "Cannot find user please login correctly."}

    def logout(self):
        self.id = -1
        return True, {"message": "User is logged out."}
    
    def auth(self, id: int, pin: int):  
        ok, result = db.auth(cursor=self.cursor, id=id, pin=pin)
        if ok:
            self.id = result["id"]
            return True, result
        
        return False, {"message": "Authentication failed"}
    
    
    def create(self, id: int, pin: int, name: str):
        ok, accountFound = db.read(cursor=self.cursor, id=id)
        
        if ok:
            return False, {"message": f"Account with id: {id} already exists"}
        
        newAcc = Account(id, pin, name, 0)
        
        ok = db.create(cursor=self.cursor, acc=newAcc)
        
        if ok:
            self.id = id
            return True, {}
        else:
            return False, {"message": "Error occurred while creating the account"}
    
    def deposit(self, amount: float):
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0"}
        
        ok, account = db.read(self.cursor, self.id)
        
        if not ok:
            return False, {"message": "An error occured please try again"}
        
        newAcc = Account(account["id"], None, account["name"], account["balance"] + amount)
        ok, result = db.update(self.cursor, newAcc)
        
        if not ok:
            return False, {"message": "An error occured please try again"}
        
        return True, result
        
    def withdraw(self, amount: float):
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0"}
        
        ok, account = db.read(self.cursor, self.id)
        
        if not ok:
            return False, {"message": "An error occured please try again"}
        
        if account["balance"] - amount < 0:
            return False, {"message": "Balance is not enough"}
        
        newAcc = Account(account["id"], None, account["name"], account["balance"] - amount)

        ok, result = db.update(self.cursor, newAcc)
        
        if not ok:
            return False, {"message": "An error occured please try again"}
        else:
            return True, result
    
    def balance(self):
        ok, result = db.read(self.cursor, self.id)
        
        if not ok:
            return False, {"message": "An error occured please try again"}
        return True, result
        
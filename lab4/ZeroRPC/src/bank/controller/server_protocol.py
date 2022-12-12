import src.bank.model.statement_repository as statement_repository
import src.bank.model.db as db
import src.bank.model.account_repository as acc_repo
from src.bank.model.account import Account
from src.bank.model.statement import Statement
import src.bank.model.utils as utils
import datetime
import uuid

class ATM_API(object):
    # connect to db
    conn, cursor = db.db_connection()
    logged_in_clients = {}
        
    # udate daily limit when a day passes
    def update_daily_limit(self, id, db_date):
        if utils.day_passed(datetime.date.today(), db_date) >= 1:
            acc_repo.update_daily_limit(self.cursor, id, 900)
    
    # get account info
    def info(self, uid):
        if self.logged_in_clients.get(uid) == None:
            return False, {"message": "Cannot find user, please login correctly."}
    
        id = self.logged_in_clients.get(uid) 
        ok, result = acc_repo.read(cursor=self.cursor, id=id)
        
        if ok:
            self.update_daily_limit(id, result["date"])
            return True, result
        else:
            return False, {"message": "Cannot find user, please login correctly."}

    # login an account
    def auth(self, id, pin):  
        
        if id <= 0:
            return False, {"message": "ID must be greater than zero."}
        
        if pin <= 999:
            return False, {"message": "Password should be greater than 3 digits."}
        
        ok, result = acc_repo.auth(cursor=self.cursor, id=id, pin=pin)
        
        if ok:
            unique_id = str(uuid.uuid4())
            result["uid"] = unique_id
            self.logged_in_clients[unique_id] = id
            self.update_daily_limit(id, result["date"])
            return True, result
        
        return False, {"message": "Authentication failed, wrong credentials."}
    
    # register an account
    def create(self, id, pin, name):
        
        if id <= 0:
            return False, {"message": "ID must be greater than zero."}
        
        if pin <= 999:
            return False, {"message": "Password should be greater than 3 digits."}
        
        if len(name) == 0:
            return False, {"message": "Name cannot be empty."}
        
        ok, accountFound = acc_repo.read(cursor=self.cursor, id=id)
        
        if ok:
            return False, {"message": f"Account with ID: {id} already exists."}
        
        newAcc = Account(id, pin, name, 0)
        
        ok = acc_repo.create(cursor=self.cursor, acc=newAcc)
        
        if ok:
            unique_id = str(uuid.uuid4())
            self.logged_in_clients[unique_id] = id
            return True, {"uid": unique_id}
        else:
            return False, {"message": "Error occurred while creating the account, please try again later."}
    
    # deposit money from an  account                 
    def deposit(self, uid, amount):
        if self.logged_in_clients.get(uid) == None:
            return False, {"message": "Cannot find user, please login correctly."}
        
        id = self.logged_in_clients.get(uid)
        return self.deposit_helper(id, amount)
        
    
    def deposit_helper(self, id, amount):
        if id <= 0:
            return False, {"message": "Bad request, please login correctly."}
        
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0."}
        
        ok, account = acc_repo.read(self.cursor, id)
        
        if not ok:
            return False, {"message": "Something went wrong, please try again later."}
        
        newAcc = Account(account["id"], None, account["name"], account["balance"] + amount, account["limit"])
        ok, result = acc_repo.update(self.cursor, newAcc)
        
        if not ok:
            return False, {"message": "Something went wrong, please try again later."}
        
        statement_repository.create(self.cursor, Statement(id, "deposit", f"Deposited {amount}", None))
        return True, result

    
    # withdraw money from an  account            
    def withdraw(self, uid,  amount):
        if self.logged_in_clients.get(uid) == None:
            return False, {"message": "Bad Request."}
        
        id = self.logged_in_clients.get(uid)
        
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0."}
        
        ok, account = acc_repo.read(self.cursor, id)
        
        if not ok:
            return False, {"message": "Something went wrong, please try again later."}
        
        if account["limit"] - amount < 0:
            limit = account["limit"]
            return False, {"message": f"Daily limit cannot be exceeded, you can only spend {limit} more for today."}
        
        if account["balance"] - amount < 0:
            return False, {"message": "Balance is not enough."}
        
        newAcc = Account(account["id"], None, account["name"], account["balance"] - amount, account["limit"] - amount)

        ok, result = acc_repo.update(self.cursor, newAcc)
        
        if not ok:
            return False, {"message": "Something went wrong, please try again later."}
        
        statement_repository.create(self.cursor, Statement(id, "withdraw", f"Withdraw {amount}", None))
        return True, result
    
    # tranfer money between accounts   
    def transfer(self, uid, id_from, id_to, name_to, amount):
        
        if self.logged_in_clients.get(uid) != id_from:
            return False, {"message": "Bad Request."}
        
        if id_from == id_to:
            return False, {"message": "You cannot tranffer money to urself."}
        
        if amount <= 0:
                    return False, {"message": "Amount must be greater than 0."}
        
        ok, _ = acc_repo.read(self.cursor, id_to)
        
        if not ok:
            return False, {"message": "The account you want to transfer doesn't exist."}
                    
        ok, acc_to_transfer = acc_repo.read(self.cursor, id_to)
            
        if not ok:
            return False, {"message": "Something went wrong, please try again later."}
        
        if acc_to_transfer["name"].lower() != name_to.lower():
            return False, {"message": "Wrong account details, transfer cannot be completed."}
        
        ok, result = self.withdraw(uid, amount)
        
        if not ok:
            return False, result
        
        ok, _ = self.deposit_helper(id_to, amount)
        
        if not ok:
            return False, result

        statement_repository.create(self.cursor, Statement(id_from, "transfer", f"{amount} transferred from ID: {id_from} to ID: {id_to}", None))
        return True, result

    # check the account balance
    def balance(self, uid):
        if self.logged_in_clients.get(uid) == None:
            return False, {"message": "Bad Request."}
        id = self.logged_in_clients.get(uid)
        ok, result = acc_repo.read(self.cursor, id)
        if not ok:
            return False, {"message": "Something went wrong, please try again later."}       
        return True, result
    
    # get bank statements for an account
    def get_statements(self, uid):
        if self.logged_in_clients.get(uid) == None:
            return False, {"message": "Bad Request."}
        
        id = self.logged_in_clients.get(uid)
        
        ok, result = statement_repository.read_by_acc_id(self.cursor, id)
        if ok:
            return True, result
        else:
            return False, {"message": "Something went wrong, please try again later."}

    def logout(self, uid):
        self.logged_in_clients.pop(uid, None)
        return True, {"message": "User logged out."}
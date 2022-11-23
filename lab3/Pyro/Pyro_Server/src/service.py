import Pyro4
import statement_repository
import db
import account_repository as acc_repo
from account import Account
from statement import Statement



@Pyro4.expose
class ATM_Service(object):
    # connect to db
    conn, cursor = db.db_connection()
    id = -1
    
    def info(self, id: int):
        if self.id == -1 or self.id <= 0 or self.id != id:
            return False, {"message": "Cannot find user please login correctly."}
        ok, result =acc_repo.read(cursor=self.cursor, id=id)
        if ok:
            statement_repository.create(self.cursor, Statement(id, "info", "Checked account info", None))
            return True, result
        else:
            return False, {"message": "Cannot find user please login correctly."}

    def logout(self, id: int):
        self.id = -1
        return True, {"message": "User is logged out."}
    
    def auth(self, id: int, pin: int):  
        ok, result = acc_repo.auth(cursor=self.cursor, id=id, pin=pin)
        if ok:
            self.id = result["id"]
            return True, result
        
        return False, {"message": "Authentication failed"}
    
    
    def create(self, id: int, pin: int, name: str):
        
        if len(name) == 0:
            return False, {"message": "Name cannot be empty"}
        
        ok, accountFound = acc_repo.read(cursor=self.cursor, id=id)
        
        if ok:
            return False, {"message": f"Account with id: {id} already exists"}
        
        newAcc = Account(id, pin, name, 0)
        
        ok = acc_repo.create(cursor=self.cursor, acc=newAcc)
        
        if ok:
            self.id = id
            return True, {}
        else:
            return False, {"message": "Error occurred while creating the account"}
    
    def deposit(self, id: int, amount: float):
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0"}
        
        ok, account = acc_repo.read(self.cursor, id)
        
        if not ok:
            return False, {"message": "An error occured please try again"}
        
        newAcc = Account(account["id"], None, account["name"], account["balance"] + amount)
        ok, result = acc_repo.update(self.cursor, newAcc)
        
        if not ok:
            return False, {"message": "An error occured please try again"}
        
        statement_repository.create(self.cursor, Statement(id, "deposit", f"Deposit {amount}", None))
        return True, result
        
    def withdraw(self, id: int,  amount: float):
        if self.id != id:
            return False, {"message": "Bad Request"}
        
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0"}
        
        ok, account = acc_repo.read(self.cursor, id)
        
        if not ok:
            return False, {"message": "An error occured please try again"}
        
        if account["balance"] - amount < 0:
            return False, {"message": "Balance is not enough"}
        
        newAcc = Account(account["id"], None, account["name"], account["balance"] - amount)

        ok, result = acc_repo.update(self.cursor, newAcc)
        
        if not ok:
            return False, {"message": "An error occured please try again"}
        
        statement_repository.create(self.cursor, Statement(id, "withdraw", f"Withdraw {amount}", None))
        return True, result
    
    def balance(self, id: int):
        if self.id != id:
            return False, {"message": "Bad Request"}
        
        ok, result = acc_repo.read(self.cursor, id)
        
        if not ok:
            return False, {"message": "An error occured please try again"}
        
        balance = result["balance"]
        statement_repository.create(self.cursor, Statement(id, "balance", f"Checked balance: {balance}", None))
        return True, result
    
    def transfer(self, id_from, id_to: int, name_to: str, amount: float):
        if self.id != id_from:
            return False, {"message": "Bad Request"}
        
        if id_from == id_to:
            return False, {"message": "You cannot tranfer money to urself"}
            
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0"}
        
        ok, acc_to_transfer = acc_repo.read(self.cursor, id_to)
        
        if not ok:
            return False, {"message": "The account you want to transfer doesn't exist"}
        
        if acc_to_transfer["name"] != name_to:
            return False, {"message": "Wrong account details"}
        
        ok, result = self.withdraw(id_from, amount)
        
        if not ok:
            return False, {"message": "Something went wrong try again"}

        
        ok, result2 = self.deposit(id_to, amount)
        
        if not ok:
            return False, {"message": "Something went wrong try again"}


        statement_repository.create(self.cursor, Statement(id_from, "Transfer", f"{amount} from ID: {id_from} to ID: {id_to}", None))
        return True, result
        
    
    def get_statements(self, id: int):
        if self.id != id:
            return False, {"message": "Bad Request"}
        ok, result = statement_repository.read_by_acc_id(self.cursor, id)
        if ok:
            return True, result
        else:
            return False, {"message": "Something went wrong"}

        
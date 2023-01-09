from spyne import Application, rpc, ServiceBase, Iterable, Integer, Unicode, String, Float
from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication


import statement_repository as statement_repository
import db as db
import account_repository as acc_repo
from account import Account
from statement import Statement
import utils as utils
import datetime
import uuid
import json


conn, cursor = db.db_connection()
logged_in_clients = {}

def update_daily_limit(id, db_date):
        if utils.day_passed(datetime.date.today(), db_date) >= 1:
            acc_repo.update_daily_limit(cursor, id, 900)
    
def serialize_json(object):
    return str(json.dumps(object))


def deposit_helper(id, amount):
        if id <= 0:
            return False, {"message": "Bad request, please login correctly."}
        
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0."}
        
        ok, account = acc_repo.read(cursor, id)
        
        if not ok:
            return False, {"message": "Something went wrong, please try again later."}
        
        newAcc = Account(account["id"], None, account["name"], account["balance"] + amount, account["limit"])
        ok, result = acc_repo.update(cursor, newAcc)
        
        if not ok:
            return False, {"message": "Something went wrong, please try again later."}
        
        statement_repository.create(cursor, Statement(id, "deposit", f"Deposited {amount}", None))
        return True, result

def withdrawHelper(uid, amount):
    if logged_in_clients.get(uid) == None:
        return False, {"message": "Bad Request."}
        
    id = logged_in_clients.get(uid)
    
    if amount <= 0:
        return False, {"message": "Amount must be greater than 0."}
    
    ok, account = acc_repo.read(cursor, id)
    
    if not ok:
        return False, {"message": "Something went wrong, please try again later."}
    
    if account["limit"] - amount < 0:
        limit = account["limit"]
        return False, {"message": f"Daily limit cannot be exceeded, you can only spend {limit} more for today."}
    
    if account["balance"] - amount < 0:
        return False, {"message": "Balance is not enough."}
    
    newAcc = Account(account["id"], None, account["name"], account["balance"] - amount, account["limit"] - amount)

    ok, result = acc_repo.update(cursor, newAcc)
    
    if not ok:
        return False, {"message": "Something went wrong, please try again later."}
    
    statement_repository.create(cursor, Statement(id, "withdraw", f"Withdraw {amount}", None))
    return True, result


class ATM_API(ServiceBase):
    # login an account
    @rpc(Integer, Integer, _returns=String)
    def auth(self, id, pin):  
        if id <= 0:
            return serialize_json((False, {"message": "ID must be greater than zero."}))
        
        if pin <= 999:
            return serialize_json((False, {"message": "Password should be greater than 3 digits."}))
        
        ok, result = acc_repo.auth(cursor=cursor, id=id, pin=pin)
        
        if ok:
            unique_id = str(uuid.uuid4())
            result["uid"] = unique_id
            logged_in_clients[unique_id] = id
            update_daily_limit(id, result["date"])
            return serialize_json((True, result))
        
        return serialize_json((False, {"message": "Authentication failed, wrong credentials."}))


    # get account info
    @rpc(String, _returns=String)
    def info(self, uid):
        if logged_in_clients.get(uid) == None:
            return serialize_json((False, {"message": "Cannot find user, please login correctly."}))
        id = logged_in_clients.get(uid) 
        ok, result = acc_repo.read(cursor=cursor, id=id)
        
        if ok:
            update_daily_limit(id, result["date"])
            return serialize_json((True, result))
        else:
            return serialize_json(( False, {"message": "Cannot find user, please login correctly."}))
    
    # register an account
    @rpc(Integer, Integer, String,_returns=String)
    def create(self, id, pin, name):
        
        if id <= 0:
            return serialize_json((False, {"message": "ID must be greater than zero."}))
        
        if pin <= 999:
            return serialize_json((False, {"message": "Password should be greater than 3 digits."}))
        
        if len(name) == 0:
            return serialize_json((False, {"message": "Name cannot be empty."}))
        
        ok, accountFound = acc_repo.read(cursor=cursor, id=id)
        
        if ok:
            return serialize_json((False, {"message": f"Account with ID: {id} already exists."}))
        
        newAcc = Account(id, pin, name, 0)
        
        ok = acc_repo.create(cursor=cursor, acc=newAcc)
        
        if ok:
            unique_id = str(uuid.uuid4())
            logged_in_clients[unique_id] = id
            return serialize_json((True, {"uid": unique_id}))
        else:
            return serialize_json((False, {"message": "Error occurred while creating the account, please try again later."}))

    # deposit money from an  account
    @rpc(String, Float, _returns=String)                 
    def deposit(self, uid, amount):
        if logged_in_clients.get(uid) == None:
            return serialize_json((False, {"message": "Cannot find user, please login correctly."}))
        
        id = logged_in_clients.get(uid)
        return serialize_json((deposit_helper(id, amount)))

    # withdraw money from an  account  
    @rpc(String, Float, _returns=String)          
    def withdraw(self, uid,  amount):
        if logged_in_clients.get(uid) == None:
            return serialize_json((False, {"message": "Bad Request."}))
        
        id = logged_in_clients.get(uid)
        
        if amount <= 0:
            return serialize_json((False, {"message": "Amount must be greater than 0."}))
        
        ok, account = acc_repo.read(cursor, id)
        
        if not ok:
            return serialize_json((False, {"message": "Something went wrong, please try again later."}))
        
        if account["limit"] - amount < 0:
            limit = account["limit"]
            return serialize_json((False, {"message": f"Daily limit cannot be exceeded, you can only spend {limit} more for today."}))
        
        if account["balance"] - amount < 0:
            return serialize_json((False, {"message": "Balance is not enough."}))
        
        newAcc = Account(account["id"], None, account["name"], account["balance"] - amount, account["limit"] - amount)

        ok, result = acc_repo.update(cursor, newAcc)
        
        if not ok:
            return serialize_json((False, {"message": "Something went wrong, please try again later."}))
        
        statement_repository.create(cursor, Statement(id, "withdraw", f"Withdraw {amount}", None))
        return serialize_json((True, result))


    # tranfer money between accounts   
    @rpc(String, Integer, Integer, String, Float, _returns=String)
    def transfer(self, uid, id_from, id_to, name_to, amount):
        
        if logged_in_clients.get(uid) != id_from:
            return serialize_json((False, {"message": "Bad Request."}))
        
        if id_from == id_to:
            return serialize_json((False, {"message": "You cannot tranffer money to ur"}))
        
        if amount <= 0:
                    return serialize_json((False, {"message": "Amount must be greater than 0."}))
        
        ok, _ = acc_repo.read(cursor, id_to)
        
        if not ok:
            return serialize_json((False, {"message": "The account you want to transfer doesn't exist."}))
                    
        ok, acc_to_transfer = acc_repo.read(cursor, id_to)
            
        if not ok:
            return serialize_json((False, {"message": "Something went wrong, please try again later."}))
        
        if acc_to_transfer["name"].lower() != name_to.lower():
            return serialize_json((False, {"message": "Wrong account details, transfer cannot be completed."}))
        
        ok, result = withdrawHelper(uid, amount)
        
        if not ok:
            return serialize_json((False, result))
        
        ok, _ = deposit_helper(id_to, amount)
        
        if not ok:
            return serialize_json((False, result))

        statement_repository.create(cursor, Statement(id_from, "transfer", f"{amount} transferred from ID: {id_from} to ID: {id_to}", None))
        
        return serialize_json((True, result))

    # check the account balance
    @rpc(String,  _returns=String)
    def balance(self, uid):
        if logged_in_clients.get(uid) == None:
            return serialize_json((False, {"message": "Bad Request."}))
        id = logged_in_clients.get(uid)
        ok, result = acc_repo.read(cursor, id)
        if not ok:
            return serialize_json((False, {"message": "Something went wrong, please try again later."}))       
        return serialize_json((True, result))
    

    # get bank statements for an account
    @rpc(String, _returns=String)
    def get_statements(self, uid):
        if logged_in_clients.get(uid) == None:
            return serialize_json((False, {"message": "Bad Request."}))
        
        id = logged_in_clients.get(uid)
        
        ok, result = statement_repository.read_by_acc_id(cursor, id)
        if ok:
            return serialize_json((True, result))
        else:
            return serialize_json((False, {"message": "Something went wrong, please try again later."}))

    @rpc(String, _returns=String)
    def logout(self, uid):
        logged_in_clients.pop(uid, None)
        return serialize_json((True, {"message": "User logged out."}))


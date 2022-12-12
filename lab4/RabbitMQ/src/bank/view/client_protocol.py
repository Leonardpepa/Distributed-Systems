import json
def handleLogin(connection_handler):
    try:
        id = int(input("Enter your ID: "))
        password = int(input("Enter your password: "))
        
        if id <= 0 or password <= 999:
            return False, {"message": "Wrong input, please try again"} 
        
        print("Loading...", flush=True)
        
        response = connection_handler.call({"type": "auth", "id": id, "pin": password})
        ok, result = response[0], response[1]
        
        if ok:
            unique_id = response[2]
            result["uid"] = unique_id
            return True, result
        else:
            return False, result
        
    except ValueError as error:
        return False, {"message": "ID and password need to be integers, please try again."}

def handleRegister(connection_handler):
    try:
        id = int(input("Enter your ID: "))
        password = int(input("Enter your password: "))
        name = input("Enter your name: ")
        
        if id <= 0 or password <= 999:
            return False, {"message": "Wrong input, please try again"}
        
        if len(name) == 0:
            return False, {"message": "Name cannot be empty"}
        
        print("Loading...", flush=True)
        
        response = connection_handler.call({"type": "register", "id": id, "pin": password, "name": name})
        ok, result = response[0], response[1]
        
        if ok:
            unique_id = response[2]
            result["id"] = id
            result["name"] = name
            result["uid"] = unique_id
            return True, result
        else:
            return False, result
    except ValueError as error:
        return False, {"message": "ID and password need to be integers, please try again."}

def handleDeposit(connection_handler, uid):
    try:
        amount = float(input("Enter the amount your want to deposit: "))
        
        if amount <= 0:
            print("Amount must be greater than 0.")
            return
        
        response = connection_handler.call({"type": "deposit", "uid": uid, "amount": amount})
        ok, result = response[0], response[1]
        
        if ok:
            print("Deposit was successful. New Balance: ", result["balance"], flush=True)
        else:
            print(result["message"], flush=True)
    except ValueError as error:
        print("The amount needs be a number, please try again.", flush=True)

def handleWithdraw(connection_handler, uid):
    try:
        amount = float(input("Enter the amount your want to withdraw: "))
        if amount <= 0:
            print("Amount must be greater than 0.")
            return
        
        response = connection_handler.call({"type": "withdraw", "uid": uid, "amount": amount})
        ok, result = response[0], response[1]
        
        if ok:
            print("Withdraw was successful. New Balance: ", result["balance"], flush=True)
        else:
            print(result["message"], flush=True)
            
    except ValueError as error:
        print("The amount needs be a number, please try again.", flush=True)

def handleTransfer(connection_handler, uid, from_id):
    try:
        to_id = int(input("Enter the ID of the account you want to transfer: "))
        
        if to_id <= 0:
            print("ID cannot be a negative number")
            return
        
        name_to = input("Enter the name of the account holder to transfer: ")
        
        if len(name_to) == 0:
            print("Name cannot be empty")
            return
        
        amount = float(input("Enter the amount to transfer: "))
        
        if amount <= 0:
            print("Amount must be greater than 0.", flush=True)
            return
        
        response = connection_handler.call({"type": "transfer","uid": uid ,"from_id": from_id, "to_id": to_id, "name_to": name_to,"amount": amount})
        ok, result = response[0], response[1]
        
        if not ok:
            print(result["message"], flush=True)
        else:
            print(f"You transfered {amount} to {name_to} successfully.", flush=True)
        
    except ValueError as error:
        print("Wrong input!")

def handleBalance(connection_handler, uid):
    response = connection_handler.call({"type": "balance", "uid": uid})
    ok, result  = response[0], response[1]
        
    if ok:
        print("Your balance is: ", result["balance"], flush=True)
    else:
        print(result["message"], flush=True)

def handleInfo(connection_handler, uid):
    
    response = connection_handler.call({"type": "info", "uid": uid})
    ok, result  = response[0], response[1]
    
    if ok:
        print("ID:", result["id"], flush=True)
        print("Name:", result["name"], flush=True)
        print("Balance:", result["balance"], flush=True)
        print("MAX daily limit:", 900, flush=True)
        print("Daily limit left:", result["limit"], flush=True)
        print("Daily limit date: ", result["date"], flush=True)
    else:
        print(result["message"], flush=True)


def get_statements(connection_handler, uid):

    response = connection_handler.call({"type": "statements", "uid": uid})
    ok, result = response[0], response[1]
        
    if not ok:
        print(result["message"], flush=True)
        return
    if len(result) == 0:
        print("You have made 0 bank statements.", flush=True)
        return
    for i, statement in enumerate(result):
        type = statement["type"]
        message = statement["message"]
        date = statement["timestamp"]
        print(f"({i+1}) Type: {type}, {message}, Date: {date}", flush=True)

def logout(connection_handler, uid):
    response = connection_handler.call({"type": "logout", "uid": uid})
    ok, result = response[0], response[1]
    return (ok, result)
    

import json
def handleLogin(socket):
    try:
        id = int(input("Enter your ID: "))
        password = int(input("Enter your password: "))
        
        if id <= 0 or password <= 999:
            return False, {"message": "Wrong input, please try again"} 
        
        print("Loading...", flush=True)
        
        request = {"type": "auth", "id": id, "pin": password}
        
        request = str(json.dumps(request))
        
        socket.send_string(request)
        response = socket.recv_string()
        response = json.loads(response)
        
        ok, result = response[0], response[1]
        
        if ok:
            unique_id = response[2]
            result["uid"] = unique_id
            return True, result
        else:
            return False, result
        
    except ValueError as error:
        return False, {"message": "ID and password need to be integers, please try again."}

def handleRegister(socket):
    try:
        id = int(input("Enter your ID: "))
        password = int(input("Enter your password: "))
        name = input("Enter your name: ")
        
        if id <= 0 or password <= 999:
            return False, {"message": "Wrong input, please try again"}
        
        if len(name) == 0:
            return False, {"message": "Name cannot be empty"}
        
        print("Loading...", flush=True)
        request = str(json.dumps({"type": "register", "id": id, "pin": password, "name": name}))
        socket.send_string(request)
        
        response = socket.recv_string()
        response = json.loads(response)
        
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

def handleDeposit(socket, uid):
    try:
        amount = float(input("Enter the amount your want to deposit: "))
        
        if amount <= 0:
            print("Amount must be greater than 0.")
            return
        
        request = str(json.dumps({"type": "deposit", "uid": uid, "amount": amount}))
        socket.send_string(request)
        
        response = socket.recv_string()
        response = json.loads(response)
        
        ok, result = response[0], response[1]
        
        if ok:
            print("Deposit was successful. New Balance: ", result["balance"], flush=True)
        else:
            print(result["message"], flush=True)
    except ValueError as error:
        print("The amount needs be a number, please try again.", flush=True)

def handleWithdraw(socket, uid):
    try:
        amount = float(input("Enter the amount your want to withdraw: "))
        if amount <= 0:
            print("Amount must be greater than 0.")
            return
        
        request = str(json.dumps({"type": "withdraw", "uid": uid, "amount": amount}))
        socket.send_string(request)
        
        response = socket.recv_string()
        response = json.loads(response)
        
        ok, result = response[0], response[1]
        
        if ok:
            print("Withdraw was successful. New Balance: ", result["balance"], flush=True)
        else:
            print(result["message"], flush=True)
            
    except ValueError as error:
        print("The amount needs be a number, please try again.", flush=True)

def handleTransfer(socket, uid, from_id):
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
        
        request = str(json.dumps({"type": "transfer","uid": uid ,"from_id": from_id, "to_id": to_id, "name_to": name_to,"amount": amount}))
        socket.send_string(request)
        
        response = socket.recv_string()
        response = json.loads(response)
        
        ok, result = response[0], response[1]
        
        if not ok:
            print(result["message"], flush=True)
        else:
            print(f"You transfered {amount} to {name_to} successfully.", flush=True)
        
    except ValueError as error:
        print("Wrong input!")

def handleBalance(socket, uid):
    
    request = str(json.dumps({"type": "balance", "uid": uid}))
    socket.send_string(request)
    
    response = socket.recv_string()
    response = json.loads(response)
    
    ok, result  = response[0], response[1]
        
    if ok:
        print("Your balance is: ", result["balance"], flush=True)
    else:
        print(result["message"], flush=True)

def handleInfo(socket, uid):
    
    request = str(json.dumps({"type": "info", "uid": uid}))
    socket.send_string(request)
    
    response = socket.recv_string()
    response = json.loads(response)
    
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


def get_statements(socket, uid):

    request = str(json.dumps({"type": "statements", "uid": uid}))
    socket.send_string(request)
    
    response = socket.recv_string()
    response = json.loads(response)
    

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

def logout(socket, uid):
    request = str(json.dumps({"type": "logout", "uid": uid}))
    socket.send_string(request)
    
    response = socket.recv_string()
    response = json.loads(response)
    
    
    ok, result = response[0], response[1]
    return (ok, result)
    

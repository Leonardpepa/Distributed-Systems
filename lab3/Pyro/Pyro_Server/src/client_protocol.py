def handleLogin(service):
    try:
        id = int(input("Enter your id: "))
        password = int(input("Enter your password: "))
        print("Loading...", flush=True)
        
        ok, result = service.auth(id=id, pin=password)
        
        if ok:
            return True, result
        else:
            return False, result
    except ValueError as error:
        return False, {"message": "id and password need to be an integers please try again"}

def handleRegister(service):
    try:
        id = int(input("Enter your id: "))
        password = int(input("Enter your password: "))
        name = input("Enter your name: ")
        print("Loading...")
        ok, result = service.create(id=id, pin=password, name=name)
        if ok:
            result["id"] = id
            result["name"] = name
            return True, result
        else:
            return False, result
    except ValueError as error:
        return False, {"message": "id and password need to be an integers please try again"}

def handleDeposit(service, id: int):
    try:
        amount = float(input("Enter the amount your want to deposit: "))
        
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0"}
        
        ok, result = service.deposit(id, amount)
        
        if ok:
            print("Deposit was successful. New Balance: ", result["balance"])
        else:
            print(result["message"])
    except ValueError as error:
        print("The amount needs be a number please try again")

def handleWithdraw(service, id: int):
    try:
        amount = float(input("Enter the amount your want to withdraw: "))
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0"}
        
        ok, result = service.withdraw(id, amount)
        
        if ok:
            print("withdraw was successful. New Balance: ", result["balance"])
        else:
            print(result["message"])
    except ValueError as error:
        print("The amount needs be a number please try again")

def handleBalance(service, id: int):
    ok, result = service.balance(id)    
    if ok:
        print("Your balance is: ", result["balance"])
    else:
        print(result["message"])

def logout(service, id: int):
    return service.logout(id)
    

def handleInfo(service, id: int):
    ok, result = service.info(id)
    if ok:
        print("ID:", result["id"])
        print("Name:", result["name"])
        print("Balance", result["balance"])
    else:
        print(result["message"])

def handleTransfer(service, from_id: int):
    try:
        to_id = int(input("Enter the ID of the account you want to transfer: "))
        name_to = input("Enter the name of the account holder to transfer: ")
        amount = float(input("Enter the amount to transfer: "))
        
        if amount <= 0:
            print("Amount must be greater than 0")
            return
            
        ok, result = service.transfer(from_id, to_id, name_to, amount)
        if not ok:
            print(result["message"])
        else:
            print(f"You transfered {amount} to {name_to} successfully")
        
    except ValueError as error:
        print(error)

def get_statements(service, id: int):
    ok, result = service.get_statements(id)
    if not ok:
        print(result["message"])
    if len(result) == 0:
        print("There are no statements")
        
    for i, statement in enumerate(result):
        type = statement["type"]
        message = statement["message"]
        date = statement["timestamp"]
        print(f"({i+1}) Type: {type}, {message}, Date: {date}")
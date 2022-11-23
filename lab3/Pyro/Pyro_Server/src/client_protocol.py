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

def handleDeposit(service):
    try:
        amount = float(input("Enter the amount your want to deposit: "))
        
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0"}
        
        ok, result = service.deposit(amount)
        
        if ok:
            print("Deposit was successful. New Balance: ", result["balance"])
        else:
            print(result["message"])
    except ValueError as error:
        print("The amount needs be a number please try again")

def handleWithdraw(service):
    try:
        amount = float(input("Enter the amount your want to withdraw: "))
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0"}
        
        ok, result = service.withdraw(amount)
        
        if ok:
            print("withdraw was successful. New Balance: ", result["balance"])
        else:
            print(result["message"])
    except ValueError as error:
        print("The amount needs be a number please try again")

def handleBalance(service):
    ok, result = service.balance()    
    if ok:
        print("Your balance is: ", result["balance"])
    else:
        print(result["message"])

def logout(service):
    return service.logout()
    

def handleInfo(service):
    ok, result = service.info()
    if ok:
        print("ID:", result["id"])
        print("Name:", result["name"])
        print("Balance", result["balance"])
    else:
        print(result["message"])

def handleTransfer(service):
    pass

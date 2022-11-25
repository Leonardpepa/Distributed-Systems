def handleLogin(ATM_API):
    try:
        id = int(input("Enter your ID: "))
        password = int(input("Enter your password: "))
        print("Loading...", flush=True)
        
        ok, result = ATM_API.auth(id=id, pin=password)
        
        if ok:
            return True, result
        else:
            return False, result
    except ValueError as error:
        return False, {"message": "ID and password need to be integers, please try again."}

def handleRegister(ATM_API):
    try:
        id = int(input("Enter your ID: "))
        password = int(input("Enter your password: "))
        name = input("Enter your name: ")
        print("Loading...")
        ok, result = ATM_API.create(id=id, pin=password, name=name)
        if ok:
            result["id"] = id
            result["name"] = name
            return True, result
        else:
            return False, result
    except ValueError as error:
        return False, {"message": "ID and password need to be integers, please try again."}

def handleDeposit(ATM_API, id: int):
    try:
        amount = float(input("Enter the amount your want to deposit: "))
        
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0."}
        
        ok, result = ATM_API.deposit(id, amount)
        
        if ok:
            print("Deposit was successful. New Balance: ", result["balance"])
        else:
            print(result["message"])
    except ValueError as error:
        print("The amount needs be a number, please try again.")

def handleWithdraw(ATM_API, id: int):
    try:
        amount = float(input("Enter the amount your want to withdraw: "))
        if amount <= 0:
            return False, {"message": "Amount must be greater than 0."}
        
        ok, result = ATM_API.withdraw(id, amount)
        
        if ok:
            print("Withdraw was successful. New Balance: ", result["balance"])
        else:
            print(result["message"])
    except ValueError as error:
        print("The amount needs be a number, please try again.")

def handleTransfer(ATM_API, from_id: int):
    try:
        to_id = int(input("Enter the ID of the account you want to transfer: "))
        name_to = input("Enter the name of the account holder to transfer: ")
        amount = float(input("Enter the amount to transfer: "))
        
        if amount <= 0:
            print("Amount must be greater than 0.")
            return
            
        ok, result = ATM_API.transfer(from_id, to_id, name_to, amount)
        if not ok:
            print(result["message"])
        else:
            print(f"You transfered {amount} to {name_to} successfully.")
        
    except ValueError as error:
        print(error)

def handleBalance(ATM_API, id: int):
    ok, result = ATM_API.balance(id)    
    if ok:
        print("Your balance is: ", result["balance"])
    else:
        print(result["message"])

def handleInfo(ATM_API, id: int):
    ok, result = ATM_API.info(id)
    if ok:
        print("ID:", result["id"])
        print("Name:", result["name"])
        print("Balance:", result["balance"])
        print("MAX daily limit:", 900)
        print("Daily limit left:", result["limit"])
        print("Daily limit date: ", result["date"])
    else:
        print(result["message"])


def get_statements(ATM_API, id: int):
    ok, result = ATM_API.get_statements(id)
    if not ok:
        print(result["message"])
        return
    if len(result) == 0:
        print("You have made 0 bank statements.")
        return
    for i, statement in enumerate(result):
        type = statement["type"]
        message = statement["message"]
        date = statement["timestamp"]
        print(f"({i+1}) Type: {type}, {message}, Date: {date}")

def logout(ATM_API, id: int):
    return ATM_API.logout(id)
    

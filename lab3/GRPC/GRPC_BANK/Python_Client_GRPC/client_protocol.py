
import sys
sys.path.insert(1, 'generated/')
import grpc
from generated import bank_pb2, bank_pb2_grpc

def handleLogin(ATM_API: bank_pb2_grpc.BankStub):
    try:
        id = int(input("Enter your ID: "))
        password = int(input("Enter your password: "))
        
        if id <= 0 or password <= 999:
            return False, {"message": "Wrong input, please try again"} 
        
        print("Loading...", flush=True)
        
        response: bank_pb2.LoginResponse = ATM_API.login(bank_pb2.AuthRequest(id=id, pin=password))
        if response.ok.ok:
            return True, {"id": response.id, "name": response.name}
        else:
            return False, {"message": response.ok.message} 
        
    except ValueError as error:
        return False, {"message": "ID and password need to be integers, please try again."}

def handleRegister(ATM_API: bank_pb2_grpc.BankStub):
    try:
        id = int(input("Enter your ID: "))
        password = int(input("Enter your password: "))
        name = input("Enter your name: ")
        
        if id <= 0 or password <= 999:
            return False, {"message": "Wrong input, please try again"}
        
        if len(name) == 0:
            return False, {"message": "Name cannot be empty"}
        
        print("Loading...", flush=True)
        response: bank_pb2.RegisterResponse = ATM_API.register(bank_pb2.RegisterRequest(id=id, pin=password, name=name))
        
        if response.ok.ok:
            return True, {"id": response.id, "name": response.name}
        else:
            return False, {"message": response.ok.message} 
        
    except ValueError as error:
        return False, {"message": "ID and password need to be integers, please try again."}

def handleDeposit(ATM_API:bank_pb2_grpc.BankStub, id):
    try:
        amount = float(input("Enter the amount your want to deposit: "))
        
        if amount <= 0:
            print("Amount must be greater than 0.")
            return
        
        response :bank_pb2.DepositResponse = ATM_API.deposit(bank_pb2.DepositRequest(id=id, amount=amount))
        
        if response.ok.ok:
            print("Deposit was successful. New Balance: ", response.balance, flush=True)
        else:
            print(response.ok.message, flush=True)
            
    except ValueError as error:
        print("The amount needs be a number, please try again.", flush=True)

def handleWithdraw(ATM_API:bank_pb2_grpc.BankStub, id):
    try:
        amount = float(input("Enter the amount your want to withdraw: "))
        
        if amount <= 0:
            print("Amount must be greater than 0.")
            return
        
        response: bank_pb2.WithdrawResponse = ATM_API.withdraw(bank_pb2.WithdrawRequest(id=id, amount=amount))
        
        if response.ok.ok:
            print("Withdraw was successful. New Balance: ", response.balance, flush=True)
        else:
            print(response.ok.message, flush=True)
    except ValueError as error:
        print("The amount needs be a number, please try again.", flush=True)

def handleTransfer(ATM_API: bank_pb2_grpc.BankStub, from_id):
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
            
        response: bank_pb2.TransferResponse = ATM_API.transfer(bank_pb2.TransferRequest(id_from=from_id, id_to=to_id, amount=amount, name_to=name_to))
        if response.ok.ok:
            print(f"You transfered {response.amount} to {name_to} successfully.", flush=True)
        else:
            print(response.ok.message, flush=True)
        
    except ValueError as error:
        print("Wrong input!")

def handleBalance(ATM_API: bank_pb2_grpc.BankStub, id):
    response: bank_pb2.BalanceResponse = ATM_API.balance(bank_pb2.GenericRequest(id=id))    
    if response.ok.ok:
        print("Your balance is: ", response.balance, flush=True)
    else:
        print(response.ok.message, flush=True)

def handleInfo(ATM_API: bank_pb2_grpc.BankStub, id):
    response: bank_pb2.InfoResponse = ATM_API.info(bank_pb2.GenericRequest(id=id))
    
    if response.ok.ok:
        print("ID:", response.account.id, flush=True)
        print("Name:", response.account.name, flush=True)
        print("Balance:", response.account.balance, flush=True)
        print("MAX daily limit:", 900, flush=True)
        print("Daily limit left:", response.account.limit, flush=True)
        print("Daily limit date: ", response.account.date, flush=True)
    else:
        print(response.ok.message, flush=True)


def get_statements(ATM_API: bank_pb2_grpc.BankStub, id):
    response: bank_pb2.StatementResponse = ATM_API.statements(bank_pb2.GenericRequest(id=id))
    if not response.ok.ok:
        print(response.ok.message, flush=True)
        return
    
    if len(response.statement) == 0:
        print("You have made 0 bank statements.", flush=True)
        return
    for i, stmt in enumerate(response.statement):
        type = stmt.type
        message = stmt.message
        date = stmt.date
        print(f"({i+1}) Type: {type}, {message}, Date: {date}", flush=True)

def logout(ATM_API: bank_pb2_grpc.BankStub, id):
    response: bank_pb2.Ok = ATM_API.logout(bank_pb2.Empty())
    return True, {"message": response.message}    

import sys
sys.path.insert(1, 'generated/')
import grpc
from generated import bank_pb2, bank_pb2_grpc

import client_protocol

def client_loop(ATM_API):
    authenticated = False
    id = -1
    name = ""
    
    print("\nWelcome to Pamak Bank Pyro Version!", flush=True)
    while not authenticated:
        print("-------------------", flush=True)
        print("(1) Login", flush=True)
        print("(2) Register", flush=True)
        print("(0) Exit", flush=True)
        print("-------------------", flush=True)
        
        user_input = input("Enter Action: ")
        
        # case enter:
        if len(user_input) == 0:
            continue
        
        # hold only the first character of users input
        user_input = user_input[0]
        
        # exit the programm
        if user_input == "0":
            print("Goodbye!", flush=True)
            break
        
        # Login routine
        if user_input == "1":
            print("----LOGIN----", flush=True)
            ok, result = client_protocol.handleLogin(ATM_API)
            if ok:
                id = result["id"]
                name = result["name"]
                authenticated = True
            else:
                print(result["message"], flush=True)
                
        # Register routine
        if user_input == "2":
            print("----Register----", flush=True)
            ok, result = client_protocol.handleRegister(ATM_API)
            if ok:
                id = result["id"]
                name = result["name"]
                authenticated = True
            else:
                print(result["message"], flush=True)
            
            
    if authenticated:
        print("\n----MAIN PAGE----", flush=True)
        print(f"Hello {name}!", flush=True)
        
    while authenticated:
        print("-------------------", flush=True)
        print("(1) Account info", flush=True)
        print("(2) Statement info", flush=True)
        print("(3) Deposit", flush=True)
        print("(4) Withdraw", flush=True)
        print("(5) Balance", flush=True)
        print("(6) Transfer", flush=True)
        print("(7) Logout", flush=True)
        print("(0) Exit Application", flush=True)
        print("-------------------", flush=True)
        
        user_input = input("Enter Action: ")
        
        # case enter:
        if len(user_input) == 0:
            continue
        
        # hold only the first character of users input
        user_input = user_input[0]
        
        if user_input == "1":
            print("----ACCOUNT INFO----", flush=True)
            client_protocol.handleInfo(ATM_API, id)
            
        if user_input == "2":
            print("----STATEMENT INFO----", flush=True)
            client_protocol.get_statements(ATM_API, id)
        
        if user_input == "0":
            authenticated = False
            id = -1
            name = ""
            print("goodbye!", flush=True)
            continue
        
        # Deposit routine
        if user_input == "3":
            print("----DEPOSIT----", flush=True)
            client_protocol.handleDeposit(ATM_API, id)
            
        # Withdraw routine
        if user_input == "4":
            print("----WITHDRAW----", flush=True)
            client_protocol.handleWithdraw(ATM_API, id)
        
        # Balance routine
        if user_input == "5":
            print("----BALANCE----", flush=True)
            client_protocol.handleBalance(ATM_API, id)  
            
        if user_input == "6":
            print("----TRANFER----", flush=True)
            client_protocol.handleTransfer(ATM_API, id)
            
        if user_input == "7":
            print("----LOGOUT----", flush=True)
            ok, result = client_protocol.logout(ATM_API, id)
            if ok:
                authenticated = False
                id = -1
                name = ""
                
            print(result["message"]) 
            return client_loop(ATM_API=ATM_API)



def main():
    channel = grpc.insecure_channel('localhost:5050')
    ATM_API = bank_pb2_grpc.BankStub(channel)
    client_loop(ATM_API=ATM_API)             
            
if __name__ == "__main__":
    main()
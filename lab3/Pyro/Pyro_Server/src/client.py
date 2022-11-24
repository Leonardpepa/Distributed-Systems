import Pyro4
import client_protocol

def client_loop(atm_service):
    authenticated = False
    id = -1
    name = ""
    
    print("\n Welcome to Pamak Bank Pyro Version")
    while not authenticated:
        print("-------------------")
        print("(1) Login")
        print("(2) Register")
        print("(0) Exit")
        print("-------------------")
        
        user_input = input()
        
        # case enter:
        if len(user_input) == 0:
            continue
        
        # hold only the first character of users input
        user_input = user_input[0]
        
        # exit the programm
        if user_input == "0":
            print("Goodbye!")
            break
        
        # Login routine
        if user_input == "1":
            print("----LOGIN----")
            ok, result = client_protocol.handleLogin(atm_service)
            if ok:
                id = result["id"]
                name = result["name"]
                authenticated = True
            else:
                print(result["message"])
                
        # Register routine
        if user_input == "2":
            print("----Register----")
            ok, result = client_protocol.handleRegister(atm_service)
            if ok:
                id = result["id"]
                name = result["name"]
                authenticated = True
            else:
                print(result["message"])
            
            
    if authenticated:
        print("----MAIN PAGE----")
        print(f"Hello {name}!")
        
    while authenticated:
        print("-------------------")
        print("(1) Account info")
        print("(2) Statement info")
        print("(3) Deposit")
        print("(4) Withdraw")
        print("(5) Balance")
        print("(6) Transfer")
        print("(7) Logout")
        print("(0) Exit Application")
        print("-------------------")
        
        user_input = input()
        
        # case enter:
        if len(user_input) == 0:
            continue
        
        # hold only the first character of users input
        user_input = user_input[0]
        
        if user_input == "1":
            print("----ACCOUNT INFO----")
            client_protocol.handleInfo(atm_service, id)
            # print("--------------------")
            
        if user_input == "2":
            print("----STATEMENT INFO----")
            client_protocol.get_statements(atm_service, id)
            # print("--------------------")
            
        
        if user_input == "0":
            authenticated = False
            id = -1
            name = ""
            print("goodbye!")
            continue
        # Deposit routine
        if user_input == "3":
            print("----DEPOSIT----")
            client_protocol.handleDeposit(atm_service, id)
            # print("---------------")
            
        # Withdraw routine
        if user_input == "4":
            print("----WITHDRAW----")
            client_protocol.handleWithdraw(atm_service, id)
            # print("----------------")
        
        # Balance routine
        if user_input == "5":
            print("----BALANCE----")
            client_protocol.handleBalance(atm_service, id)  
            # print("---------------")
        
        if user_input == "6":
            print("----TRANFER----")
            client_protocol.handleTransfer(atm_service, id)
            # print("---------------")
            
        if user_input == "7":
            print("----LOGOUT----")
            # ok, result = client_protocol.logout(atm_service, id)
            # if ok:
            #     authenticated = False
            #     id = -1
            #     name = ""
            # print(result["message"]) 
            # client_loop(atm_service=atm_service)
            # # print("---------------")



def main():
    atm_service = Pyro4.Proxy("PYRONAME:ATM_Service")
    client_loop(atm_service=atm_service)            
            
            
if __name__ == "__main__":
    main()
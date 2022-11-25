import Pyro4
import client_protocol

def client_loop(ATM_API):
    authenticated = False
    id = -1
    name = ""
    
    print("\nWelcome to Pamak Bank Pyro Version!")
    while not authenticated:
        print("-------------------")
        print("(1) Login")
        print("(2) Register")
        print("(0) Exit")
        print("-------------------")
        
        user_input = input("Enter Action: ")
        
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
            ok, result = client_protocol.handleLogin(ATM_API)
            if ok:
                id = result["id"]
                name = result["name"]
                authenticated = True
            else:
                print(result["message"])
                
        # Register routine
        if user_input == "2":
            print("----Register----")
            ok, result = client_protocol.handleRegister(ATM_API)
            if ok:
                id = result["id"]
                name = result["name"]
                authenticated = True
            else:
                print(result["message"])
            
            
    if authenticated:
        print("\n----MAIN PAGE----")
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
        
        user_input = input("Enter Action: ")
        
        # case enter:
        if len(user_input) == 0:
            continue
        
        # hold only the first character of users input
        user_input = user_input[0]
        
        if user_input == "1":
            print("----ACCOUNT INFO----")
            client_protocol.handleInfo(ATM_API, id)
            # print("--------------------")
            
        if user_input == "2":
            print("----STATEMENT INFO----")
            client_protocol.get_statements(ATM_API, id)
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
            client_protocol.handleDeposit(ATM_API, id)
            # print("---------------")
            
        # Withdraw routine
        if user_input == "4":
            print("----WITHDRAW----")
            client_protocol.handleWithdraw(ATM_API, id)
            # print("----------------")
        
        # Balance routine
        if user_input == "5":
            print("----BALANCE----")
            client_protocol.handleBalance(ATM_API, id)  
            # print("---------------")
        
        if user_input == "6":
            print("----TRANFER----")
            client_protocol.handleTransfer(ATM_API, id)
            # print("---------------")
            
        if user_input == "7":
            print("----LOGOUT----")
            ok, result = client_protocol.logout(ATM_API, id)
            if ok:
                authenticated = False
                id = -1
                name = ""
            print(result["message"]) 
            return client_loop(ATM_API=ATM_API)



def main():
    ATM_API = Pyro4.Proxy("PYRONAME:ATM_API")
    client_loop(ATM_API=ATM_API)            
            
            
if __name__ == "__main__":
    main()
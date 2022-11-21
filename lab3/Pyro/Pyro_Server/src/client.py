import Pyro4
        
def main():
    atm_service = Pyro4.Proxy("PYRONAME:ATM_Service")
    
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
            try:
                id = int(input("Enter your id: "))
                password = int(input("Enter your password: "))
                print("Loading...")
                result = atm_service.auth(id=id, pin=password)
                if result[0]:
                    authenticated = True
                    id = result[1]
                    name = result[2]
                    break
                else:
                    print(result[1])
            except ValueError as error:
                print("id and password need to be an integers please try again")
                continue
        # Register routine
        if user_input == "2":
            print("----Register----")
            try:
                id = int(input("Enter your id: "))
                password = int(input("Enter your password: "))
                name = input("Enter your name: ")
                print("Loading...")
                result = atm_service.create(id=id, pin=password, name=name)
                if result[0]:
                    authenticated = True
                    id = result[1]
                    name = result[2]
                    break
                else:
                    print(result[1])
            except ValueError as error:
                print("id and password need to be an integers please try again")
                continue
            
    if authenticated:
        print("----MAIN PAGE----")
        print(f"Hello {name}!")
        
    while authenticated:
        print("-------------------")
        print("(1) Deposit")
        print("(2) Withdraw")
        print("(3) Balance")
        print("(0) Exit")
        print("-------------------")
        
        user_input = input()[0]
        
        if user_input == "0":
            authenticated = False
            id = -1
            name = ""
            print("goodbye!")
            continue
        # Deposit routine
        if user_input == "1":
            print("----DEPOSIT----")
            try:
                amount = float(input("Enter the amount your want to deposit: "))
                result = atm_service.deposit(amount)
                if result[0] == True:
                    print("Deposit was successful. New Balance: ", result[1])
                else:
                    print(result[1])
            except ValueError as error:
                print("The amount needs be a number please try again")
        # Withdraw routine
        if user_input == "2":
            print("----WITHDRAW----")
            try:
                amount = float(input("Enter the amount your want to withdraw: "))
                result = atm_service.withdraw(amount)
                if result[0]:
                    print("withdraw was successful. New Balance: ", result[1])
                else:
                    print(result[1])
            except ValueError as error:
                print("The amount needs be a number please try again")
        # Balance routine
        if user_input == "3":
            print("----BALANCE----")
            result = atm_service.balance()    
            if result[0]:
                print("Your balance is: ", result[1])
            else:
                print(result[1])
        
            
if __name__ == "__main__":
    main()
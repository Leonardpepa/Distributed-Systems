import sys
sys.path.insert(1, 'generated/')
import grpc
import generated.calculator_pb2 as calculator_pb2
import generated.calculator_pb2_grpc as calculator_pb2_grpc

def read_numbers():
    try:
        a = float(input("Enter the first nunber: ")) 
        b = float(input("Enter the second nunber: "))
        return a, b    
    except ValueError as error:
        print("Please enter valid numbers.", flush=True)
    


def main():
    channel = grpc.insecure_channel('server:5050')
    calculator = calculator_pb2_grpc.CalculatorStub(channel)
    
    print("Grpc Powered Simple Calculator", flush=True)
    print("Welcome!", flush=True)
    
    while True:
        print("\nSimple Calculator", flush=True)
        print("---------------------", flush=True)
        print("(1) ADD", flush=True)
        print("(2) SUB", flush=True)    
        print("(3) MUL", flush=True)    
        print("(4) DIV", flush=True)    
        print("(0) EXIT", flush=True)
        
        answer = input("Enter Action: ")
        
        if len(answer) == 0:
            continue
        
        answer = answer[0]
        op = ""
        a = 0
        b = 0
        
        result = 0
        if answer == '1':
            a, b = read_numbers()
            op = "+"
            response: calculator_pb2.BinaryOpResponse = calculator.calculate(calculator_pb2.BinaryOpRequest(operator=calculator_pb2.ADD, operandA=a, operandB=b))
            result = response.result
            
        if answer == '2':
            a, b = read_numbers()
            op = "-"
            response: calculator_pb2.BinaryOpResponse = calculator.calculate(calculator_pb2.BinaryOpRequest(operator=calculator_pb2.SUB, operandA=a, operandB=b))
            result = response.result
            
        if answer == '3':
            a, b = read_numbers()
            op = "*"
            response: calculator_pb2.BinaryOpResponse = calculator.calculate(calculator_pb2.BinaryOpRequest(operator=calculator_pb2.MUL, operandA=a, operandB=b))
            result = response.result
            
        if answer == '4':
            a, b = read_numbers()
            op = ""
            response: calculator_pb2.BinaryOpResponse = calculator.calculate(calculator_pb2.BinaryOpRequest(operator=calculator_pb2.DIV, operandA=a, operandB=b))
            result = response.result
            
        if answer == '0':
            print("Goodbye!", flush=True)
            break
        
        print(f"{a} {op} {b} = {result}")
            
if __name__ == '__main__':
    main()
    
    
    
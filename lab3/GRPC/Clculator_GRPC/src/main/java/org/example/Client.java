package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.Calculator.Operator;
import org.example.grpc.CalculatorClient;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5050)
                .usePlaintext()
                .build();

        CalculatorClient client = new CalculatorClient(channel);
        System.out.println("Welcome to grpc powered calculator!");
        boolean run = true;
        while (run){
            System.out.println("Simple Calculator");
            System.out.println("----------------");
            System.out.println("(1) ADD");
            System.out.println("(2) SUB");
            System.out.println("(3) MUL");
            System.out.println("(4) DIV");
            System.out.println("(0) EXIT");
            System.out.println("----------------");

            try{
                System.out.println("Enter action: ");
                int answer = scan.nextInt();
                float[] numbers;
                float result;

                switch (answer){
                    case 1:
                        numbers = readNumbers(scan);
                        result = client.calculate(Operator.ADD, numbers[0], numbers[1]);
                        System.out.print("Result: ");
                        System.out.println(result);
                        break;
                    case 2:
                        numbers = readNumbers(scan);
                        result = client.calculate(Operator.SUB, numbers[0], numbers[1]);
                        System.out.print("Result: ");
                        System.out.println(result);
                        break;
                    case 3:
                        numbers = readNumbers(scan);
                        result = client.calculate(Operator.MUL, numbers[0], numbers[1]);
                        System.out.print("Result: ");
                        System.out.println(result);
                        break;
                    case 4:
                        numbers = readNumbers(scan);
                        result = client.calculate(Operator.DIV, numbers[0], numbers[1]);
                        System.out.print("Result: ");
                        System.out.println(result);
                        break;
                    case 0:
                        run = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.err.println("Wrong input!!");
                }

            }catch (NumberFormatException e){
                System.err.println("Wrong input!!");
            }catch (RuntimeException ex){
                System.err.println(ex.getMessage());
            }


        }
        channel.shutdown();
        scan.close();
    }

    public static float[] readNumbers(Scanner scan){
        System.out.println("Enter the first number: ");
        float a = scan.nextFloat();
        System.out.println("Enter the first number: ");
        float b = scan.nextFloat();
        return new float[]{a, b};
    }
}

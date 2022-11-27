package org.bank;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.bank.grpc.Bank.*;

import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5050)
                .usePlaintext()
                .build();
        BankGrpc.BankBlockingStub stud = BankGrpc.newBlockingStub(channel);

        Scanner scanner = new Scanner(System.in);
        int id = -1;
        while (true) {
            System.out.println("1 R");
            System.out.println("2 L");
            System.out.println("3 I");
            System.out.println("Action: ");
            int a = scanner.nextInt();

            if (a == 1){

                System.out.println("id: ");
                int user_id = scanner.nextInt();
                System.out.println("password: ");
                int pin = scanner.nextInt();
                System.out.println("name: ");
                String name = "hello";
                RegisterResponse response = stud.register(RegisterRequest.newBuilder().setId(user_id).setName(name).setPin(pin).build());
                if (response.getOk().getOk()){
                    id = response.getId();
                    System.out.println("YEs");
                }else{
                    System.out.println(response.getOk().getMessage());
                }

            }else if (a == 2){
                System.out.println("id: ");
                int user_id = scanner.nextInt();
                System.out.println("password: ");
                int pin = scanner.nextInt();
                LoginResponse response = stud.login(AuthRequest.newBuilder().setId(user_id).setPin(pin).build());
                if (response.getOk().getOk()){
                    id = response.getId();
                    System.out.println("YEs");
                }else{
                    System.out.println(response.getOk().getMessage());
                }

            }else if (a == 3){
                InfoResponse response = stud.info(GenericRequest.newBuilder().setId(id).build());
                System.out.println(response);
            }else if (a == 4){
                BalanceResponse response = stud.balance(GenericRequest.newBuilder().setId(id).build());
                System.out.println(response.getBalance());
            }else {
                break;
            }
        }

        channel.shutdown();
    }


}

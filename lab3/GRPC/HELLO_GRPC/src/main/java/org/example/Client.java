package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.GreeterClient;

public class Client {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5050)
                .usePlaintext()
                .build();

        GreeterClient client = new GreeterClient(channel);
        System.out.println(client.SayHello("Leonard pepa"));
        System.out.println(client.SayHelloAgain("Leonard pepa"));
        channel.shutdown();
    }


}

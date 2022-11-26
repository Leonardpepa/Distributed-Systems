package org.example;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.ServerBuilder;
import org.example.grpc.CalculatorServiceImpl;

import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        ServerBuilder<?> serverBuilder = Grpc.newServerBuilderForPort(5050,  InsecureServerCredentials.create());
        io.grpc.Server server = serverBuilder.addService(new CalculatorServiceImpl()).build();
        try {
            System.out.println("Calculator Server started on port 5050");
            server.start();
            server.awaitTermination();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
package org.example.grpc;

import io.grpc.Channel;
import org.example.grpc.Greeting.GreeterGrpc;
import org.example.grpc.Greeting.GreetingRequest;
import org.example.grpc.Greeting.GreetingResponse;

public class GreeterClient {
    private GreeterGrpc.GreeterBlockingStub stub;


    public GreeterClient(Channel channel) {
        stub = GreeterGrpc.newBlockingStub(channel);
    }

    public String SayHello(String name){
        GreetingRequest request = GreetingRequest.newBuilder().setName(name).build();
        GreetingResponse response = stub.sayHello(request);
        return response.getMessage();
    }

    public String SayHelloAgain(String name){
        GreetingRequest request = GreetingRequest.newBuilder().setName(name).build();
        GreetingResponse response = stub.sayHelloAgain(request);
        return response.getMessage();
    }

}

package org.example.grpc;

import io.grpc.stub.StreamObserver;
import org.example.grpc.Greeting.GreeterGrpc;
import org.example.grpc.Greeting.GreetingRequest;
import org.example.grpc.Greeting.GreetingResponse;

public class GreeterServiceImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        GreetingResponse response = GreetingResponse.newBuilder().setMessage("Hello, " + request.getName()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sayHelloAgain(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        GreetingResponse response = GreetingResponse.newBuilder().setMessage("Hello again, " + request.getName()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

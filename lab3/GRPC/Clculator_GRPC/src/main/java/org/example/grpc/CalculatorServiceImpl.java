package org.example.grpc;

import io.grpc.stub.StreamObserver;
import org.example.grpc.Calculator.BinaryOpRequest;
import org.example.grpc.Calculator.BinaryOpResponse;
import org.example.grpc.Calculator.CalculatorGrpc;

public class CalculatorServiceImpl extends CalculatorGrpc.CalculatorImplBase {
    @Override
    public void calculate(BinaryOpRequest request, StreamObserver<BinaryOpResponse> responseObserver) {
        BinaryOpResponse response;
        float result = 0;
        switch (request.getOperator()) {
            case ADD:
                result = request.getOperandA() + request.getOperandB();
                break;
            case SUB:
                result = request.getOperandA() - request.getOperandB();
                break;
            case MUL:
                result = request.getOperandA() * request.getOperandB();
                break;
            case DIV:
                if (request.getOperandB() == 0) throw new RuntimeException("Error | Division with 0");
                result = request.getOperandA() / request.getOperandB();
                break;
            default:
                throw new RuntimeException("Unknown operator");
        }
        response = BinaryOpResponse.newBuilder().setResult(result).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

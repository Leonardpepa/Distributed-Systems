package org.example.grpc;

import io.grpc.Channel;
import org.example.grpc.Calculator.BinaryOpRequest;
import org.example.grpc.Calculator.BinaryOpResponse;
import org.example.grpc.Calculator.CalculatorGrpc;
import org.example.grpc.Calculator.Operator;

public class CalculatorClient {
    private CalculatorGrpc.CalculatorBlockingStub stub;

    public CalculatorClient(Channel channel){
        stub = CalculatorGrpc.newBlockingStub(channel);
    }

    public float calculate(Operator operator, float operandA, float operandB){
        BinaryOpRequest request = BinaryOpRequest.newBuilder()
                                            .setOperator(operator)
                                            .setOperandA(operandA)
                                            .setOperandB(operandB).build();
        BinaryOpResponse response = stub.calculate(request);
        return response.getResult();
    }

}

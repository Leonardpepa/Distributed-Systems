package org.bank.grpc;

import io.grpc.stub.StreamObserver;
import org.bank.Model.AccountRepository;
import org.bank.Model.DatabaseConnector;
import org.bank.grpc.Bank.*;

public class BankServiceImpl extends BankGrpc.BankImplBase {

    private int id = -1;

    private AccountRepository repo = new AccountRepository(new DatabaseConnector().getConnection());

    @Override
    public void info(GenericRequest request, StreamObserver<InfoResponse> responseObserver) {
        InfoResponse response;
        if (this.id != request.getId()){
            response = InfoResponse.newBuilder()
                            .setOk(Ok.newBuilder().setOk(false).setMessage("Cannot find user, please login correctly.").build())
                            .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Account account = repo.read(request.getId());

        if (account == null){
            response = InfoResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Cannot find user, please login correctly.").build())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        response = InfoResponse.newBuilder()
                        .setOk(Ok.newBuilder().setOk(true).build())
                        .setAccount(account)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void statements(GenericRequest request, StreamObserver<StatementResponse> responseObserver) {

    }

    @Override
    public void balance(GenericRequest request, StreamObserver<BalanceResponse> responseObserver) {
        BalanceResponse response;
        if (this.id != request.getId()){
            response = BalanceResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Bad Request.").build())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Account account = repo.read(request.getId());

        if (account == null){
            response = BalanceResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Something went wrong try again, please try again later.").build())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        response = BalanceResponse.newBuilder()
                .setOk(Ok.newBuilder().setOk(true).build())
                .setBalance(account.getBalance())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void login(AuthRequest request, StreamObserver<LoginResponse> responseObserver) {
        LoginResponse response;
        Account account = repo.auth(request.getId(), request.getPin());

        if (account == null){
            response = LoginResponse.newBuilder()
                                    .setOk(Ok.newBuilder().setOk(false).setMessage("Authentication failed, wrong credentials.")
                                    .build()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        this.id = account.getId();
        response = LoginResponse.newBuilder()
                                .setOk(Ok.newBuilder().setOk(true))
                                .setId(account.getId())
                                .setName(account.getName())
                                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
        RegisterResponse response;
        if (request.getName().isBlank() || request.getName().isEmpty()){
            response = RegisterResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Name cannot be empty.").build())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Account accountFound = repo.read(request.getId());

        if (accountFound != null){
            response = RegisterResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Account with ID: "+ request.getId() +" already exists.").build())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Account newAccount = Account.newBuilder().setId(request.getId())
                .setPin(request.getPin())
                .setName(request.getName())
                .setLimit(0)
                .setDate("")
                .build();

        if (repo.create(newAccount) == null){
            response = RegisterResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Error occurred while creating the account, please try again later.").build())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        this.id = newAccount.getId();
        response = RegisterResponse.newBuilder()
                .setOk(Ok.newBuilder().setOk(true).build())
                .setId(newAccount.getId())
                .setName(newAccount.getName())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deposit(DepositRequest request, StreamObserver<DepositResponse> responseObserver) {
//        super.deposit(request, responseObserver);
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<WithdrawResponse> responseObserver) {
//        super.withdraw(request, responseObserver);
    }

    @Override
    public void transfer(TransferRequest request, StreamObserver<TransferResponse> responseObserver) {
//        super.transfer(request, responseObserver);
    }
}

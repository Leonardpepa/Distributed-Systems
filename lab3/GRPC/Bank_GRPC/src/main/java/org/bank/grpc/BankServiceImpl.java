package org.bank.grpc;

import io.grpc.stub.StreamObserver;
import org.bank.Model.AccountRepository;
import org.bank.Model.DatabaseConnector;
import org.bank.Model.StatementRepository;
import org.bank.Server;
import org.bank.grpc.Bank.*;

import javax.swing.text.DateFormatter;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BankServiceImpl extends BankGrpc.BankImplBase {

    private int id = -1;
    private Connection connection = new DatabaseConnector().getConnection();
    private AccountRepository repo = new AccountRepository(connection);
    private StatementRepository stmtRepo = new StatementRepository(connection);

    private void updateDailyLimit(int id, String dateInDB){
        LocalDate nowDate = LocalDate.now();
        LocalDate limitDate = LocalDate.parse(dateInDB);

        Period period = Period.between(nowDate, limitDate);
        int diff = Math.abs(period.getDays());

        if (diff >= 1){
            repo.updateDailyLimit(id, 900);
        }
    }
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
        Server.lockRead(request.getId());
        try {
            Account account = repo.read(request.getId());

            if (account == null){
                response = InfoResponse.newBuilder()
                        .setOk(Ok.newBuilder().setOk(false).setMessage("Cannot find user, please login correctly.").build())
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            updateDailyLimit(request.getId(), account.getDate());

            response = InfoResponse.newBuilder()
                            .setOk(Ok.newBuilder().setOk(true).build())
                            .setAccount(account)
                            .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }finally {
            Server.unlockRead(request.getId());
        }
    }

    @Override
    public void statements(GenericRequest request, StreamObserver<StatementResponse> responseObserver) {
        StatementResponse response;

        if (request.getId() != this.id){
            response = StatementResponse.newBuilder().setOk(Ok.newBuilder().setOk(false).setMessage("Bad Request."))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Server.lockRead(request.getId());
        try{
            List<Statement> statements = stmtRepo.readByAccId(request.getId());
            if (statements == null){
                response = StatementResponse.newBuilder().setOk(Ok.newBuilder().setOk(false).setMessage("Something went wrong, please try again later."))
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            StatementResponse.Builder builder = StatementResponse.newBuilder();
            builder.addAllStatement(statements);
            response = builder.setOk(Ok.newBuilder().setOk(true).build()).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }finally {
            Server.unlockRead(request.getId());
        }
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
        Server.lockRead(request.getId());
        try{
            Account account = repo.read(request.getId());
            if (account == null){
                response = BalanceResponse.newBuilder()
                        .setOk(Ok.newBuilder().setOk(false).setMessage("Something went wrong try again, please try again later.").build())
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
            updateDailyLimit(account.getId(), account.getDate());
            response = BalanceResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(true).build())
                    .setBalance(account.getBalance())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }finally {
            Server.unlockRead(request.getId());
        }
    }

    @Override
    public void login(AuthRequest request, StreamObserver<LoginResponse> responseObserver) {
        LoginResponse response;

        if (request.getId() <= 0){
            response = LoginResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("ID must be greater than zero.")
                            .build()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        if (request.getPin() <= 999){
            response = LoginResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Password should be greater than 3 digits.").build())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

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
        updateDailyLimit(account.getId(), account.getDate());
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

        if (request.getId() <= 0){
            response = RegisterResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("ID must be greater than zero..")
                            .build()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        if (request.getPin() <= 999){
            response = RegisterResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Password should be greater than 3 digits.").build())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

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
        Server.putLock(request.getId());
        Server.lockWrite(request.getId());
        try {
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
        }finally {
            Server.unlockWrite(request.getId());
        }
    }

    @Override
    public void deposit(DepositRequest request, StreamObserver<DepositResponse> responseObserver) {
        Server.lockWrite(request.getId());
        try{
            DepositResponse response = depositHelper(request, false);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }finally {
            Server.unlockWrite(request.getId());
        }
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<WithdrawResponse> responseObserver) {
        Server.lockWrite(request.getId());
        try{
            WithdrawResponse response = withdrawHelper(request);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }finally {
            Server.unlockWrite(request.getId());
        }
}

    @Override
    public void transfer(TransferRequest request, StreamObserver<TransferResponse> responseObserver) {
        TransferResponse response;
        if (this.id != request.getIdFrom()){
            response = TransferResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Bad Request.").build())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        if (request.getIdFrom() == request.getIdTo()){
            response = TransferResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("You cannot transfer money to urself.").build())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        if (request.getAmount() <= 0){
            response = TransferResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Amount must be greater than 0.").build())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }


        Account accToTransfer = repo.read(request.getIdTo());

        if (accToTransfer == null){
            response = TransferResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("The account you want to transfer doesn't exist.").build())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        Server.twoFaceLock(request.getIdFrom(), request.getIdTo());
        try{
            accToTransfer = repo.read(request.getIdTo());

            if (!request.getNameTo().equalsIgnoreCase(accToTransfer.getName())){
                response = TransferResponse.newBuilder()
                        .setOk(Ok.newBuilder().setOk(false).setMessage("Wrong account details, transfer cannot be completed.").build())
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

           WithdrawResponse withdrawResponse = this.withdrawHelper(WithdrawRequest.newBuilder().setId(request.getIdFrom()).setAmount(request.getAmount())
                    .build());

            if (withdrawResponse.getOk().getOk() == false){
                response = TransferResponse.newBuilder().setOk(withdrawResponse.getOk()).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            DepositResponse depositResponse = depositHelper(DepositRequest.newBuilder().setId(request.getIdTo()).setAmount(request.getAmount())
                    .build(), true);

            if (depositResponse.getOk().getOk() == false){
                response = TransferResponse.newBuilder().setOk(depositResponse.getOk()).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            stmtRepo.create(Statement.newBuilder()
                    .setAccountId(request.getIdFrom())
                    .setType("transfer")
                    .setMessage(request.getAmount() + " transferred from ID: " + request.getIdFrom() + " to ID: "+ request.getIdTo())
                    .setDate("")
                    .build());
            response = TransferResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(true).build())
                    .setAmount(request.getAmount())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }finally {
            Server.twoFaceUnlock(request.getIdFrom(), request.getIdTo());
        }

    }
    @Override
    public void logout(Empty request, StreamObserver<Ok> responseObserver) {
        Ok response = Ok.newBuilder().setOk(true).setMessage("User logged out.").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    private DepositResponse depositHelper(DepositRequest request, boolean isTransfer){
        DepositResponse response;

        if (!isTransfer){
            if (this.id != request.getId()){
                response = DepositResponse.newBuilder()
                        .setOk(Ok.newBuilder().setOk(false).setMessage("Bad Request.").build())
                        .build();
                return response;
            }

        }

        if (request.getId() <= 0){
            response = DepositResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Bad request, please login correctly.").build())
                    .build();
            return response;
        }

        if (request.getAmount() <= 0){
            response = DepositResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Amount must be greater than 0.").build())
                    .build();
            return response;
        }

        Account account = repo.read(request.getId());

        if (account == null){
            response = DepositResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Something went wrong, please try again later.").build())
                    .build();
            return response;
        }

        Account updated = Account.newBuilder()
                .setBalance(account.getBalance() + request.getAmount())
                .setId(account.getId())
                .setName(account.getName())
                .setLimit(account.getLimit())
                .build();

        if (repo.update(updated) == null){
            response = DepositResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Something went wrong, please try again later.").build())
                    .build();
            return response;
        }

        stmtRepo.create(Statement.newBuilder()
                .setAccountId(account.getId())
                .setType("deposit")
                .setMessage("Deposited " + request.getAmount())
                .setDate("")
                .build());

        response = DepositResponse.newBuilder()
                .setOk(Ok.newBuilder().setOk(true).build())
                .setBalance(updated.getBalance())
                .build();

        return response;
    }


    private WithdrawResponse withdrawHelper(WithdrawRequest request){
        WithdrawResponse response;
        if (this.id != request.getId()){
            response = WithdrawResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Bad Request.").build())
                    .build();
            return response;
        }

        if (request.getAmount() <= 0){
            response = WithdrawResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Amount must be greater than 0.").build())
                    .build();
            return response;
        }

        Account account = repo.read(request.getId());

        if (account == null){
            response = WithdrawResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Something went wrong, please try again later.").build())
                    .build();
            return response;
        }

        if (account.getLimit() - request.getAmount() < 0){
            response = WithdrawResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Daily limit cannot be exceeded, you can only spend " + account.getLimit() + " more for today.").build())
                    .build();
            return response;
        }

        if (account.getBalance() - request.getAmount() < 0){
            response = WithdrawResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Balance is not enough.").build())
                    .build();
            return response;

        }

        Account updated = Account.newBuilder()
                .setBalance(account.getBalance() - request.getAmount())
                .setLimit(account.getLimit() - request.getAmount())
                .setId(account.getId())
                .setName(account.getName())
                .build();

        if (repo.update(updated) == null){
            response = WithdrawResponse.newBuilder()
                    .setOk(Ok.newBuilder().setOk(false).setMessage("Something went wrong, please try again later.").build())
                    .build();
            return response;
        }

        stmtRepo.create(Statement.newBuilder()
                .setAccountId(account.getId())
                .setType("withdraw")
                .setMessage("Withdraw " + request.getAmount())
                .setDate("")
                .build());

        response = WithdrawResponse.newBuilder()
                .setOk(Ok.newBuilder().setOk(true).build())
                .setBalance(updated.getBalance())
                .build();

        return response;
    }


}

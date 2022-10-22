package Controller;

import Model.Account;
import Model.AccountRepository;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServerProtocol {
    private final Request request;
    private final AccountRepository service;

    private final Controller controller;

    public ServerProtocol(Request request, Controller controller) {
        this.request = request;
        this.controller = controller;
        this.service = controller.getRepository();
    }

    public Response proccessRequest() {
        switch (request.getType()) {
            case auth:
                return proccessAuth();
            case register:
                return createAcc();
            case deposit:
                return proccessDeposit();
            case withdraw:
                return proccessWithdraw();
            case checkBalance:
                return proccessCheckBalance();
            case logout:
                return logout();
            default:
                throw new IllegalStateException("Unexpected value: " + request.getType());
        }
    }

    private Response logout() {
        Response response = Response.createGeneralSuccessResponse();
        System.out.println("Client with id " + controller.getClient_id() + " logged out");
        controller.locks.remove(controller.getClient_id());
        controller.setClient_id(-1);
        return response;
    }

    private Response proccessAuth() {
        Account acc = service.auth(request.getId(), request.getPin());
        if (acc == null) {
            return Response.createGeneralErrorResponse("Authentication failed");
        }
        controller.setClient_id(acc.getId());
        controller.locks.putIfAbsent(acc.getId(), new ReentrantReadWriteLock());
        System.out.println("Client with id " + controller.getClient_id() + " Logged in");
        return Response.createAuthSuccessResponse(acc.getId(), acc.getName());
    }

    private Response createAcc() {
        System.out.println("Id " + request.getId() + "Create account Method");
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        controller.locks.putIfAbsent(request.getId(), lock);
        controller.lockWrite(request.getId());
        try{
            Account account = new Account(request.getId(), request.getPin(), request.getName(), 0);
            if (service.create(account) == null) {
                return Response.createGeneralErrorResponse("Account creation failed");
            }
            return Response.createGeneralSuccessResponse();
        }finally {
            controller.unlockWrite(request.getId());
        }
    }

    private Response proccessDeposit() {
        System.out.println("Id " + request.getId() + " Deposit");
        controller.lockWrite(request.getId());
        try {
            double amountToDeposit = request.getAmount();
            if (amountToDeposit == 0) {
                return Response.createGeneralErrorResponse("The amount cannot be 0");
            }
            Account account = service.read(request.getId());
            account.deposit(amountToDeposit);
            account = service.update(account);

            if (account == null) {
                return Response.createGeneralErrorResponse("Deposit failed");
            }
            return Response.createGeneralSuccessResponse();
        }finally {
            controller.unlockWrite(request.getId());
        }
    }

    private Response proccessWithdraw() {
        System.out.println("Id " + request.getId() + " Withdraw");
        controller.lockWrite(request.getId());
        try{
            double amountToWithdraw = request.getAmount();
            if (amountToWithdraw == 0) {
                return Response.createGeneralErrorResponse("The amount cannot be 0");
            }

            Account account = service.read(request.getId());

            if (account.getBalance() - amountToWithdraw < 0) {
                return Response.createGeneralErrorResponse("Balance is not enough Withdrawal failed");
            }
            account.withdraw(amountToWithdraw);
            service.update(account);
            return Response.createGeneralSuccessResponse();
        }finally {
            controller.unlockWrite(request.getId());
        }
    }

    private Response proccessCheckBalance() {
        System.out.println("Id " + request.getId() + " Check Balance");
        controller.lockRead(request.getId());
        try{
            Account account = service.read(request.getId());
            if (account == null) {
                return Response.createGeneralErrorResponse("Server error");
            }
            return Response.createCheckBalanceResponse(account.getBalance());
        }finally {
            controller.unlockRead(request.getId());
        }
    }
}

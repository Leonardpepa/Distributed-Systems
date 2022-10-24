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
                return processDeposit();
            case withdraw:
                return processWithdraw();
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
        System.out.println("- Id " + request.getId() + " Create account -");

        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        controller.locks.putIfAbsent(request.getId(), lock);
        controller.lockWrite(request.getId());

        try {
            Account foundAccount = service.read(request.getId());
            if (foundAccount != null) {
                return Response.createGeneralErrorResponse("Account already exists");
            }
            Account account = new Account(request.getId(), request.getPin(), request.getName(), 0);
            if (service.create(account) == null) {
                return Response.createGeneralErrorResponse("Account creation failed please try again");
            }
            return Response.createGeneralSuccessResponse();
        } finally {
            controller.unlockWrite(request.getId());
        }
    }

    private Response processDeposit() {
        System.out.println("- Id " + request.getId() + " Deposit -");
        controller.lockWrite(request.getId());

        try {
            double amountToDeposit = request.getAmount();

            if (amountToDeposit == 0) {
                return Response.createGeneralErrorResponse("The amount cannot be 0");
            }
            Account accountFound = service.read(request.getId());

            if (accountFound == null) {
                return Response.createGeneralErrorResponse("There was an error with the request");
            }

            accountFound.deposit(amountToDeposit);

            if (service.update(accountFound) == null) {
                return Response.createGeneralErrorResponse("Deposit failed");
            }
            return Response.createGeneralSuccessResponse();
        } finally {
            controller.unlockWrite(request.getId());
        }
    }

    private Response processWithdraw() {
        System.out.println("- Id " + request.getId() + " Withdraw -");
        controller.lockWrite(request.getId());

        try {
            double amountToWithdraw = request.getAmount();
            if (amountToWithdraw == 0) {
                return Response.createGeneralErrorResponse("The amount cannot be 0");
            }

            Account accountFound = service.read(request.getId());
            if (accountFound == null) {
                return Response.createGeneralErrorResponse("There was an error with the request");
            }

            if (accountFound.getBalance() - amountToWithdraw < 0) {
                return Response.createGeneralErrorResponse("Balance is not enough Withdrawal failed");
            }

            accountFound.withdraw(amountToWithdraw);
            if (service.update(accountFound) == null) {
                return Response.createGeneralErrorResponse("Withdrawal failed");
            }

            return Response.createGeneralSuccessResponse();
        } finally {
            controller.unlockWrite(request.getId());
        }
    }

    private Response proccessCheckBalance() {
        System.out.println("- Id " + request.getId() + " Check Balance -");
        controller.lockRead(request.getId());

        try {
            Account account = service.read(request.getId());
            if (account == null) {
                return Response.createGeneralErrorResponse("Server error");
            }
            return Response.createCheckBalanceResponse(account.getBalance());
        } finally {
            controller.unlockRead(request.getId());
        }
    }
}

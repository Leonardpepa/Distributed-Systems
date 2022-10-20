package Controller;

import Model.Account;
import Model.AccountRepository;

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
        controller.setClient_id(-1);
        return response;
    }

    private Response proccessAuth() {
        Account acc = service.auth(request.getId(), request.getPin());
        if (acc == null) {
            return Response.createGeneralErrorResponse("Authentication failed");
        }
        controller.setClient_id(acc.getId());
        return Response.createAuthSuccessResponse(acc.getId(), acc.getName());
    }

    private Response createAcc() {
        Account account = new Account(request.getId(), request.getPin(), request.getName(), 0);
        if (service.create(account) == null) {
            return Response.createGeneralErrorResponse("Account creation failed");
        }
        return Response.createGeneralSuccessResponse();
    }

    private Response proccessDeposit() {
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
    }

    private Response proccessWithdraw() {
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
    }

    private Response proccessCheckBalance() {
        Account account = service.read(request.getId());
        if (account == null) {
            return Response.createGeneralErrorResponse("Server error");
        }
        return Response.createCheckBalanceResponse(account.getBalance());
    }
}

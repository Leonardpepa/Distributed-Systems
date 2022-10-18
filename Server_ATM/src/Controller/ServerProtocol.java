package Controller;

import Model.Account;
import Model.AccountRepository;

public class ServerProtocol {
    private final Request request;
    private final AccountRepository service;

    public ServerProtocol(Request request, AccountRepository service) {
        this.request = request;
        this.service = service;
    }

    public Response proccessRequest() {
        switch (request.getType()) {
            case Auth:
                return proccessAuth();
            case deposit:
                return proccessDeposit();
            case withdraw:
                return proccessWithdraw();
            case checkBalance:
                return proccessCheckBalance();
            default:
                throw new IllegalStateException("Unexpected value: " + request.getType());
        }
    }

    private Response proccessAuth() {
        boolean authSuccess = service.auth(request.getId(), request.getPin());
        Response response = new Response("Auth", !authSuccess, authSuccess);
        return response;
    }

    private Response proccessDeposit() {
        double amountToDeposit = request.getBalance();
        Account account = service.read(request.getId());
        account.setBalance(account.getBalance() + amountToDeposit);
        account = service.update(account);

        if (account != null) {
            return new Response("Deposit Succeed", false, true);
        }
        return new Response("Deposit Failed", true, false);
    }

    private Response proccessWithdraw() {
        double amountToWithdraw = request.getBalance();
        Account account = service.read(request.getId());
        if (account.getBalance() - amountToWithdraw < 0) {
            return new Response("Withdraw Failed balance is not enough ", true, false);
        }
        account.setBalance(account.getBalance() - amountToWithdraw);
        service.update(account);

        return new Response("Withdraw Succeed", false, true);
    }

    private Response proccessCheckBalance() {
        Account account = service.read(request.getId());
        if (account == null) {
            return new Response("Something went wrong", true, false);

        }
        return new Response("Check balance", false, true);
    }
}

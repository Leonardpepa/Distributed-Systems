package Controller;

import Model.AccountRepository;

public class ServerProtocol {
    private final Request request;
    private final AccountRepository service;

    public ServerProtocol(Request request, AccountRepository service) {
        this.request = request;
        this.service = service;
    }

    public Response proccessRequest() {
        return null;
    }

    private void proccessAuth() {

    }

    private void proccessDeposit() {

    }

    private void proccessWithdraw() {

    }

    private void proccessCheckBalance() {

    }
}

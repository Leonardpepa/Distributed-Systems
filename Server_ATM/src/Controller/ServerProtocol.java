package Controller;

import Model.AccountRepository;

import java.sql.Connection;

public class ServerProtocol {
    private Request request;
    private AccountRepository service;

    public ServerProtocol(Request request, AccountRepository service){
        this.request = request;
        this.service = service;
    }

    public void proccessRequest(){

    }
    private void proccessAuth(){

    }

    private void proccessDeposit(){

    }

    private void proccessWithdraw(){

    }

    private void proccessCheckBalance(){

    }
}

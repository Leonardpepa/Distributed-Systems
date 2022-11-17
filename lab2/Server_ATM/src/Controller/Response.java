package Controller;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Response extends UnicastRemoteObject implements Serializable {
    private int id;
    private String name;
    private double balance;

    private String message;
    private boolean ok;

    private Response() throws RemoteException {
        super();
    }

    public static Response createCheckBalanceResponse(double balance) throws RemoteException {
        Response checkResponse = new Response();
        checkResponse.setBalance(balance);
        checkResponse.setOk(true);
        return checkResponse;
    }

    public static Response createAuthSuccessResponse(int id, String name) throws RemoteException {
        Response authSuccessResponse = new Response();
        authSuccessResponse.setId(id);
        authSuccessResponse.setName(name);
        authSuccessResponse.setOk(true);
        return authSuccessResponse;
    }

    public static Response createGeneralSuccessResponse() throws RemoteException {
        Response ok = new Response();
        ok.setOk(true);
        return ok;
    }

    public static Response createGeneralErrorResponse(String message) throws RemoteException {
        Response error = new Response();
        error.setOk(false);
        error.setMessage(message);
        return error;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}




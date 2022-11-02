package Controller;

import java.io.Serializable;

public class Response implements Serializable {
    private int id;
    private String name;
    private double balance;

    private String message;
    private boolean ok;

    private Response() {

    }

    public static Response createCheckBalanceResponse(double balance) {
        Response checkResponse = new Response();
        checkResponse.setBalance(balance);
        checkResponse.setOk(true);
        return checkResponse;
    }

    public static Response createAuthSuccessResponse(int id, String name) {
        Response authSuccessResponse = new Response();
        authSuccessResponse.setId(id);
        authSuccessResponse.setName(name);
        authSuccessResponse.setOk(true);
        return authSuccessResponse;
    }

    public static Response createGeneralSuccessResponse() {
        Response ok = new Response();
        ok.setOk(true);
        return ok;
    }

    public static Response createGeneralErrorResponse(String message) {
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




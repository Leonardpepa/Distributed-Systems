package Controller;

import java.io.Serializable;

public class Response implements Serializable {

    private double balance;
    private String message;
    private boolean error;
    private boolean ok;

    public Response(double balance, String message, boolean error, boolean ok) {
        this.balance = balance;
        this.message = message;
        this.error = error;
        this.ok = ok;
    }

    public Response(String message, boolean error, boolean ok) {
        this.message = message;
        this.error = error;
        this.ok = ok;
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

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}

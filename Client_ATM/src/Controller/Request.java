package Controller;

import java.io.Serializable;

public class Request implements Serializable {
    private RequestType type;
    private int id;
    private int pin;
    private double balance;

    // general
    public Request(RequestType type, int id, int pin, double balance) {
        this.type = type;
        this.id = id;
        this.pin = pin;
        this.balance = balance;
    }

    //auth
    public Request(RequestType type, int id, int pin) {
        this.type = type;
        this.id = id;
        this.pin = pin;
    }

    // deposit, withdraw, check
    public Request(RequestType type, double balance) {
        this.type = type;
        this.balance = balance;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

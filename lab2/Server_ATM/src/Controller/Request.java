package Controller;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Request extends UnicastRemoteObject implements Serializable {
    private RequestType type;
    private int id;
    private int pin;
    private double amount;
    private String name;

    private Request() throws RemoteException {
        super();
    }

    public static Request createLogoutRequest(int id) throws RemoteException {
        Request request = new Request();
        request.setType(RequestType.logout);
        request.setId(id);
        return request;
    }

    public static Request createAuthRequest(int id, int pin) throws RemoteException {
        Request authRequest = new Request();
        authRequest.setType(RequestType.auth);
        authRequest.setId(id);
        authRequest.setPin(pin);
        return authRequest;
    }

    public static Request createRegisterRequest(int id, int pin, String name, double balance) throws RemoteException {
        Request registerRequest = new Request();
        registerRequest.setType(RequestType.register);
        registerRequest.setId(id);
        registerRequest.setPin(pin);
        registerRequest.setName(name);
        registerRequest.setAmount(balance);
        return registerRequest;
    }

    public static Request createCheckBalanceRequest(int id) throws RemoteException {
        Request checkRequest = new Request();
        checkRequest.setType(RequestType.checkBalance);
        checkRequest.setId(id);
        return checkRequest;
    }

    public static Request createDepositRequest(int id, double balance) throws RemoteException {
        Request depositRequest = new Request();
        depositRequest.setType(RequestType.deposit);
        depositRequest.setAmount(balance);
        depositRequest.setId(id);
        return depositRequest;
    }

    public static Request createWithdrawRequest(int id, double balance) throws RemoteException {
        Request withdrawRequest = new Request();
        withdrawRequest.setType(RequestType.withdraw);
        withdrawRequest.setAmount(balance);
        withdrawRequest.setId(id);
        return withdrawRequest;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

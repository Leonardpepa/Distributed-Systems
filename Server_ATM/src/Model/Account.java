package Model;

import java.io.Serializable;

public class Account implements Serializable {
    private int id;
    private int pin;
    private String name;
    private double balance;

    public Account() {
    }

    public Account(int id, int pin, String name, double balance) {
        this.name = name;
        this.id = id;
        this.pin = pin;
        this.balance = balance;
    }

    public Account(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", pin=" + pin + ", name='" + name + '\'' + ", balance=" + balance + '}';
    }

    public void deposit(double amount) {
        this.setBalance(this.balance + amount);
    }

    public void withdraw(double amount) {
        this.setBalance(this.balance - amount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

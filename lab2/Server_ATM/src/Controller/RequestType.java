package Controller;

import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;

public enum RequestType implements Serializable {
    auth, deposit, withdraw, checkBalance, register, logout
}

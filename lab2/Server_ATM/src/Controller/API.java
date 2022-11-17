package Controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface API extends Remote {
    Response authenticate(Request authRequest) throws RemoteException;

    Response register(Request registerRequest) throws RemoteException;

    Response deposit(Request depositRequest) throws RemoteException;

    Response withdraw(Request withdrawRequest) throws RemoteException;

    Response balance(Request balanceRequest) throws RemoteException;

    Response logout(Request logoutRequest) throws RemoteException;
}

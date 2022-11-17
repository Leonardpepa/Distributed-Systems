package Controller;

import Model.Account;
import Model.AccountRepository;
import Model.DatabaseConnector;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class ServerProtocolImpl extends UnicastRemoteObject implements IServerProtocol {
    private AccountRepository service;
    public ServerProtocolImpl() throws RemoteException {
        super();
        DatabaseConnector connector = new DatabaseConnector("bank");
        try {
            this.service = new AccountRepository(connector.getDbConnection(), "account");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response authenticate(Request authRequest) throws RemoteException {
        Account acc = service.auth(authRequest.getId(), authRequest.getPin());

        if (acc == null) {
            return Response.createGeneralErrorResponse("Authentication failed");
        }

        return Response.createAuthSuccessResponse(acc.getId(), acc.getName());
    }

    @Override
    public Response register(Request registerRequest) throws RemoteException {
        Account foundAccount = service.read(registerRequest.getId());

        if (foundAccount != null) {
            return Response.createGeneralErrorResponse("Account already exists");
        }

        Account account = new Account(registerRequest.getId(), registerRequest.getPin(), registerRequest.getName(), 0);

        if (service.create(account) == null) {
            return Response.createGeneralErrorResponse("Account creation failed please try again");
        }
        return Response.createGeneralSuccessResponse();

    }

    @Override
    public Response deposit(Request depositRequest) throws RemoteException {
        double amountToDeposit = depositRequest.getAmount();

        if (amountToDeposit == 0) {
            return Response.createGeneralErrorResponse("The amount cannot be 0");
        }
        Account accountFound = service.read(depositRequest.getId());

        if (accountFound == null) {
            return Response.createGeneralErrorResponse("There was an error with the request");
        }

        accountFound.deposit(amountToDeposit);

        if (service.update(accountFound) == null) {
            return Response.createGeneralErrorResponse("Deposit failed");
        }
        return Response.createGeneralSuccessResponse();

    }

    @Override
    public Response withdraw(Request withdrawRequest) throws RemoteException {
        double amountToWithdraw = withdrawRequest.getAmount();
        if (amountToWithdraw == 0) {
            return Response.createGeneralErrorResponse("The amount cannot be 0");
        }

        Account accountFound = service.read(withdrawRequest.getId());
        if (accountFound == null) {
            return Response.createGeneralErrorResponse("There was an error with the request");
        }

        if (accountFound.getBalance() - amountToWithdraw < 0) {
            return Response.createGeneralErrorResponse("Balance is not enough Withdrawal failed");
        }

        accountFound.withdraw(amountToWithdraw);
        if (service.update(accountFound) == null) {
            return Response.createGeneralErrorResponse("Withdrawal failed");
        }

        return Response.createGeneralSuccessResponse();

    }

    @Override
    public Response balance(Request balanceRequest) throws RemoteException {
        Account account = service.read(balanceRequest.getId());
        if (account == null) {
            return Response.createGeneralErrorResponse("Server error");
        }
        return Response.createCheckBalanceResponse(account.getBalance());
    }

    @Override
    public Response logout(Request logoutRequest) throws RemoteException {
        Response response = Response.createGeneralSuccessResponse();
        return response;
    }
}

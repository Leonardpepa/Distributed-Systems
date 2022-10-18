package Controller;

import java.io.Serializable;

public enum RequestType implements Serializable {
    Auth,
    deposit,
    withdraw,
    checkBalance,
    createAccount
}

package Controller;

import java.io.Serializable;

public enum RequestType implements Serializable {
    auth,
    deposit,
    withdraw,
    checkBalance,
    register,
    logout
}

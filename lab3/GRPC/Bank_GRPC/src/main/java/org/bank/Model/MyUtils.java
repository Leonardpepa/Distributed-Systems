package org.bank.Model;

import org.bank.grpc.Bank.Account;
import org.bank.grpc.Bank.Statement;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyUtils {
    public static Account deserializeAcc(ResultSet res) {
        try {
            int id = res.getInt("id");
            String name = res.getString("name");
            double balance = res.getDouble("balance");
            double limit = res.getDouble("limit");
            Date date = res.getDate("date");
            return Account.newBuilder().setId(id).setName(name).setBalance(balance).setLimit(limit).setDate(date.toString()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Statement deserializeStmt(ResultSet res) {
        try {
            int id = res.getInt("id");
            int accId = res.getInt("account_id");
            String type = res.getString("type");
            String message = res.getString("message");
            Date date = res.getDate("timestamp");
            return Statement.newBuilder().setAccountId(accId).setType(type).setMessage(message).setDate(date.toString()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

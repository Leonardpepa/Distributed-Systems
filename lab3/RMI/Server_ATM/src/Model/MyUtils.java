package Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyUtils {

    public static Account decerializeAcc(ResultSet res) throws SQLException {
        int id = res.getInt(1);
        int pin = res.getInt(2);
        String name = res.getString("name");
        double balance = res.getDouble(4);
        return new Account(id, pin, name, balance);
    }



}

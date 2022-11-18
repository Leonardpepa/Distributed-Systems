import View.Login;

import javax.swing.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Login login = new Login();
                } catch (RemoteException e) {
                    JOptionPane.showMessageDialog(null, "Server is not responding");
                    System.exit(1);
                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
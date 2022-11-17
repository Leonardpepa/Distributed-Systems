package View;

import Controller.API;
import Controller.Request;
import Controller.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;

public class HomeWindow extends JFrame {


    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private final int id;
    private final String name;
    private JPanel panel;
    private JButton deposit;
    private JButton withdraw;
    private JButton exit;
    private JButton balance;
    private JPanel container;
    private JPanel header;

    private JLabel welcome;
    private API api;
    public HomeWindow(API api, int id, String name, JFrame parentFrame) {
        this.id = id;
        this.name = name;
        this.api = api;
        setUpGUI(parentFrame);

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // create the logout request so the server know to delete your id
                Request request = null;
                try {
                    request = Request.createLogoutRequest(id);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                try {

                    Response response = api.logout(request);
                    if (response.isOk()) {
                        dispose();
                        parentFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(HomeWindow.this, "An error occurred please try again");
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(HomeWindow.this, "Something went wrong please reopen the app and try again");
                }
            }
        });

        deposit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String answer = JOptionPane.showInputDialog("Enter the amount to deposit");

                if (answer == null) {
                    return;
                }
                if (answer.isEmpty() || answer.isBlank()) {
                    JOptionPane.showMessageDialog(HomeWindow.this, "The amount to deposit cannot be empty");
                    return;
                }
                try {
                    double amount = Double.parseDouble(answer);
                    if (amount <= 0){
                        JOptionPane.showMessageDialog(HomeWindow.this, "The amount to deposit cannot be negative");
                        return;
                    }

                    Request request = Request.createDepositRequest(id, amount);

                    Response response = api.deposit(request);

                    if (response.isOk()) {
                        JOptionPane.showMessageDialog(HomeWindow.this, "You deposit " + amount + " successful");
                    } else {
                        JOptionPane.showMessageDialog(HomeWindow.this, response.getMessage());
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(HomeWindow.this, "The input must be a number");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(HomeWindow.this, "Something went wrong please reopen the app and try again");
                }
            }
        });

        withdraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String answer = JOptionPane.showInputDialog("Enter the amount to withdraw");
                if (answer == null) {
                    return;
                }
                if (answer.isEmpty() || answer.isBlank()) {
                    JOptionPane.showMessageDialog(HomeWindow.this, "The amount to withdraw cannot be empty");
                    return;
                }

                try {
                    double amount = Double.parseDouble(answer);

                    if (amount < 0){
                        JOptionPane.showMessageDialog(HomeWindow.this, "The amount to withdraw cannot be negative");
                        return;
                    }
                    Request request = Request.createWithdrawRequest(id, amount);
                    Response response = api.withdraw(request);

                    if (response.isOk()) {
                        JOptionPane.showMessageDialog(HomeWindow.this, "Withdrawal " + amount + " successful");
                    } else {
                        JOptionPane.showMessageDialog(HomeWindow.this, response.getMessage());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(HomeWindow.this, "The input must be a number");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(HomeWindow.this, "Something went wrong please reopen the app and try again");
                }
            }
        });


        balance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Request request = null;
                try {
                    request = Request.createCheckBalanceRequest(id);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    Response response = api.balance(request);
                    if (response.isOk()) {
                        JOptionPane.showMessageDialog(HomeWindow.this, "Your balance is: " + response.getBalance());
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(HomeWindow.this, "Something went wrong please reopen the app and try again");
                }
            }
        });


    }

    public void setUpGUI(JFrame parentFrame) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        container = new JPanel();
        container.setLayout(new BorderLayout());
        header = new JPanel();
        welcome = new JLabel("Welcome " + this.name);
        welcome.setSize(100, 50);
        welcome.setFont(new Font("Arial", Font.CENTER_BASELINE, 20));
        header.add(welcome);
        deposit = new JButton("Deposit");
        withdraw = new JButton("Withdraw");
        exit = new JButton("Exit");
        balance = new JButton("Balance");

        panel.add(deposit);
        panel.add(withdraw);
        panel.add(balance);
        panel.add(exit);

        container.add(header, BorderLayout.NORTH);
        container.add(panel, BorderLayout.CENTER);

        this.setContentPane(container);
        this.setTitle("Pamak Bank Home");
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(parentFrame);

    }
}

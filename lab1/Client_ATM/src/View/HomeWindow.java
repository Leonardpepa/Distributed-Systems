package View;

import Controller.Request;
import Controller.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HomeWindow extends JFrame {


    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private final Socket clientSocket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private final int id;
    private final String name;
    private JPanel panel;
    private JButton deposit;
    private JButton withdraw;
    private JButton exit;
    private JButton balance;
    private JPanel container;
    private JPanel header;

    private JLabel welocme;

    public HomeWindow(Socket clientSocket, ObjectInputStream input, ObjectOutputStream output, int id, String name, JFrame parentFrame) {
        this.clientSocket = clientSocket;
        this.input = input;
        this.output = output;
        this.id = id;
        this.name = name;

        setUpGUI(parentFrame);

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // create the logout request so the server know to delete your id
                Request request = Request.createLogoutReuest();
                try {
                    output.writeObject(request);
                    Response response = (Response) input.readObject();
                    if (response.isOk()) {
                        dispose();
                        parentFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(HomeWindow.this, "An error occurred please try again");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
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
                }
                try {
                    double amount = Double.parseDouble(answer);
                    Request request = Request.createDepositRequest(amount);
                    output.writeObject(request);
                    Response response = (Response) input.readObject();

                    if (response.isOk()) {
                        JOptionPane.showMessageDialog(HomeWindow.this, "You deposit " + amount + " successful");
                    } else {
                        JOptionPane.showMessageDialog(HomeWindow.this, response.getMessage());
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(HomeWindow.this, "The input must be a number");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
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
                }

                try {
                    double amount = Double.parseDouble(answer);
                    Request request = Request.createWithdrawRequest(amount);
                    output.writeObject(request);
                    Response response = (Response) input.readObject();
                    if (response.isOk()) {
                        JOptionPane.showMessageDialog(HomeWindow.this, "Withdrawal " + amount + " successful");
                    } else {
                        JOptionPane.showMessageDialog(HomeWindow.this, response.getMessage());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(HomeWindow.this, "The input must be a number");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        balance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Request request = Request.createCheckBalanceRequest();
                try {
                    output.writeObject(request);
                    Response response = (Response) input.readObject();
                    if (response.isOk()) {
                        JOptionPane.showMessageDialog(HomeWindow.this, "Your balance is: " + response.getBalance());
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
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
        welocme = new JLabel("Welcome " + this.name);
        welocme.setSize(100, 50);
        welocme.setFont(new Font("Arial", Font.CENTER_BASELINE, 20));
        header.add(welocme);
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

package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.Socket;

public class HomeWindow extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGT = 400;

    private JPanel panel;
    private JButton deposit;
    private JButton withdraw;
    private JButton exit;
    private JButton balance;
    private final Socket clientSocket;
    private final DataInputStream input;
    private final DataOutput output;

    public HomeWindow(Socket clientSocket, DataInputStream input, DataOutputStream output) {
        this.clientSocket = clientSocket;
        this.input = input;
        this.output = output;

        setUpGUI();

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login(clientSocket, input, output);
            }
        });

    }

    public void setUpGUI() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        deposit = new JButton("Deposit");
        withdraw = new JButton("Withdraw");
        exit = new JButton("Exit");
        balance = new JButton("Balance");

        panel.add(deposit);
        panel.add(withdraw);
        panel.add(balance);
        panel.add(exit);

        this.setContentPane(panel);
        this.setSize(WIDTH, HEIGT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }


}

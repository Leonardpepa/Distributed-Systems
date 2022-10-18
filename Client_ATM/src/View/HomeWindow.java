package View;

import Controller.Request;
import Controller.RequestType;
import Controller.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HomeWindow extends JFrame{


    private static final int WIDTH = 400;
    private static final int HEIGT = 400;

    private JPanel panel;
    private JButton deposit;
    private JButton withdraw;
    private JButton exit;
    private JButton balance;
    private final Socket clientSocket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private int id;
    private String name;

    private JPanel container;
    private JPanel header;

    private JLabel welocme;
    public HomeWindow(Socket clientSocket, ObjectInputStream input, ObjectOutputStream output, int id, String name) {
        this.clientSocket = clientSocket;
        this.input = input;
        this.output = output;
        this.id = id;
        this.name = name;
        setUpGUI();

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        deposit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String answer = JOptionPane.showInputDialog("Enter the amount to deposit");
                if(answer == null){
                    return;
                }
                if(answer.isEmpty() || answer.isBlank()){
                    JOptionPane.showMessageDialog(null, "Wrong input");
                }

                try{
                    double amount = Double.parseDouble(answer);
                    Request request = new Request(RequestType.deposit, id, amount);

                    output.writeObject(request);
                    Response response = (Response) input.readObject();
                    if(response.isOk()){
                        JOptionPane.showMessageDialog(null, "Deposit was successful");
                    }else{
                        JOptionPane.showMessageDialog(null, "There was an error wit your deposit");
                    }
                }catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Wrong input");
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
                if(answer == null){
                    return;
                }
                if(answer.isEmpty() || answer.isBlank()){
                    JOptionPane.showMessageDialog(null, "Wrong input");
                }

                try{
                    double amount = Double.parseDouble(answer);
                    System.out.println(amount);
                    Request request = new Request(RequestType.withdraw, id, amount);

                    output.writeObject(request);
                    Response response = (Response) input.readObject();
                    if(response.isOk()){
                        JOptionPane.showMessageDialog(null, "Withdraw was successful");
                    }else{
                        JOptionPane.showMessageDialog(null, "There was an error wit your withdraw");
                    }
                }catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Wrong input");
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
                Request request = new Request(RequestType.checkBalance, id);
                try {
                    output.writeObject(request);
                    Response response = (Response) input.readObject();
                    JOptionPane.showMessageDialog(null, response.getMessage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


    }

    public void setUpGUI() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        container = new JPanel();
        container.setLayout(new BorderLayout());
        header = new JPanel();
        welocme = new JLabel("Welcome " + this.name);
        welocme.setSize(100, 50);
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
        this.setSize(WIDTH, HEIGT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }
}

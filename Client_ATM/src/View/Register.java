package View;

import Controller.Request;
import Controller.RequestType;
import Controller.Response;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Register extends JFrame{

    private static final int WIDTH = 400;
    private static final int HEIGT = 400;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private JPanel panel;
    private JButton register;
    private JLabel title;

    private JTextField id_field;

    private JTextField pin_field;

    private JTextField name_field;
    private JButton back;
    public Register(Socket socket,ObjectInputStream input, ObjectOutputStream output) {

        clientSocket = socket;
        ObjectOutputStream clientOutputStream = output;
        ObjectInputStream clientInputStream = input;
        this.input = clientInputStream;
        this.output = clientOutputStream;

        setUpGUI();

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_string = id_field.getText();
                String pin_string = pin_field.getText();
                String name = name_field.getText();

                if(id_string.isEmpty() || pin_string.isEmpty() || id_string.isBlank() || pin_string.isBlank() || name.isBlank() || name.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                    return;
                }

                try{
                    int id = Integer.parseInt(id_string);
                    int pin = Integer.parseInt(pin_string);

                }catch (NumberFormatException ex){

                }
            }
        });

    }

    public void setUpGUI() {
        panel = new JPanel();
        panel.setLayout(null);
        register = new JButton("Register");
        title = new JLabel("Welcome to Pamak Bank");
        back = new JButton("back");

        id_field = new JTextField("Enter your id");
        pin_field = new JTextField("Enter your pin");
        name_field = new JTextField("Enter your name");

        id_field.setBounds(30, HEIGT / 2 - 100, 150, 30);
        pin_field.setBounds(200, HEIGT / 2 - 100, 150, 30);
        name_field.setBounds(WIDTH / 2 - 75, HEIGT / 2 - 50, 150, 30);

        title.setBounds(WIDTH / 2 - 70, HEIGT / 2 - 200, 200, 50);
        register.setBounds(WIDTH / 2 - 50, HEIGT / 2 + 25, 100, 50);
        back.setBounds(WIDTH / 2 - 50, HEIGT / 2 + 90, 100, 50);

        panel.add(title);
        panel.add(register);
        panel.add(id_field);
        panel.add(pin_field);
        panel.add(back);
        panel.add(name_field);
        this.setContentPane(panel);
        this.setSize(WIDTH, HEIGT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }




}

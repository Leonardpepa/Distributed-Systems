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

public class Register extends JFrame {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private final Socket clientSocket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private JPanel panel;
    private JButton register;
    private JLabel title;

    private JTextField id_field;

    private JTextField pin_field;

    private JTextField name_field;
    private JButton back;

    // get the socket connection and the input stream already connected
    public Register(Socket socket, ObjectInputStream input, ObjectOutputStream output, JFrame parentFrame) {

        clientSocket = socket;
        ObjectOutputStream clientOutputStream = output;
        ObjectInputStream clientInputStream = input;
        this.input = clientInputStream;
        this.output = clientOutputStream;

        setUpGUI(parentFrame);

        // navigate to Login window
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                parentFrame.setVisible(true);
            }
        });

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_string = id_field.getText();
                String pin_string = pin_field.getText();
                String name = name_field.getText();

                // check if fields are empty and warn the user
                if (id_string.isEmpty() || pin_string.isEmpty() || id_string.isBlank() || pin_string.isBlank() || name.isBlank() || name.isEmpty() || name.equals("Enter your name")) {
                    JOptionPane.showMessageDialog(Register.this, "Please fill all the fields");
                    return;
                }

                try {
                    // parse the id and pin into integer values
                    int id = Integer.parseInt(id_string);
                    int pin = Integer.parseInt(pin_string);

                    // create Register request
                    Request request = Request.createRegisterRequest(id, pin, name, 0);

                    // send request to the server and wait for the response
                    try {
                        output.writeObject(request);
                        Response response = (Response) input.readObject();

                        // if the request is successful then we navigate back to the login screen
                        if (response.isOk()) {
                            JOptionPane.showMessageDialog(Register.this, "Account created successfuly");
                            dispose();
                            parentFrame.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(Register.this, "There was an error creating the account");
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Register.this, "Something went wrong please try again");
                }
            }
        });

    }

    public void setUpGUI(JFrame parentFrame) {
        panel = new JPanel();
        panel.setLayout(null);
        register = new JButton("Register");
        title = new JLabel("Welcome to Pamak Bank");
        back = new JButton("back");

        id_field = new JTextField("Enter your id");
        pin_field = new JTextField("Enter your pin");
        name_field = new JTextField("Enter your name");

        title.setFont(new Font("Arial", Font.CENTER_BASELINE, 20));
        title.setBounds(80, HEIGHT / 2 - 175, WIDTH, 50);


        id_field.setBounds(30, HEIGHT / 2 - 100, 150, 30);
        pin_field.setBounds(200, HEIGHT / 2 - 100, 150, 30);
        name_field.setBounds(WIDTH / 2 - 75, HEIGHT / 2 - 50, 150, 30);

        title.setBounds(WIDTH / 2 - 70, HEIGHT / 2 - 200, 200, 50);
        register.setBounds(WIDTH / 2 - 50, HEIGHT / 2 + 25, 100, 50);
        back.setBounds(WIDTH / 2 - 50, HEIGHT / 2 + 90, 100, 50);

        panel.add(title);
        panel.add(register);
        panel.add(id_field);
        panel.add(pin_field);
        panel.add(back);
        panel.add(name_field);
        this.setContentPane(panel);
        this.setTitle("Register Pamak Bank");
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(parentFrame);
    }


}

package View;

import Controller.Request;
import Controller.Response;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Login extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private JPanel panel;
    private JButton login;
    private JButton register;
    private JLabel title;

    private JTextField id_field;

    private JTextField pin_field;

    public Login() {

        try {
            clientSocket = new Socket("localhost", 3008);
            ObjectOutputStream clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream clientInputStream = new ObjectInputStream(clientSocket.getInputStream());

            this.input = clientInputStream;
            this.output = clientOutputStream;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(Login.this, "Server is not responding");
            return;
        }

        setUpGUI();

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_string = id_field.getText();
                String pin_string = pin_field.getText();

                if (id_string.isEmpty() || pin_string.isEmpty() || id_string.isBlank() || pin_string.isBlank()) {
                    JOptionPane.showMessageDialog(Login.this, "Please fill all the fields");
                    return;
                }

                try {
                    int id = Integer.parseInt(id_string);
                    int pin = Integer.parseInt(pin_string);

                    Request request = Request.createAuthRequest(id, pin);

                    try {
                        output.writeObject(request);
                        Response response = (Response) input.readObject();
                        if (response.isOk()) {
                            Login.this.setVisible(false);
                            new HomeWindow(clientSocket, input, output, response.getId(), response.getName(), Login.this);
                        } else {
                            JOptionPane.showMessageDialog(Login.this, response.getMessage());
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Login.this, "Please fill all the fields");
                }
            }
        });


        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login.this.setVisible(false);
                new Register(clientSocket, input, output, Login.this);
            }
        });

    }

    public void setUpGUI() {
        panel = new JPanel();
        panel.setLayout(null);
        login = new JButton("Login");
        register = new JButton("Register");
        title = new JLabel("Welcome to Pamak Bank");

        id_field = new JTextField("Enter your id");
        pin_field = new JTextField("Enter your pin");

        id_field.setBounds(30, HEIGHT / 2 - 100, 150, 30);
        pin_field.setBounds(200, HEIGHT / 2 - 100, 150, 30);
        title.setBounds(WIDTH / 2 - 70, HEIGHT / 2 - 200, 200, 50);
        login.setBounds(WIDTH / 2 - 50, HEIGHT / 2 - 50, 100, 50);
        register.setBounds(WIDTH / 2 - 50, HEIGHT / 2 + 25, 100, 50);
        panel.add(title);
        panel.add(login);
        panel.add(register);
        panel.add(id_field);
        panel.add(pin_field);
        this.setContentPane(panel);
        this.setTitle("Login Pamak Bank");
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

}

package View;

import Controller.API;
import Controller.Request;
import Controller.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Login extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private JPanel panel;
    private JButton login;
    private JButton register;
    private JLabel title;

    private JTextField id_field;

    private JTextField pin_field;

    private final int PORT = Registry.REGISTRY_PORT;
    private final String ADDRESS = "localhost";
    API api;
    public Login() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(ADDRESS,PORT);
        api = (API) registry.lookup("ATM_API");
        setUpGUI();

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_string = id_field.getText();
                String pin_string = pin_field.getText();

                // check if fields are empty and warn the user
                if (id_string.isEmpty() || pin_string.isEmpty() || id_string.isBlank() || pin_string.isBlank()) {
                    JOptionPane.showMessageDialog(Login.this, "Please fill all the fields");
                    return;
                }

                try {
                    // parse the fields to integer values
                    int id = Integer.parseInt(id_string);
                    int pin = Integer.parseInt(pin_string);

                    // create the authentication request
                    Request request = Request.createAuthRequest(id, pin);

                    try {
                        // send the request to the server and wait the response
                        Response response = api.authenticate(request);

                        // if response is successful login else show message to the user
                        if (response.isOk()) {
                            Login.this.setVisible(false);
                            new HomeWindow(api, response.getId(), response.getName(), Login.this);
                        } else {
                            JOptionPane.showMessageDialog(Login.this, response.getMessage());
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(Login.this, "Something went wrong please reopen the app and try again");
                    }
                } catch (NumberFormatException ex) {
                    // show message to the user if parsing the inputs failed
                    JOptionPane.showMessageDialog(Login.this, "Please fill all the fields");
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // open register window
                Login.this.setVisible(false);
                new Register(api, Login.this);
            }
        });

    }

    // Gui configurations
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

        title.setBounds(80, HEIGHT / 2 - 175, WIDTH, 50);
        title.setFont(new Font("Arial", Font.CENTER_BASELINE, 20));

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

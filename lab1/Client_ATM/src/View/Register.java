package View;

import Controller.Request;
import Controller.Response;

import javax.swing.*;
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

    public Register(Socket socket, ObjectInputStream input, ObjectOutputStream output, JFrame parentFrame) {

        clientSocket = socket;
        ObjectOutputStream clientOutputStream = output;
        ObjectInputStream clientInputStream = input;
        this.input = clientInputStream;
        this.output = clientOutputStream;

        setUpGUI(parentFrame);

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

                if (id_string.isEmpty() || pin_string.isEmpty() || id_string.isBlank() || pin_string.isBlank() || name.isBlank() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(Register.this, "Please fill all the fields");
                    return;
                }

                try {
                    int id = Integer.parseInt(id_string);
                    int pin = Integer.parseInt(pin_string);

                    Request request = Request.createRegisterRequest(id, pin, name, 0);

                    try {
                        output.writeObject(request);
                        Response response = (Response) input.readObject();
                        if (response.isOk()) {
                            JOptionPane.showMessageDialog(Register.this, "Account created successfuly");
                        } else {
                            JOptionPane.showMessageDialog(Register.this, "There was an error creating the account");
                        }
                        dispose();
                        parentFrame.setVisible(true);
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

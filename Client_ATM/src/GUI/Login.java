package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Login extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGT = 400;

    private JPanel panel;
    private JTextField idField;
    private JButton submit;
    private JLabel title;

    private Socket clientSocket;
    private DataInputStream input;
    private DataOutputStream output;

    public Login() {

        try {
            clientSocket = new Socket("localhost", 3008);
            this.input = new DataInputStream(clientSocket.getInputStream());
            this.output = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Server is not responding");
            return;
        }

        setUpGUI();

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticatedUser();
            }
        });

    }

    public Login(Socket clientSocket, DataInputStream input, DataOutputStream output) {

        clientSocket = clientSocket;
        this.input = input;
        this.output = output;

        setUpGUI();

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticatedUser();
            }
        });

    }

    public void authenticatedUser() {
        String id = idField.getText();
        if (id.isBlank() || id.isEmpty()) {
            JOptionPane.showMessageDialog(null, "ID cannot be empty");
            return;
        }
        if (id.equalsIgnoreCase("Please enter your id")) {
            JOptionPane.showMessageDialog(null, "Please enter an id");
            return;
        }

        try {
            int numericId = Integer.parseInt(id);
            output.writeUTF(numericId + "");

            if (input.readBoolean()) {
                // authentication succeded
                this.dispose();
                new HomeWindow(clientSocket, input, output);
            } else {
                JOptionPane.showMessageDialog(null, "Customer with that id doesnt exist");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Id must be a number");
            return;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    public void setUpGUI() {
        panel = new JPanel();
        panel.setLayout(null);
        idField = new JTextField("Please enter your id");
        submit = new JButton("Submit");
        title = new JLabel("Welcome to Pamak Bank");

        idField.setBounds(WIDTH / 2 - 100, HEIGT / 2 - 100, 200, 50);
        title.setBounds(WIDTH / 2 - 70, HEIGT / 2 - 200, 200, 50);
        submit.setBounds(WIDTH / 2 - 50, HEIGT / 2, 100, 50);

        panel.add(idField);
        panel.add(title);
        panel.add(submit);
        this.setContentPane(panel);
        this.setSize(WIDTH, HEIGT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

}

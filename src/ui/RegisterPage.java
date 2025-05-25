
package ui;

import security.PasswordUtils;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class RegisterPage extends JFrame {
    public RegisterPage() {
        setTitle("Cadastro");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1));
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JLabel suggestion = new JLabel("Sugestão: " + PasswordUtils.generateStrongPassword());
        JButton registerButton = new JButton("Cadastrar");

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Senha:"));
        panel.add(passwordField);
        panel.add(suggestion);
        panel.add(registerButton);

        add(panel);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
                    return;
                }

                String hashedPassword = PasswordUtils.hashPassword(password);
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"));
                    writer.write(email + ";" + hashedPassword);
                    writer.close();
                    dispose();
                    new LoginPage();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }
}

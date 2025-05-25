
package ui;

import security.EmailService;
import security.PasswordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Random;

public class LoginPage extends JFrame {
    public LoginPage() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1));
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Senha:"));
        panel.add(passwordField);
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                    String line = reader.readLine();
                    if (line != null) {
                        String[] parts = line.split(";");
                        if (parts[0].equals(email) && PasswordUtils.verifyPassword(password, parts[1])) {
                            // 2FA
                            String code = String.valueOf(new Random().nextInt(900000) + 100000);
                            EmailService.sendEmail(email, "Seu código 2FA", "Seu código: " + code);
                            String enteredCode = JOptionPane.showInputDialog("Código enviado para o email:");
                            if (code.equals(enteredCode)) {
                                dispose();
                                new PasswordManagerPage(email);
                            } else {
                                JOptionPane.showMessageDialog(null, "Código incorreto!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Credenciais inválidas!");
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }
}

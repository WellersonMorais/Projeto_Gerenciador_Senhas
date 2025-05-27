package ui;

import security.PasswordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginPage extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPage() {
        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Senha:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Entrar");
        loginButton.addActionListener(this::handleLogin);

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Espaço vazio para alinhamento
        panel.add(loginButton);

        add(panel);
    }

    private void handleLogin(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean authenticated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2 && parts[0].equals(email)) {
                    if (PasswordUtils.verifyPassword(password, parts[1])) {
                        authenticated = true;
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao ler arquivo de usuários: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }

        if (authenticated) {
            JOptionPane.showMessageDialog(this, "Login realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new PasswordManagerPage(email);
        } else {
            JOptionPane.showMessageDialog(this, "Credenciais inválidas!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

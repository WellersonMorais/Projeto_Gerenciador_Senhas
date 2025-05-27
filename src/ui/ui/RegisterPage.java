package ui;

import model.User;
import security.EmailSender;
import security.PasswordUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;
import java.util.HashMap;

public class RegisterPage extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private static final HashMap<String, User> users = new HashMap<>();

    public RegisterPage() {
        setTitle("Cadastro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        emailField = new JTextField();
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        passwordField = new JPasswordField();
        String suggestedPassword = PasswordUtils.generateStrongPassword();
        passwordField.setText(suggestedPassword);
        passwordField.setForeground(Color.GRAY);

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setText(suggestedPassword);
                passwordField.setForeground(Color.BLACK);
            }
        });

        panel.add(new JLabel("Senha:"));
        panel.add(passwordField);

        JButton registerButton = new JButton("Cadastrar");
        registerButton.addActionListener(e -> handleRegister());

        JButton loginButton = new JButton("Ir para Login");
        loginButton.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        JPanel buttons = new JPanel();
        buttons.add(registerButton);
        buttons.add(loginButton);

        add(panel, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void handleRegister() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        // Código de verificação por email
        String code = String.format("%06d", new SecureRandom().nextInt(1000000));
        try {
            EmailSender.sendVerificationEmail(email, code);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao enviar e-mail: " + e.getMessage());
            return;
        }

        String inputCode = JOptionPane.showInputDialog(this, "Digite o código enviado ao seu e-mail:");
        if (!code.equals(inputCode)) {
            JOptionPane.showMessageDialog(this, "Código incorreto.");
            return;
        }

        String hashedPassword = PasswordUtils.hashPassword(password);
        users.put(email, new User(email, hashedPassword));

        JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!");
        dispose();
        new LoginPage();
    }

    public static HashMap<String, User> getUsers() {
        return users;
    }
}

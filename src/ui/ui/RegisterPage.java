package ui;

import db.UserSave;
import security.EmailService;
import security.PasswordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.security.SecureRandom;

public class RegisterPage extends JFrame {

    private static final SecureRandom secureRandom = new SecureRandom();
    private JTextField emailField;
    private JPasswordField passwordField;

    public RegisterPage() {
        setTitle("Cadastro");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        emailField = new JTextField();
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        passwordField = new JPasswordField();

        String suggestedPassword = PasswordUtils.generateStrongPassword();
        passwordField.setText(suggestedPassword);
        passwordField.setForeground(Color.GRAY);
        passwordField.setEchoChar((char) 0);

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setForeground(Color.BLACK);
                passwordField.setEchoChar('*');
            }

            @Override
            public void focusLost(FocusEvent e) {
                String current = new String(passwordField.getPassword());
                if (current.equals(suggestedPassword)) {
                    passwordField.setEchoChar((char) 0);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

        JLabel passwordRules = new JLabel(
        "<html><ul>" +
        "<li>Ao menos 6 caracteres</li>" +
        "<li>1 letra maiúscula</li>" +
        "<li>1 número</li></ul></html>");
        passwordRules.setForeground(Color.DARK_GRAY);

        panel.add(passwordField);

        JButton registerButton = new JButton("Cadastrar");
        registerButton.setBackground(Color.GRAY);      // cor de fundo
        registerButton.setForeground(Color.WHITE);     // cor do texto
        registerButton.addActionListener(e -> handleRegister());

        JButton loginButton = new JButton("Ir para Login");
        loginButton.setBackground(Color.BLUE);      // cor de fundo
        loginButton.setForeground(Color.WHITE); 
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
    String email = emailField.getText().trim();
    String password = new String(passwordField.getPassword());

    if (email.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!PasswordUtils.isValidPassword(password)) {
        JOptionPane.showMessageDialog(this,
                "A senha deve ter no mínimo 6 caracteres, incluir uma letra maiúscula e um número.",
                "Senha inválida",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Gera código de verificação
    String code = String.format("%06d", secureRandom.nextInt(1000000));

    try {
        EmailService.sendVerificationEmail(email, code);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro ao enviar e-mail: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String inputCode = JOptionPane.showInputDialog(this, "Digite o código enviado ao seu e-mail:");

    if (inputCode == null || !inputCode.equals(code)) {
        JOptionPane.showMessageDialog(this, "Código incorreto.", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String hashedPassword = PasswordUtils.hashPassword(password);

    try {
        UserSave.salvarUsuario(email, hashedPassword);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro ao salvar usuário no banco: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    dispose();
    new LoginPage();
}

}


package ui;

import security.EncryptionUtils;
import security.PasswordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Base64;

public class PasswordManagerPage extends JFrame {
    private String userEmail;

    public PasswordManagerPage(String userEmail) {
        this.userEmail = userEmail;
        setTitle("Gerenciador de Senhas");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JTextArea credentialArea = new JTextArea();
        credentialArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(credentialArea);

        JTextField serviceField = new JTextField();
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton generateButton = new JButton("Gerar senha forte");
        JButton saveButton = new JButton("Salvar");
        JButton logoutButton = new JButton("Sair");

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Serviço:"));
        inputPanel.add(serviceField);
        inputPanel.add(new JLabel("Usuário:"));
        inputPanel.add(userField);
        inputPanel.add(new JLabel("Senha:"));
        inputPanel.add(passField);
        inputPanel.add(generateButton);
        inputPanel.add(saveButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(logoutButton, BorderLayout.SOUTH);

        loadCredentials(credentialArea);

        generateButton.addActionListener((ActionEvent e) -> {
            passField.setText(PasswordUtils.generateStrongPassword());
        });

        saveButton.addActionListener((ActionEvent e) -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("credentials.txt", true))) {
                String encrypted = EncryptionUtils.encrypt(new String(passField.getPassword()));
                writer.write(serviceField.getText() + ";" + userField.getText() + ";" + encrypted + "\n");
                credentialArea.append("[" + serviceField.getText() + "] " + userField.getText() + " : " + passField.getText() + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        add(panel);
        setVisible(true);
    }

    private void loadCredentials(JTextArea area) {
        try (BufferedReader reader = new BufferedReader(new FileReader("credentials.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String decrypted = EncryptionUtils.decrypt(parts[2]);
                    area.append("[" + parts[0] + "] " + parts[1] + " : " + decrypted + "\n");
                }
            }
        } catch (Exception e) {
            // No problem on first run
        }
    }
}

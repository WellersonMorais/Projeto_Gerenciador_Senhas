package ui;

import security.EncryptionUtils;
import security.PasswordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class PasswordManagerPage extends JFrame {
    private String userEmail;

    private JTextArea credentialArea;
    private JTextField serviceField;
    private JTextField userField;
    private JPasswordField passField;
    private JButton generateButton;
    private JButton saveButton;
    private JButton logoutButton;

    public PasswordManagerPage(String userEmail) {
        this.userEmail = userEmail;

        setTitle("Gerenciador de Senhas - " + userEmail);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadCredentials();

        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        credentialArea = new JTextArea();
        credentialArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(credentialArea);

        serviceField = new JTextField();
        userField = new JTextField();
        passField = new JPasswordField();

        generateButton = new JButton("Gerar senha forte");
        saveButton = new JButton("Salvar");
        logoutButton = new JButton("Sair");

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        add(panel);

        // Ações dos botões
        generateButton.addActionListener((ActionEvent e) -> {
            passField.setText(PasswordUtils.generateStrongPassword());
        });

        saveButton.addActionListener((ActionEvent e) -> {
            saveCredential();
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginPage();
        });
    }

    private void loadCredentials() {
        credentialArea.setText(""); // limpa antes de carregar
        try (BufferedReader reader = new BufferedReader(new FileReader("credentials.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String decrypted = EncryptionUtils.decrypt(parts[2]);
                    credentialArea.append("[" + parts[0] + "] " + parts[1] + " : " + decrypted + "\n");
                }
            }
        } catch (FileNotFoundException e) {
            // Arquivo não existe ainda, ok para primeira execução
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar credenciais: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveCredential() {
        String service = serviceField.getText().trim();
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());

        if (service.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos antes de salvar.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("credentials.txt", true))) {
            String encrypted = EncryptionUtils.encrypt(pass);
            writer.write(service + ";" + user + ";" + encrypted + "\n");
            writer.flush();

            credentialArea.append("[" + service + "] " + user + " : " + pass + "\n");

            // Limpa campos após salvar
            serviceField.setText("");
            userField.setText("");
            passField.setText("");

            JOptionPane.showMessageDialog(this, "Credencial salva com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar credencial: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

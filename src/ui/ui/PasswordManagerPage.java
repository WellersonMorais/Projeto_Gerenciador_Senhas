package ui;

import security.EncryptionUtils;
import security.LeakChecker;
import security.PasswordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class PasswordManagerPage extends JFrame {

    private Timer inactivityTimer;
    private DefaultListModel<String> credentialListModel;
    private JList<String> credentialList;

    private JTextField serviceField;
    private JTextField userField;
    private JPasswordField passField;
    @SuppressWarnings("unused")
    private String userEmail;

    public PasswordManagerPage(String userEmail) {
        this.userEmail = userEmail;

        setTitle("Gerenciador de Senhas - " + userEmail);
        setSize(500, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        initComponents();
        loadCredentials();

        int timeout = 5 * 60 * 1000; // 5 minutos em ms

        inactivityTimer = new Timer(timeout, e -> {
            inactivityTimer.stop(); // ✅ Evita execução contínua
            JOptionPane.showMessageDialog(this, "Sessão expirada por inatividade.");
            dispose();
            new LoginPage();
        });

        inactivityTimer.setRepeats(false);
        inactivityTimer.start();

        // Reseta timer para qualquer interação do usuário
        addUserActivityListeners(this);

        setVisible(true);
    }

    private void addUserActivityListeners(Component comp) {
        comp.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                inactivityTimer.restart();
            }
        });
        comp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                inactivityTimer.restart();
            }
        });
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                addUserActivityListeners(child);
            }
        }
    }

    private void initComponents() {
        credentialListModel = new DefaultListModel<>();
        credentialList = new JList<>(credentialListModel);
        credentialList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(credentialList);

        serviceField = new JTextField();
        userField = new JTextField();
        passField = new JPasswordField();

        JButton generateButton = new JButton("Gerar senha forte");
        generateButton.setToolTipText("Gera uma senha segura e aleatória");

        JButton saveButton = new JButton("Salvar");
        saveButton.setToolTipText("Salva a senha no gerenciador");

        JButton deleteButton = new JButton("Excluir");
        deleteButton.setToolTipText("Remove a senha selecionada");

        JButton logoutButton = new JButton("Sair");
        logoutButton.setToolTipText("Encerra a sessão atual");


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

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(deleteButton);
        bottomPanel.add(logoutButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        generateButton.addActionListener(e -> passField.setText(PasswordUtils.generateStrongPassword()));

        saveButton.addActionListener(e -> saveCredential());

        deleteButton.addActionListener(e -> deleteSelectedCredential());

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginPage();
        });
    }

    private void loadCredentials() {
        credentialListModel.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("credentials.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String decrypted = EncryptionUtils.decrypt(parts[2]);
                    String display = "[" + parts[0] + "] " + parts[1] + " : " + decrypted;
                    credentialListModel.addElement(display);
                }
            }
        } catch (FileNotFoundException e) {
            // Arquivo pode não existir na primeira execução, ok
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

        if (LeakChecker.isPasswordLeaked(pass)) {
            JOptionPane.showMessageDialog(this, "ATENÇÃO: Esta senha foi vazada anteriormente! Considere usar outra senha.", "Senha Vazada", JOptionPane.WARNING_MESSAGE);
            int option = JOptionPane.showConfirmDialog(this, "Deseja salvar mesmo assim?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (option != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("credentials.txt", true))) {
            String encrypted = EncryptionUtils.encrypt(pass);
            writer.write(service + ";" + user + ";" + encrypted);
            writer.newLine();
            writer.flush();

            String display = "[" + service + "] " + user + " : " + pass;
            credentialListModel.addElement(display);

            serviceField.setText("");
            userField.setText("");
            passField.setText("");

            JOptionPane.showMessageDialog(this, "Credencial salva com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar credencial: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteSelectedCredential() {
        int selectedIndex = credentialList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma credencial para excluir.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir a credencial selecionada?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            File file = new File("credentials.txt");
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            String selectedValue = credentialListModel.getElementAt(selectedIndex);
            int firstBracketClose = selectedValue.indexOf("]");
            if (firstBracketClose == -1) {
                JOptionPane.showMessageDialog(this, "Formato inválido da credencial selecionada.");
                return;
            }
            String service = selectedValue.substring(1, firstBracketClose);
            String rest = selectedValue.substring(firstBracketClose + 2);
            int colonIndex = rest.indexOf(" : ");
            if (colonIndex == -1) {
                JOptionPane.showMessageDialog(this, "Formato inválido da credencial selecionada.");
                return;
            }
            String user = rest.substring(0, colonIndex);

            ArrayList<String> updatedLines = new ArrayList<>();
            for (String line : lines) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    if (!(parts[0].equals(service) && parts[1].equals(user))) {
                        updatedLines.add(line);
                    }
                } else {
                    updatedLines.add(line);
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                for (String l : updatedLines) {
                    writer.write(l);
                    writer.newLine();
                }
                writer.flush();
            }

            credentialListModel.remove(selectedIndex);

            JOptionPane.showMessageDialog(this, "Credencial excluída com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir credencial: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

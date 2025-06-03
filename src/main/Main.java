package main;

import ui.RegisterPage;
import db.SetupDatabase;
import config.Descriptografador;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Solicita a senha mestra
        JPasswordField campoSenha = new JPasswordField();
        Object[] mensagem = { "Digite a senha mestra:", campoSenha };

        int opcao = JOptionPane.showConfirmDialog(null, mensagem, "Autenticação", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcao == JOptionPane.OK_OPTION) {
            String senhaMestra = new String(campoSenha.getPassword());

            try {
                // Tenta descriptografar — sucesso indica senha válida
                String chaveDescriptografada = Descriptografador.descriptografarArquivo(senhaMestra);
                System.out.println("Chave descriptografada com sucesso."); // Opcional: log para debug

                // Cria a tabela users, se ainda não existir
                SetupDatabase.setup();

                // Abre a tela de registro
                new RegisterPage();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Senha mestra incorreta ou falha na descriptografia.", "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(1); // Encerra o programa
            }

        } else {
            JOptionPane.showMessageDialog(null, "Acesso cancelado.", "Encerrando", JOptionPane.WARNING_MESSAGE);
            System.exit(0); // Encerra se o usuário cancelar
        }
    }
}

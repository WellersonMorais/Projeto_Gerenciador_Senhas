package main;

import ui.RegisterPage;
import db.SetupDatabase;

public class Main {
    public static void main(String[] args) {
        // Cria a tabela users, se ainda não existir
        SetupDatabase.setup();

        // Abre a tela de registro
        new RegisterPage();
    }
}

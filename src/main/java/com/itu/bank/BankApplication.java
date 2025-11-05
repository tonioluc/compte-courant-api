package com.itu.bank;

import java.sql.Connection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.itu.bank.utils.Connexion;
import com.itu.bank.utils.IdVirementGenerator;

@SpringBootApplication
public class BankApplication {
    public static void main(String[] args) {
        try (Connection conn = Connexion.getConnexion()) {
            IdVirementGenerator.initialize(conn);
            System.out.println("✅ IdVirementGenerator initialisé avec succès");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'initialisation du générateur d'ID : " + e.getMessage());
        }
        SpringApplication.run(BankApplication.class, args);
    }
}

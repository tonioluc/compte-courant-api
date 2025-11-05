package com.itu.bank.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IdVirementGenerator {
    private static int currentNumber = 0;
    public static int getCurrentNumber() {
        return currentNumber;
    }

    public static void setCurrentNumber(int currentNumber) {
        IdVirementGenerator.currentNumber = currentNumber;
    }

    private static boolean initialized = false;

    // --- Méthode d'initialisation : appelée une seule fois ---
    public static synchronized void initialize(Connection conn) throws Exception {
        if (initialized)
            return;

        String sql = """
                    SELECT COALESCE(MAX(CAST(SUBSTRING(id_virement FROM 'VRMT_(\\d+)') AS INTEGER)), 0) AS max_num
                    FROM virement
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                currentNumber = rs.getInt("max_num");
            }
            initialized = true;
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'initialisation de l'IdVirementGenerator : " + e.getMessage(), e);
        }
    }

    // --- Méthode pour générer un nouvel ID ---
    public static synchronized String generateNewId() throws Exception {
        if (!initialized) {
            // sécurité au cas où initialize() n’a pas été appelée
            try (Connection conn = Connexion.getConnexion()) {
                initialize(conn);
            }
        }

        currentNumber++;
        return "VRMT_" + currentNumber;
    }
}

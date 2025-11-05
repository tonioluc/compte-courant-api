package com.itu.bank.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

    private static final String URL = "jdbc:postgresql://localhost:5432/bank";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    private static Connection connection = null;

    /**
     * Établit une connexion à la base de données si elle n'existe pas encore.
     * @return un objet Connection connecté à la base PostgreSQL
     * @throws SQLException si une erreur de connexion se produit
     */
    public static Connection getConnexion() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Chargement du driver PostgreSQL
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver PostgreSQL introuvable.", e);
            }
        }
        return connection;
    }

    /**
     * Ferme la connexion si elle est encore ouverte.
     * @throws SQLException si une erreur se produit lors de la fermeture
     */
    public static void deconnexion() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

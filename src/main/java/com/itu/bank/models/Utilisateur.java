package com.itu.bank.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Utilisateur {
    private int idUtilsateur;
    private String username;
    private String password;

    public int getIdUtilsateur() {
        return idUtilsateur;
    }

    public void setIdUtilsateur(int idUtilsateur) {
        this.idUtilsateur = idUtilsateur;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ✅ Méthode pour check login
    public static Utilisateur checkLogin(Connection conn, String username, String password) throws Exception {
        String query = "SELECT id_utilisateur, username, password FROM utilisateur WHERE username = ? AND password = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            rs = stmt.executeQuery();

            if (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setIdUtilsateur(rs.getInt("id_utilisateur"));
                utilisateur.setUsername(rs.getString("username"));
                utilisateur.setPassword(rs.getString("password"));
                return utilisateur;
            } else {
                return null; // Aucun utilisateur trouvé
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors de la vérification du login : " + e.getMessage(), e);
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
    }

}
package com.itu.bank.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Mouvement {

    private int idMouvement;
    private LocalDateTime dateHeure;
    private String source;
    private int idTypeMouvement;
    private float montant;  
    private int idCompte;

    public int getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(int idCompte) {
        this.idCompte = idCompte;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public int getIdMouvement() {
        return idMouvement;
    }

    public void setIdMouvement(int idMouvement) {
        this.idMouvement = idMouvement;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getIdTypeMouvement() {
        return idTypeMouvement;
    }

    public void setIdTypeMouvement(int idTypeMouvement) {
        this.idTypeMouvement = idTypeMouvement;
    }

    // --- Sauvegarde en base ---
    public Mouvement save(Connection conn) throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Connexion invalide ou fermée");
        }

        // Si dateHeure n'est pas définie, on prend la date actuelle
        if (this.dateHeure == null) {
            this.dateHeure = LocalDateTime.now();
        }

        String sql = """
                INSERT INTO mouvement(date_heure, source, id_type_mouvement , montant , id_compte)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id_mouvement
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(this.dateHeure));
            pstmt.setString(2, this.source);
            pstmt.setInt(3, this.idTypeMouvement);
            pstmt.setFloat(4, this.montant);
            pstmt.setFloat(5, this.idCompte);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.idMouvement = rs.getInt("id_mouvement");
                } else {
                    throw new Exception("Échec de l'insertion du mouvement");
                }
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'enregistrement du mouvement : " + e.getMessage(), e);
        }

        return this;
    }
}

package com.itu.bank.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Validation {

    private int idValidation;
    private LocalDateTime dateDeValidation;
    private String idObjet; // Remplacé int par String pour correspondre à virement.id_virement
    private int idUtilisateur;
    private String nomTable;

    public int getIdValidation() {
        return idValidation;
    }

    public void setIdValidation(int idValidation) {
        this.idValidation = idValidation;
    }

    public LocalDateTime getDateDeValidation() {
        return dateDeValidation;
    }

    public void setDateDeValidation(LocalDateTime dateDeValidation) {
        this.dateDeValidation = dateDeValidation;
    }

    public String getIdObjet() {
        return idObjet;
    }

    public void setIdObjet(String idObjet) {
        this.idObjet = idObjet;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNomTable() {
        return nomTable;
    }

    public void setNomTable(String nomTable) {
        this.nomTable = nomTable;
    }

    // --- Sauvegarde en base ---
    public Validation save(Connection conn) throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Connexion invalide ou fermée");
        }

        // Vérifie que le nom de table est défini
        if (this.nomTable == null || this.nomTable.isBlank()) {
            throw new Exception("Nom de table invalide pour la validation");
        }

        // Si la date n'est pas fournie, on prend la date actuelle
        if (this.dateDeValidation == null) {
            this.dateDeValidation = LocalDateTime.now();
        }

        // Construction dynamique de la requête avec le nom de table
        String sql = "INSERT INTO " + this.nomTable + " (date_de_validation, id_objet, id_utilisateur) " +
                "VALUES (?, ?, ?) RETURNING id_validation_virement";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(this.dateDeValidation));
            pstmt.setString(2, this.idObjet);
            pstmt.setInt(3, this.idUtilisateur);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.idValidation = rs.getInt("id_validation_virement");
                } else {
                    throw new Exception("Échec de l'insertion de la validation dans " + this.nomTable);
                }
            }
        } catch (Exception e) {
            throw new Exception(
                    "Erreur lors de l'enregistrement de la validation dans " + this.nomTable + " : " + e.getMessage(),
                    e);
        }

        return this;
    }

}

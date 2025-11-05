package com.itu.bank.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class HistoriqueVirement {
    private int idHistoriqueVirement;
    private String idObjet;
    private LocalDateTime dateHeure;
    private int idUtilsateur;

    public int getIdHistoriqueVirement() {
        return idHistoriqueVirement;
    }

    public void setIdHistoriqueVirement(int idHistoriqueVirement) {
        this.idHistoriqueVirement = idHistoriqueVirement;
    }

    public String getIdObjet() {
        return idObjet;
    }

    public void setIdObjet(String idObjet) {
        this.idObjet = idObjet;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public int getIdUtilsateur() {
        return idUtilsateur;
    }

    public void setIdUtilsateur(int idUtilsateur) {
        this.idUtilsateur = idUtilsateur;
    }

    public HistoriqueVirement save(Connection conn) throws Exception {
        String sql = "INSERT INTO historique_virement (id_objet, date_heure, id_utilisateur) VALUES (?, ?, ?) RETURNING id_historique_virement";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.idObjet);
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(this.dateHeure));
            stmt.setInt(3, this.idUtilsateur);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.idHistoriqueVirement = rs.getInt("id_historique_virement");
                } else {
                    throw new Exception("Échec lors de la récupération de l'identifiant généré");
                }
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'enregistrement de l'historique du virement : " + e.getMessage(), e);
        }

        return this;
    }

}

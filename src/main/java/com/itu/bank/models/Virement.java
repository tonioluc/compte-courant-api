package com.itu.bank.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.itu.bank.utils.IdVirementGenerator;

public class Virement {
    private String idVirement;
    private int idCompteEmetteur;
    private int idCompteDestinataire;
    private float montant;
    private int etat;
    private LocalDateTime dateEffective;
    private int idChange;

    public Virement() throws Exception{
        // Génère un nouvel ID au format VRMT_1, VRMT_2, ...
        this.idVirement = IdVirementGenerator.generateNewId();
    }

    public void setMontant(float montant) throws Exception {
        if (montant < 1) {
            throw new Exception("Le montant de virement ne doit pas être < 1");
        }
        this.montant = montant;
    }

    public String getIdVirement() {
        return idVirement;
    }

    public void setIdVirement(String idVirement) {
        this.idVirement = idVirement;
    }

    public int getIdCompteEmetteur() {
        return idCompteEmetteur;
    }

    public void setIdCompteEmetteur(int idCompteEmetteur) {
        this.idCompteEmetteur = idCompteEmetteur;
    }

    public int getIdCompteDestinataire() {
        return idCompteDestinataire;
    }

    public void setIdCompteDestinataire(int idCompteDestinataire) {
        this.idCompteDestinataire = idCompteDestinataire;
    }

    public float getMontant() {
        return montant;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public LocalDateTime getDateEffective() {
        return dateEffective;
    }

    public void setDateEffective(LocalDateTime dateEffective) {
        this.dateEffective = dateEffective;
    }

    public int getIdChange() {
        return idChange;
    }

    public void setIdChange(int idChange) {
        this.idChange = idChange;
    }

    public Virement save(Connection conn) throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Connexion invalide ou fermée");
        }
        this.etat = 1;

        String sql = """
                    INSERT INTO virement(id_virement, id_compte_emetteur, id_compte_destinataire, montant, etat, date_effective, id_change)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.idVirement);
            pstmt.setInt(2, this.idCompteEmetteur);
            pstmt.setInt(3, this.idCompteDestinataire);
            pstmt.setFloat(4, this.montant);
            pstmt.setInt(5, this.etat);
            pstmt.setTimestamp(6, Timestamp.valueOf(this.dateEffective));
            pstmt.setInt(7, this.idChange);

            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new Exception("Échec de l'insertion du virement");
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'enregistrement du virement : " + e.getMessage(), e);
        }

        return this;
    }

}
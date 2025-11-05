package com.itu.bank.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import com.itu.bank.utils.Connexion;

public class Compte {
    private int idCompte;
    private float solde;
    private float plafond;
    private int idTypeCompte;
    private int idClient;

    public int getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(int idCompte) {
        this.idCompte = idCompte;
    }

    public float getSolde() {
        return solde;
    }

    public void setSolde(float solde) {
        this.solde = solde;
    }

    public float getPlafond() {
        return plafond;
    }

    public void setPlafond(float plafond) {
        this.plafond = plafond;
    }

    public int getIdTypeCompte() {
        return idTypeCompte;
    }

    public void setIdTypeCompte(int idTypeCompte) {
        this.idTypeCompte = idTypeCompte;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void getCompteById(Connection conn) throws Exception {
        String query = "SELECT * from compte where id_compte = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, this.idCompte);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            rs.close();
            stmt.close();
            throw new Exception("Aucun compte trouvé pour l'id " + this.idCompte);
        } else {
            this.setIdClient(rs.getInt("id_client"));
            this.setIdTypeCompte(rs.getInt("id_type_compte"));
            this.setPlafond(rs.getFloat("plafond"));
            this.setSolde(rs.getFloat("solde"));
        }
        rs.close();
        stmt.close();
    }

    public boolean estAutoriserADebiter(float montant) {
        if (this.solde < montant) {
            return false;
        }
        return true;
    }

    public float calculMontantDebiter(Connection conn, LocalDateTime dateEffective) throws Exception {
        String query = "SELECT SUM(montant) AS somme_montant_utiliser " +
                "FROM v_virement_valider " +
                "WHERE id_compte_emetteur = ? " +
                "AND DATE(date_effective) = ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, this.idCompte);
        stmt.setDate(2, Date.valueOf(dateEffective.toLocalDate()));

        ResultSet rs = stmt.executeQuery();
        float somme = 0;
        if (rs.next()) {
            somme = rs.getFloat("somme_montant_utiliser");
        }

        rs.close();
        stmt.close();

        return somme;
    }

    public boolean aAtteintPlafond(Connection conn, LocalDateTime dateEffective, float montantMga) throws Exception {
        float sommeMontantDebiter = this.calculMontantDebiter(conn, dateEffective);
        return (sommeMontantDebiter + montantMga) >= this.getPlafond();
    }

    public void controlleComplexe(Connection conn, LocalDateTime dateEffective, int idChange, float montant)
            throws Exception {
        Change change = new Change(idChange);
        change.getChangeByIdAndDate(conn, dateEffective);
        float montantMga = change.convertirEnAriary(montant);
        if (!this.estAutoriserADebiter(montantMga)) {
            throw new Exception("Ce compte n'a pas du solde insuffisant pour effectuer ce virement");
        }
        if (this.aAtteintPlafond(conn, dateEffective, montantMga)) {
            throw new Exception(
                    "Ce compte a déjà atteint le plafond de virement de " + this.getPlafond() + "MGA pour cet date");
        }
    }

    public Virement virer(String idCompteDestinataireStr, String dateEffectiveStr, String idChangeStr,
            String montantStr) throws Exception {
        int idCompteDestinataire = Integer.parseInt(idCompteDestinataireStr);
        LocalDateTime dateEffective = LocalDateTime.parse(dateEffectiveStr);
        int idChange = Integer.parseInt(idChangeStr);
        float montant = Float.parseFloat(montantStr);

        Virement virement = new Virement();
        virement.setDateEffective(dateEffective);
        virement.setIdChange(idChange);
        virement.setIdCompteDestinataire(idCompteDestinataire);
        virement.setIdCompteEmetteur(this.getIdCompte());
        virement.setMontant(montant);

        Connection conn = null;
        try {
            conn = Connexion.getConnexion();
            conn.setAutoCommit(false);
            this.controlleComplexe(conn, dateEffective, idChange, montant);
            virement.save(conn);
            conn.commit();
            return virement;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                throw e;
            }
        }
    }
}

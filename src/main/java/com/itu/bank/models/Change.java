package com.itu.bank.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Change {
    public Change(String devise, LocalDate dateDebut, LocalDate dateFin, float montant) {
        this.devise = devise;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montant = montant;
    }

    public Change(int idChange) {
        this.idChange = idChange;
    }

    private int idChange;
    private String devise;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private float montant;

    public Change() {
    }

    public int getIdChange() {
        return idChange;
    }

    public void setIdChange(int idChange) {
        this.idChange = idChange;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public void getChangeByDeviseAndDate(Connection conn, LocalDateTime dateEffective) throws Exception {
        String query = "SELECT * FROM change WHERE devise = ? AND ?::date BETWEEN date_debut AND date_fin";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, this.getDevise());
            stmt.setDate(2, Date.valueOf(dateEffective.toLocalDate())); // Utilise uniquement la date

            rs = stmt.executeQuery();

            if (rs.next()) {
                this.setIdChange(rs.getInt("id_change"));
                this.setDevise(rs.getString("devise"));
                this.setDateDebut(rs.getDate("date_debut").toLocalDate());
                this.setDateFin(rs.getDate("date_fin").toLocalDate());
                this.setMontant(rs.getFloat("montant"));
            } else {
                throw new Exception(
                        "Aucun change trouvé pour la devise " + devise + " et la date " + dateEffective.toLocalDate());
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors de la récupération du change : " + e.getMessage(), e);
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
    }

    public float convertirEnAriary(float montant) {
        return montant * this.getMontant();
    }

    public static List<Change> getAllChangeDistinctDevise(Connection conn) throws Exception {
        List<Change> changes = new ArrayList<>();

        String query = "SELECT DISTINCT devise FROM change ORDER BY devise";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Change change = new Change(rs.getString("devise"), null, null, 0f);
                changes.add(change);
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors de la récupération des devises distinctes : " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (Exception ex) {
                throw ex;
            }
        }

        return changes;
    }

    public static List<Change> getAllChange(Connection conn) throws Exception {
        List<Change> changes = new ArrayList<>();

        String query = "SELECT id_change, devise, date_debut, date_fin, montant FROM change ORDER BY id_change";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Change change = new Change(
                        rs.getString("devise"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        rs.getFloat("montant"));
                change.setIdChange(rs.getInt("id_change"));
                changes.add(change);
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors de la récupération des taux de change : " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (Exception ex) {
                // Rien à faire ici
            }
        }

        return changes;
    }
}

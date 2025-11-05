package com.itu.bank.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public void getChangeByIdAndDate(Connection conn, LocalDateTime dateEffective) throws Exception {
        String query = "SELECT * FROM change WHERE id_change = ? AND ?::date BETWEEN date_debut AND date_fin";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, this.idChange);
        stmt.setDate(2, Date.valueOf(dateEffective.toLocalDate())); // dateEffective sans l'heure

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            this.setDateDebut(rs.getDate("date_debut").toLocalDate());
            this.setDateFin(rs.getDate("date_fin").toLocalDate());
            this.setDevise(rs.getString("devise"));
            this.setMontant(rs.getFloat("montant"));
        } else {
            rs.close();
            stmt.close();
            throw new Exception(
                    "Aucun change trouvé pour l'ID " + this.idChange + " et la date " + dateEffective.toLocalDate());
        }

        rs.close();
        stmt.close();
    }

    public float convertirEnAriary(float montant) {
        return montant * this.getMontant();
    }

}

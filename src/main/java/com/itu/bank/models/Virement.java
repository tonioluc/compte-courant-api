package com.itu.bank.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.itu.bank.utils.Connexion;

public class Virement {
    private String idVirement;
    private int idCompteEmetteur;
    private int idCompteDestinataire;
    private float montant;
    private int etat;
    private LocalDateTime dateEffective;
    private int idChange;

    public Virement() throws Exception {
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

    public Frais calculFrais(Connection conn) throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Connexion invalide ou fermée");
        }

        String sql = "SELECT * FROM frais WHERE montant_inf <= ? AND ? <= montant_sup";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setFloat(1, this.montant);
            stmt.setFloat(2, this.montant);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Frais frais = new Frais();
                frais.setIdFrais(rs.getInt("id_frais"));
                frais.setMontantInf(rs.getFloat("montant_inf"));
                frais.setMontantSup(rs.getFloat("montant_sup"));
                frais.setFraisEnMontant(rs.getFloat("frais_en_montant"));
                frais.setFraisPourcentage(rs.getFloat("frais_pourcentage"));
                frais.setIdTypeCompte(rs.getInt("id_type_compte"));
                rs.close();
                return frais;
            } else {
                rs.close();
                throw new Exception("Aucun frais trouvé pour le montant : " + this.montant);
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors du calcul des frais : " + e.getMessage(), e);
        }
    }

    public void getByIdVirement(Connection conn) throws Exception {
        if (this.idVirement == null || this.idVirement.isEmpty()) {
            throw new Exception("idVirement n'est pas défini");
        }

        String sql = "SELECT * FROM virement WHERE id_virement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.idVirement);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new Exception("Aucun virement trouvé pour l'id : " + this.idVirement);
                } else {
                    this.idCompteEmetteur = rs.getInt("id_compte_emetteur");
                    this.idCompteDestinataire = rs.getInt("id_compte_destinataire");
                    this.montant = rs.getFloat("montant");
                    this.etat = rs.getInt("etat");
                    Timestamp ts = rs.getTimestamp("date_effective");
                    if (ts != null) {
                        this.dateEffective = ts.toLocalDateTime();
                    }
                    this.idChange = rs.getInt("id_change");
                }
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors de la récupération du virement : " + e.getMessage(), e);
        }
    }

    public boolean estValidable() {
        if (this.getEtat() >= 11) {
            return false;
        }
        return true;
    }

    public Virement merge(Connection conn) throws Exception {
        if (conn == null || conn.isClosed()) {
            throw new Exception("Connexion invalide ou fermée");
        }

        if (this.idVirement == null || this.idVirement.isEmpty()) {
            throw new Exception("idVirement non défini pour la mise à jour");
        }

        String sql = """
                UPDATE virement
                SET id_compte_emetteur = ?,
                    id_compte_destinataire = ?,
                    montant = ?,
                    etat = ?,
                    date_effective = ?,
                    id_change = ?
                WHERE id_virement = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.idCompteEmetteur);
            pstmt.setInt(2, this.idCompteDestinataire);
            pstmt.setFloat(3, this.montant);
            pstmt.setInt(4, this.etat);
            pstmt.setTimestamp(5, Timestamp.valueOf(this.dateEffective));
            pstmt.setInt(6, this.idChange);
            pstmt.setString(7, this.idVirement);

            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new Exception("Aucun virement trouvé avec id " + this.idVirement + " pour la mise à jour");
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors de la mise à jour du virement : " + e.getMessage(), e);
        }

        return this;
    }

    public Validation valider(String idUtilisateurStr) throws Exception {
        int idUtilisateur = Integer.parseInt(idUtilisateurStr);
        Connection conn = null;
        try {
            conn = Connexion.getConnexion();
            conn.setAutoCommit(false);
            this.getByIdVirement(conn); // maka ny mombamomban'ity virement rehetra ity
            if (!this.estValidable()) {
                throw new Exception("Virement non validable.");
            }
            Validation validation = new Validation();
            validation.setNomTable("validation_virement");
            validation.setIdObjet(this.getIdVirement());
            validation.setIdUtilisateur(idUtilisateur);

            Mouvement mouvDebiterEmetteur = new Mouvement();
            mouvDebiterEmetteur.setIdTypeMouvement(1);
            mouvDebiterEmetteur.setSource(this.getIdVirement());
            mouvDebiterEmetteur.setMontant(this.getMontant());
            mouvDebiterEmetteur.setIdCompte(this.idCompteEmetteur);

            Frais frais = this.calculFrais(conn);
            float totalFrais = frais.getFraisEnMontant() + ((frais.getFraisPourcentage() * this.getMontant()) / 100);

            Mouvement mouvCreditDestinataire = new Mouvement();
            mouvCreditDestinataire.setIdTypeMouvement(2);
            mouvCreditDestinataire.setMontant(this.getMontant() - totalFrais);
            mouvCreditDestinataire.setSource(this.getIdVirement());
            mouvCreditDestinataire.setIdCompte(this.idCompteDestinataire);

            Mouvement mouvCreditetBank = new Mouvement();
            mouvCreditetBank.setIdTypeMouvement(3);
            mouvCreditetBank.setMontant(totalFrais);
            mouvCreditetBank.setSource(this.getIdVirement());

            Compte compteEmetteur = new Compte();
            compteEmetteur.setIdCompte(this.idCompteEmetteur);
            compteEmetteur = compteEmetteur.getCompteById(conn);

            Compte compteDestinataire = new Compte();
            compteDestinataire.setIdCompte(this.idCompteDestinataire);
            compteDestinataire = compteDestinataire.getCompteById(conn);

            compteEmetteur.controlleComplexe(conn, this.getDateEffective(), 1, this.getMontant()); // 1 le idChange satria efa MGA

            compteEmetteur.setSolde(compteEmetteur.getSolde() - this.getMontant());
            compteDestinataire.setSolde(compteDestinataire.getSolde() + this.getMontant() - totalFrais);
            this.setEtat(21); // Update anleh etat we efa validé ity

            // Persistence
            validation.save(conn);
            mouvCreditDestinataire.save(conn);
            mouvCreditetBank.save(conn);
            mouvDebiterEmetteur.save(conn);
            compteEmetteur.merge(conn);
            compteDestinataire.merge(conn);
            this.merge(conn);

            conn.commit();
            return validation;
        } catch (Exception e) {
            conn.rollback();
            throw new Exception("Erreur lors de la validation : " + e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                throw e;
            }
        }
    }

}

/*
 * Calcul frais : retourne Objet de type frais
 * Inséreko le frais
 * Créeeko ndray objet validation
 * Mahazo objet validation
 * persistiavako validation
 * update virement (lasa 21 le etat) le objet aloha le miova eto
 * persistiavako le update
 * commit transaction
 */

// Asio source any anaty mouvement
// Ao anaty classe virement , manana foonction miteny we getMouvementLier()
// Ao anaty classe virement manana classe boolean estValidable , mijery we efa
// valider ve
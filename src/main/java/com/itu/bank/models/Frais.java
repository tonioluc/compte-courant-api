package com.itu.bank.models;

public class Frais {
    private int idFrais;
    private float montantInf;
    private float montantSup;
    private float fraisEnMontant;
    private float fraisPourcentage;
    private int idTypeCompte;

    public int getIdFrais() {
        return idFrais;
    }

    public void setIdFrais(int idFrais) {
        this.idFrais = idFrais;
    }

    public float getMontantInf() {
        return montantInf;
    }

    public void setMontantInf(float montantInf) {
        this.montantInf = montantInf;
    }

    public float getMontantSup() {
        return montantSup;
    }

    public void setMontantSup(float montantSup) {
        this.montantSup = montantSup;
    }

    public float getFraisEnMontant() {
        return fraisEnMontant;
    }

    public void setFraisEnMontant(float fraisEnMontant) {
        this.fraisEnMontant = fraisEnMontant;
    }

    public float getFraisPourcentage() {
        return fraisPourcentage;
    }

    public void setFraisPourcentage(float fraisPourcentage) {
        this.fraisPourcentage = fraisPourcentage;
    }

    public int getIdTypeCompte() {
        return idTypeCompte;
    }

    public void setIdTypeCompte(int idTypeCompte) {
        this.idTypeCompte = idTypeCompte;
    }
}

package com.itu.bank.models;

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
}
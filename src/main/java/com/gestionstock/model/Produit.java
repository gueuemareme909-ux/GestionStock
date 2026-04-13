package com.gestionstock.model;

public class Produit {
    private int id;
    private String nom;
    private double prix;
    private int quantite;
    private int idCategorie;
    private String nomCategorie;

    public Produit() {}

    public Produit(int id, String nom, double prix, int quantite, int idCategorie) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
        this.idCategorie = idCategorie;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public int getIdCategorie() { return idCategorie; }
    public void setIdCategorie(int idCategorie) { this.idCategorie = idCategorie; }

    public String getNomCategorie() { return nomCategorie; }
    public void setNomCategorie(String nomCategorie) { this.nomCategorie = nomCategorie; }
}
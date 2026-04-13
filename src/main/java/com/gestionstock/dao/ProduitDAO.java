package com.gestionstock.dao;

import com.gestionstock.database.DatabaseConnection;
import com.gestionstock.model.Produit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO {

    public void ajouter(Produit produit) {
        String sql = "INSERT INTO produit (nom, prix, quantite, id_categorie) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, produit.getNom());
            pstmt.setDouble(2, produit.getPrix());
            pstmt.setInt(3, produit.getQuantite());
            pstmt.setInt(4, produit.getIdCategorie());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                produit.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Erreur ajout produit : " + e.getMessage());
        }
    }

    public List<Produit> getAll() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT p.*, c.libelle as nom_categorie FROM produit p " +
                "JOIN categorie c ON p.id_categorie = c.id ORDER BY p.nom";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                produits.add(extractProduitFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lecture produits : " + e.getMessage());
        }
        return produits;
    }

    public List<Produit> rechercherParNom(String nom) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT p.*, c.libelle as nom_categorie FROM produit p " +
                "JOIN categorie c ON p.id_categorie = c.id " +
                "WHERE p.nom LIKE ? ORDER BY p.nom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nom + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                produits.add(extractProduitFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche : " + e.getMessage());
        }
        return produits;
    }

    public void modifier(Produit produit) {
        String sql = "UPDATE produit SET nom = ?, prix = ?, quantite = ?, id_categorie = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, produit.getNom());
            pstmt.setDouble(2, produit.getPrix());
            pstmt.setInt(3, produit.getQuantite());
            pstmt.setInt(4, produit.getIdCategorie());
            pstmt.setInt(5, produit.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur modification produit : " + e.getMessage());
        }
    }

    public void supprimer(int id) {
        String sql = "DELETE FROM produit WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur suppression produit : " + e.getMessage());
        }
    }

    private Produit extractProduitFromResultSet(ResultSet rs) throws SQLException {
        Produit p = new Produit();
        p.setId(rs.getInt("id"));
        p.setNom(rs.getString("nom"));
        p.setPrix(rs.getDouble("prix"));
        p.setQuantite(rs.getInt("quantite"));
        p.setIdCategorie(rs.getInt("id_categorie"));
        p.setNomCategorie(rs.getString("nom_categorie"));
        return p;
    }
}
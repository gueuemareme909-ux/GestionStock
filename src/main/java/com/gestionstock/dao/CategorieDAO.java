package com.gestionstock.dao;

import com.gestionstock.database.DatabaseConnection;
import com.gestionstock.model.Categorie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO {

    public void ajouter(Categorie categorie) {
        String sql = "INSERT INTO categorie (libelle) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, categorie.getLibelle());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                categorie.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Erreur ajout catégorie : " + e.getMessage());
        }
    }

    public List<Categorie> getAll() {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT * FROM categorie ORDER BY libelle";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categorie cat = new Categorie();
                cat.setId(rs.getInt("id"));
                cat.setLibelle(rs.getString("libelle"));
                categories.add(cat);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lecture catégories : " + e.getMessage());
        }
        return categories;
    }

    public void modifier(Categorie categorie) {
        String sql = "UPDATE categorie SET libelle = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categorie.getLibelle());
            pstmt.setInt(2, categorie.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur modification catégorie : " + e.getMessage());
        }
    }

    public void supprimer(int id) {
        String sql = "DELETE FROM categorie WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur suppression catégorie : " + e.getMessage());
            throw new RuntimeException("Catégorie utilisée par des produits");
        }
    }
}
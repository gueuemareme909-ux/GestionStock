package com.gestionstock.controller;

import com.gestionstock.dao.CategorieDAO;
import com.gestionstock.dao.ProduitDAO;
import com.gestionstock.model.Categorie;
import com.gestionstock.model.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ProduitController implements Initializable {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrix;
    @FXML private TextField txtQuantite;
    @FXML private ComboBox<Categorie> comboCategorie;

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnNouveau;

    @FXML private TextField txtRecherche;
    @FXML private Button btnRechercher;

    @FXML private TableView<Produit> tableProduits;
    @FXML private TableColumn<Produit, Integer> colId;
    @FXML private TableColumn<Produit, String> colNom;
    @FXML private TableColumn<Produit, Double> colPrix;
    @FXML private TableColumn<Produit, Integer> colQuantite;
    @FXML private TableColumn<Produit, String> colCategorie;

    @FXML private TextField txtNomCategorie;
    @FXML private Button btnAjouterCategorie;
    @FXML private Button btnModifierCategorie;
    @FXML private Button btnSupprimerCategorie;
    @FXML private TableView<Categorie> tableCategories;
    @FXML private TableColumn<Categorie, Integer> colIdCategorie;
    @FXML private TableColumn<Categorie, String> colLibelleCategorie;

    private ProduitDAO produitDAO;
    private CategorieDAO categorieDAO;

    private ObservableList<Produit> listeProduits = FXCollections.observableArrayList();
    private ObservableList<Categorie> listeCategories = FXCollections.observableArrayList();

    private Produit produitSelectionne;
    private Categorie categorieSelectionnee;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        produitDAO = new ProduitDAO();
        categorieDAO = new CategorieDAO();

        // Colonnes produits
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("nomCategorie"));

        // Colonnes catégories
        colIdCategorie.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLibelleCategorie.setCellValueFactory(new PropertyValueFactory<>("libelle"));

        // 🔥 LIAISON DES LISTES (IMPORTANT)
        comboCategorie.setItems(listeCategories);
        tableCategories.setItems(listeCategories);
        tableProduits.setItems(listeProduits);

        // Chargement initial
        chargerCategories();
        chargerProduits();

        // Sélection produit
        tableProduits.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        produitSelectionne = newSelection;
                        remplirChampsProduit(newSelection);
                    }
                }
        );

        // Sélection catégorie
        tableCategories.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        categorieSelectionnee = newSelection;
                        txtNomCategorie.setText(newSelection.getLibelle());
                    }
                }
        );

        // Boutons produits
        btnAjouter.setOnAction(e -> ajouterProduit());
        btnModifier.setOnAction(e -> modifierProduit());
        btnSupprimer.setOnAction(e -> supprimerProduit());
        btnNouveau.setOnAction(e -> viderChampsProduit());
        btnRechercher.setOnAction(e -> rechercherProduit());

        // Boutons catégories
        btnAjouterCategorie.setOnAction(e -> ajouterCategorie());
        btnModifierCategorie.setOnAction(e -> modifierCategorie());
        btnSupprimerCategorie.setOnAction(e -> supprimerCategorie());

        // 🔥 Mise à jour automatique du ComboBox
        comboCategorie.setOnShowing(e -> chargerCategories());
    }

    // ================= PRODUITS =================

    private void chargerProduits() {
        listeProduits.clear();
        listeProduits.addAll(produitDAO.getAll());
    }

    private void remplirChampsProduit(Produit p) {
        txtNom.setText(p.getNom());
        txtPrix.setText(String.valueOf(p.getPrix()));
        txtQuantite.setText(String.valueOf(p.getQuantite()));

        for (Categorie c : listeCategories) {
            if (c.getId() == p.getIdCategorie()) {
                comboCategorie.setValue(c);
                break;
            }
        }
    }

    private void ajouterProduit() {
        if (!validerChampsProduit()) return;

        Produit p = new Produit();
        p.setNom(txtNom.getText().trim());
        p.setPrix(Double.parseDouble(txtPrix.getText()));
        p.setQuantite(Integer.parseInt(txtQuantite.getText()));
        p.setIdCategorie(comboCategorie.getValue().getId());

        produitDAO.ajouter(p);
        chargerProduits();
        viderChampsProduit();

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Produit ajouté !");
    }

    private void modifierProduit() {
        if (produitSelectionne == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Sélectionnez un produit");
            return;
        }

        if (!validerChampsProduit()) return;

        produitSelectionne.setNom(txtNom.getText().trim());
        produitSelectionne.setPrix(Double.parseDouble(txtPrix.getText()));
        produitSelectionne.setQuantite(Integer.parseInt(txtQuantite.getText()));
        produitSelectionne.setIdCategorie(comboCategorie.getValue().getId());

        produitDAO.modifier(produitSelectionne);
        chargerProduits();
        viderChampsProduit();

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Produit modifié !");
    }

    private void supprimerProduit() {
        if (produitSelectionne == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Sélectionnez un produit");
            return;
        }

        produitDAO.supprimer(produitSelectionne.getId());
        chargerProduits();
        viderChampsProduit();

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Produit supprimé !");
    }

    private void rechercherProduit() {
        String recherche = txtRecherche.getText().trim();

        if (recherche.isEmpty()) {
            chargerProduits();
        } else {
            listeProduits.clear();
            listeProduits.addAll(produitDAO.rechercherParNom(recherche));
        }
    }

    private boolean validerChampsProduit() {
        if (txtNom.getText().isEmpty() || comboCategorie.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs obligatoires");
            return false;
        }
        return true;
    }

    private void viderChampsProduit() {
        txtNom.clear();
        txtPrix.clear();
        txtQuantite.clear();
        comboCategorie.setValue(null);
        produitSelectionne = null;
    }

    // ================= CATEGORIES =================

    private void chargerCategories() {
        listeCategories.clear();
        listeCategories.addAll(categorieDAO.getAll());
    }

    private void ajouterCategorie() {
        String nom = txtNomCategorie.getText().trim();
        if (nom.isEmpty()) return;

        Categorie cat = new Categorie();
        cat.setLibelle(nom);

        categorieDAO.ajouter(cat);
        chargerCategories();
        txtNomCategorie.clear();

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Catégorie ajoutée !");
    }

    private void modifierCategorie() {
        if (categorieSelectionnee == null) return;

        categorieSelectionnee.setLibelle(txtNomCategorie.getText());
        categorieDAO.modifier(categorieSelectionnee);
        chargerCategories();

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Catégorie modifiée !");
    }

    private void supprimerCategorie() {
        if (categorieSelectionnee == null) return;

        categorieDAO.supprimer(categorieSelectionnee.getId());
        chargerCategories();

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Catégorie supprimée !");
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
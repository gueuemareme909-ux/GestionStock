package com.gestionstock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/gestionstock/view/produit.fxml")
        );
        Scene scene = new Scene(loader.load(), 1000, 800);

        primaryStage.setTitle("Gestion de Stock");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("✅ Application démarrée !");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
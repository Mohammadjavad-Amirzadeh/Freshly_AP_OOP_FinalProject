package com.example.freshly;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RunAdminPage extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file for the new scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminPage.fxml"));
        Parent root = loader.load();//ProductPage

        // Create a new scene with the loaded FXML file as the root node
        Scene scene = new Scene(root);

        // Set the new scene as the root of the primary stage
        primaryStage.setScene(scene);

        // Show the primary stage with the new scene
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
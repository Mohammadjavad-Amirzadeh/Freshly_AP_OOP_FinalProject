package com.example.freshly;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GoToCostumerPage {
    public static void goToCostumerPage(){
        try {
            Parent root = FXMLLoader.load(GoToCostumerPage.class.getResource("CostumerPage.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setTitle("Login Page");

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

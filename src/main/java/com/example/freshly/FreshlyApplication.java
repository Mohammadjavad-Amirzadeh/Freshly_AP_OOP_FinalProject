package com.example.freshly;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class FreshlyApplication extends Application {
    static File file;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file for the new scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("intro.fxml"));
        Parent root = loader.load();//ProductPage

        // Create a new scene with the loaded FXML file as the root node
        Scene scene = new Scene(root);

        // Set the new scene as the root of the primary stage
        primaryStage.setScene(scene);

        // Show the primary stage with the new scene
        primaryStage.show();
    }

    public static void main(String[] args) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        file=new File("src/main/resources/com/example/freshly/Assets/Relax Music - Nicmusic -009.wav");
        AudioInputStream audioInputStream= AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
        launch();
    }
}
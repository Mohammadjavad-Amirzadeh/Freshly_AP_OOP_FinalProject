package com.example.freshly;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class AuctionLabel {

    @FXML
    private Label AuctionRemainingDays;

    @FXML
    private ImageView ProductImage;

    @FXML
    private Label ProductName;

    @FXML
    private Label ProductPrice;

    @FXML
    private Label ProductSeller;

    Product product;

    public void setData(Product product){
        this.product = product;
        ProductName.setText(product.getName());
        ProductPrice.setText(String.valueOf(product.getPrice()));
        ProductSeller.setText(product.getProductSeller());
        String path = "File:" + product.getImage();
        Image image = new Image(path, 246, 223, false, true);
        ProductImage.setImage(image);
    }
    public void setData2(String t){
        AuctionRemainingDays.setText(t);
    }

    public void OpenAuctionPage(){
        ProductSeller.getScene().getWindow().hide();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AuctionPage.fxml"));
            Parent root = loader.load();
            AuctionPage auctionPage = loader.getController();
            auctionPage.setData(this.product);
            auctionPage.serData2(AuctionRemainingDays.getText());


            Stage primaryStage = new Stage();

            // Create a new scene with the loaded FXML file as the root node
            Scene scene = new Scene(root);

            // Set the new scene as the root of the primary stage
            primaryStage.setScene(scene);

            // Show the primary stage with the new scene
            primaryStage.show();


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}

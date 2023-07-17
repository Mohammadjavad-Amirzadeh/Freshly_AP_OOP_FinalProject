package com.example.freshly;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ProductLabelInHistory {

    @FXML
    private Label CountLabel;

    @FXML
    private ImageView ImageProduct;

    @FXML
    private Pane PaneAfterClickingBuyingButton;

    @FXML
    private Label PriceLabel;

    @FXML
    private Label ProductLabel;

    @FXML
    private Label SellerName;

    private Product product;
    private Image image;


    public void setData(Product product){
        this.product=product;
        ProductLabel.setText(product.getName());
        SellerName.setText(product.getProductSeller());
        PriceLabel.setText(String.valueOf(product.getPrice()));
        String path = "File:" + product.getImage();
        image = new Image(path, 246, 223, false, true);
        ImageProduct.setImage(image);
        CountLabel.setText(String.valueOf(product.getStock()));
    }


}

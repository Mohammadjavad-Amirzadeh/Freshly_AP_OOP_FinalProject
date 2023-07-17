package com.example.freshly;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.w3c.dom.events.MouseEvent;

public class ProductLabelInAnbar {

    @FXML
    private Label ProductID;

    @FXML
    private ImageView ProductImage;

    @FXML
    private Label ProductName;

    public Product MyProduct;


    public void setData(Product product){
        MyProduct = product;
        ProductID.setText(String.valueOf(product.getProductId()));
        ProductName.setText(product.getName());
        String path = "File:" + product.getImage();
        Image image = new Image(path, 246, 223, false, true);
        ProductImage.setImage(image);
    }

    public void getData(){
        SellerPage.name = MyProduct.getName();
        SellerPage.price = String.valueOf(MyProduct.getPrice());
    }

    @FXML
    private void handleClick(MouseEvent event) {
        SellerPage.name = ProductID.getText();
        SellerPage.price = ProductName.getText();
    }

}
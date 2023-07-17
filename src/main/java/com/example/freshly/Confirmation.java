package com.example.freshly;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Confirmation {

    @FXML
    private Label CompanyNameText;

    @FXML
    private Button ConfirmButton;

    @FXML
    private Label EmailText;

    @FXML
    private Label FamilyText;

    @FXML
    private Label NameText;

    @FXML
    private Label PhoneNumberText;

    @FXML
    private Button RefuseButton;

    @FXML
    private Label UsernameText;

    private Seller seller;

    public void setData(Seller seller){
        this.seller=seller;
        NameText.setText(seller.getFirstname());
        FamilyText.setText(seller.getLastname());
        PhoneNumberText.setText(seller.getPhoneNumber());
        UsernameText.setText(seller.getUsername());
        EmailText.setText(seller.getEmailAddress());
        CompanyNameText.setText(seller.getCompany());
    }
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    public void confirm(ActionEvent event){
        connection=database.connectDB();
        try {
            statement = connection.createStatement();
            statement.executeUpdate("UPDATE seller SET varified = 'true' WHERE Username = '"+seller.getUsername()+"'");
            PhoneNumberText.getScene().getWindow().hide();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("AdminPage.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("Main Page");

                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void refuse(ActionEvent actionEvent){
        connection=database.connectDB();
        try {
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM seller WHERE Username = '"+seller.getUsername()+"'");
            PhoneNumberText.getScene().getWindow().hide();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("AdminPage.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("Main Page");

                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

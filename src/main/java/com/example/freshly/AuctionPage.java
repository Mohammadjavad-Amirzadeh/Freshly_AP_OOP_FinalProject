package com.example.freshly;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class AuctionPage implements BackToMainPage{

    @FXML
    private Label BrandName;

    @FXML
    private Label BrandProduct;

    @FXML
    private Label BrandProductUnder;

    @FXML
    private TextField CostumerPrice;

    @FXML
    private ImageView FreshlyHeader;

    @FXML
    private Pane Koll1;

    @FXML
    private ImageView ProductImage;

    @FXML
    private Label ProductPrice;

    @FXML
    private Label ProductRate;

    @FXML
    private Label RemainingDays;

    @FXML
    private TextField SearchTextField;

    @FXML
    private Label TypeProduct;

    @FXML
    private Button addToCart;

    @FXML
    private Pane loginedPane;

    @FXML
    private Label productSeller;

    @FXML
    private ScrollPane scroll;

    @FXML
    private AnchorPane scrollPane;

    @FXML
    private Pane unLoginedPane;

    Product product;

    private Connection connection;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet resultSet;
    private ResultSet result;
    private int formerprice;

    protected ObservableList<Product> cardListData = FXCollections.observableArrayList();
    protected ObservableList<Product> cardListDataTemp = FXCollections.observableArrayList();
    protected ObservableList<Product> allProducts = FXCollections.observableArrayList();


    public void setData(Product product){
        this.product=product;
        BrandName.setText(product.getName());
        BrandProduct.setText(product.getBrand());
        BrandProductUnder.setText(product.getBrand());
        TypeProduct.setText(product.getType());
        ProductRate.setText(String.valueOf(product.getPoint()));
        connection = database.connectDB();
        try {
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT auctionprice FROM auction WHERE productid = '"+product.getProductId()+"'");
            String s="";
            if (result.next()){
                ProductPrice.setText(result.getString("auctionprice"));
                formerprice=Integer.valueOf(ProductPrice.getText());
            }else {
                ProductPrice.setText("0");
                formerprice=Integer.valueOf(ProductPrice.getText());
            }
        } catch (SQLException e) {
            System.out.println("r");
        }

        String path = "File:" + product.getImage();
        Image image = new Image(path, 246, 223, false, true);
        ProductImage.setImage(image);
        productSeller.setText(product.getProductSeller());
    }
    public void serData2(String s){
        RemainingDays.setText(s);
    }

    public void UpdateAuctionPrice(String price){

    }

    public void CheckEnteredPrice() throws SQLException {
        String enteredPrice = CostumerPrice.getText();
        if (!CostumerPrice.getText().isEmpty()){
            double entered = Double.parseDouble(enteredPrice);
            if (MainPage.customer.getWallet() >= entered){
                if (Double.parseDouble(ProductPrice.getText()) < entered){
                    ProductPrice.setText(enteredPrice);
                    connection = database.connectDB();
                    statement = connection.createStatement();
                    String formerWinner = "";
                    result = statement.executeQuery("SELECT winner FROM auction WHERE productid = '"+product.getProductId()+"'");
                    if (result.next()){
                        formerWinner = result.getString("winner");
                    }
                    String wallet = "";
                    statement = connection.createStatement();
                    if (!formerWinner.equals("")){
                        result = statement.executeQuery("SELECT wallet FROM costumer WHERE Username = '"+formerWinner+"'");
                        if (result.next()){
                            wallet = result.getString("wallet");
                        }
                        statement = connection.createStatement();
                        int newWallet = Integer.parseInt(wallet);
                        int formerPrice = formerprice;
                        newWallet += formerPrice;
                        statement = connection.createStatement();
                        statement.executeUpdate("UPDATE costumer SET wallet = '"+newWallet+"' WHERE Username = '"+formerWinner+"'");

                    }


                    statement.executeUpdate("UPDATE auction SET auctionprice = '"+enteredPrice+"' , winner = '"+MainPage.customer.getUsername()+"' WHERE productid = '"+product.getProductId()+"'");
                    statement = connection.createStatement();
                    MainPage.customer.setWallet(MainPage.customer.getWallet()-Integer.parseInt(enteredPrice));
                    statement.executeUpdate("UPDATE costumer SET wallet = '"+MainPage.customer.getWallet()+"'WHERE Username = '"+MainPage.customer.getUsername()+"'");
                }
            }else {
                System.out.println("namonaseb");
            }
        }
    }

    @FXML
    void AddingToCostumerCart(ActionEvent event) {

    }

    @FXML
    void FixSearchTextField(MouseEvent event) {

    }

    @FXML
    void openCart(MouseEvent event) {
        if (MainPage.customer == null){
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("You didnt Login in to your account , Do you want to login ?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    loginedPane.getScene().getWindow().hide();

                    // LINK YOUR LOGIN FORM AND SHOW IT
                    Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    stage.setTitle("Login Page");
                    /*stage.setOnHiding(new EventHandler<WindowEvent>() {
                        public void handle(WindowEvent we) {
                            stage.hide();
                 @FXML
    void searchingClick(MouseEvent event) {
            ShowingProducts.name = SearchTextField.getText();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("ShowingProducts.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("Login Page");
                headerMainPage.getScene().getWindow().hide();

                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }           // نمایش پنجره قبلی
                            stage.getOwner().showingProperty();
                        }
                    });*/


                    stage.setScene(scene);
                    stage.show();

                }
                if (option.get().equals(ButtonType.NO)){
                    alert.close();
                }
                /*
                 */
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            Parent root = null;
            try {
                loginedPane.getScene().getWindow().hide();
                root = FXMLLoader.load(getClass().getResource("CartPage.fxml"));
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

    @FXML
    void searching(KeyEvent event) {
        if(String.valueOf(event.getCode()).equals("ENTER")){
            ShowingProducts.name = SearchTextField.getText();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("ShowingProducts.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("Login Page");
                BrandName.getScene().getWindow().hide();

                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @FXML
    void searchingClick(MouseEvent event) {
        ShowingProducts.name = SearchTextField.getText();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("ShowingProducts.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setTitle("Login Page");
            BrandName.getScene().getWindow().hide();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void openLoginPage(MouseEvent event) {

    }

    public void returnClicked(MouseEvent event){
        Return();
    }

    @Override
    public void Return() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setTitle("Main Page");
            loginedPane.getScene().getWindow().hide();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void goToCostumerPage(){
        BrandName.getScene().getWindow().hide();
        GoToCostumerPage.goToCostumerPage();
    }


}

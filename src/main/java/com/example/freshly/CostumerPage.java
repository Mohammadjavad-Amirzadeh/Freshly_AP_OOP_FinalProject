package com.example.freshly;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class CostumerPage implements Initializable , BackToMainPage{

    @FXML
    private GridPane AuctionGridPane;

    @FXML
    private ImageView AuctionLine;

    @FXML
    private ScrollPane AuctionScroll;

    @FXML
    private GridPane BuyingGridPane;

    @FXML
    private Label BuyingHistoryButton;

    @FXML
    private ScrollPane BuyingHistoryScrollPane;

    @FXML
    private ImageView CartLine;

    @FXML
    private TextField enteredAmount;

    @FXML
    private Pane CartPane;

    @FXML
    private ImageView FreshlyHeader;

    @FXML
    private ImageView HistoryLine;

    @FXML
    private ImageView OffCode;

    @FXML
    private Label OffCodeButton;

    @FXML
    private GridPane OffCodeGrid;

    @FXML
    private ScrollPane OffCodesScroll;

    @FXML
    private Pane PaymentOpening;

    @FXML
    private Label PersonalInformationButton;

    @FXML
    private Label PersonalInformationEmail;

    @FXML
    private Label PersonalInformationFamily;

    @FXML
    private Label PersonalInformationName;

    @FXML
    private Pane PersonalInformationPane;

    @FXML
    private Label PersonalInformationPassword;

    @FXML
    private Label PersonalInformationPhoneNumber;

    @FXML
    private Label PersonalInformationUsername;

    @FXML
    private ImageView PersonalLine;

    @FXML
    private TextField SearchTextField;

    @FXML
    private Pane loginedPane;

    @FXML
    private ScrollPane scroll;

    @FXML
    private AnchorPane scrollPane;

    @FXML
    private Pane unLoginedPane;

    @FXML
    private Label wallet;

    private Alert alert;

    public static boolean swWallet;

    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet result;
    private ObservableList<Product> historyCard = FXCollections.observableArrayList();
    private ObservableList<String> offCodes=FXCollections.observableArrayList();

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

    public void returnClicked(MouseEvent event){
        Return();
    }


    public ObservableList<Product> GetProductsList() throws IOException {
        String sql = "SELECT * FROM product";
        HashMap<String ,String > temp = readHistory();
        ObservableList<Product> listData = FXCollections.observableArrayList();
        connection = database.connectDB();

        try {
            if (connection != null) {
                prepare = connection.prepareStatement(sql);
            }
            result = prepare.executeQuery();

            Product prod;

            while (result.next()) {
                String productid = result.getString("productid");
                prod = new Product(result.getString("name"),
                        result.getString("brand"),
                        Double.parseDouble(result.getString("point")),
                        Integer.parseInt(result.getString("numberofpoints")),
                        Integer.parseInt(result.getString("productid")),
                        result.getString("information"),
                        Double.parseDouble(result.getString("price")),
                        result.getString("type"),
                        result.getString("image"),
                        Integer.parseInt(result.getString("stock")),
                        readCommentOfProductFromDataBase.commentOfProduct(productid),
                        result.getString("productSeller"));

                if (temp.containsKey(String.valueOf(prod.getProductId()))){
                    prod.setStock(Integer.parseInt(temp.get(String.valueOf(prod.getProductId()))));
                    listData.add(prod);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < listData.size(); i++) {
            System.out.println(listData.get(i).getProductId());
        }

        return listData;
    }

    public HashMap<String,String> readHistory(){
        HashMap<String,String> historyProductId = new HashMap<>();
        String history = "";

        connection = database.connectDB();
        String sql = "SELECT history FROM costumer WHERE Username = '"+MainPage.customer.getUsername()+"'";
        try {
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()){
                history = result.getString("history");
            }
            System.out.println(history
            );
            String [] prodAndCounts = history.split("@");
            if (prodAndCounts.length > 1) {
                for (String prodAndCount : prodAndCounts) {
                    String[] cur = prodAndCount.split("_");
                    if(historyProductId.containsKey(cur[0])){
                        int t= Integer.parseInt(historyProductId.get(cur[0]));
                        t+=Integer.parseInt(cur[1]);
                        historyProductId.put(cur[0], String.valueOf(t));
                    }
                    else
                        historyProductId.put(cur[0], cur[1]);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return historyProductId;
    }

    public void setBuyingGridPane() throws IOException {
        historyCard.clear();
        historyCard.addAll(GetProductsList());

        int row = 0;
        int column = 0;

        BuyingGridPane.getChildren().clear();
        BuyingGridPane.getRowConstraints().clear();
        BuyingGridPane.getColumnConstraints().clear();

        for (int q = 0; q < historyCard.size(); q++) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("ProductLabelInHistory.fxml"));
                Pane pane = load.load();
                ProductLabelInHistory Pl = load.getController();
                Pl.setData(historyCard.get(q));

                if (column == 1) {
                    column = 0;
                    row += 1;
                }

                BuyingGridPane.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(1));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setOffCodesGridpane(){
        offCodes.clear();
        offCodes.addAll(MainPage.customer.getDiscountCode());

        int row = 0;
        int column = 0;

        OffCodeGrid.getChildren().clear();
        OffCodeGrid.getRowConstraints().clear();
        OffCodeGrid.getColumnConstraints().clear();

        for (int q = 0; q < offCodes.size(); q++) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("OffCodeLabel.fxml"));
                Pane pane = load.load();
                OffCodeLabel Pl = load.getController();
                Pl.setData(offCodes.get(q));

                if (column == 1) {
                    column = 0;
                    row += 1;
                }

                OffCodeGrid.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(1));

            } catch (Exception e) {
                e.printStackTrace();
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
                loginedPane.getScene().getWindow().hide();

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
                loginedPane.getScene().getWindow().hide();

                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    @FXML
    void openCart(MouseEvent event) {
        if (MainPage.customer == null){
            try {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
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
                            // نمایش پنجره قبلی
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

    public void FixSearchTextField(MouseEvent event){
        SearchTextField.requestFocus();
    }

    public void openLoginPage(MouseEvent mouseEvent){
        System.out.println("going to login page");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setTitle("Login Page");
            loginedPane.getScene().getWindow().hide();

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void ShowPersonalInformation(){
        BuyingHistoryScrollPane.setVisible(false);
        PersonalInformationPane.setVisible(true);
        PersonalLine.setVisible(true);
        HistoryLine.setVisible(false);
        OffCode.setVisible(false);
        OffCodesScroll.setVisible(false);
        AuctionScroll.setVisible(false);
        AuctionLine.setVisible(false);
        CartPane.setVisible(false);
        CartLine.setVisible(false);
    }

    public void ShowBuyingHistory(){
        BuyingHistoryScrollPane.setVisible(true);
        PersonalInformationPane.setVisible(false);
        HistoryLine.setVisible(true);
        PersonalLine.setVisible(false);
        OffCode.setVisible(false);
        OffCodesScroll.setVisible(false);
        AuctionScroll.setVisible(false);
        AuctionLine.setVisible(false);
        CartPane.setVisible(false);
        CartLine.setVisible(false);
    }

    public void showOffCodes(){
        BuyingHistoryScrollPane.setVisible(false);
        PersonalInformationPane.setVisible(false);
        HistoryLine.setVisible(false);
        PersonalLine.setVisible(false);
        OffCode.setVisible(true);
        OffCodesScroll.setVisible(true);
        AuctionScroll.setVisible(false);
        AuctionLine.setVisible(false);
        CartPane.setVisible(false);
        CartLine.setVisible(false);
    }

    public void showAuctions(){
        BuyingHistoryScrollPane.setVisible(false);
        PersonalInformationPane.setVisible(false);
        HistoryLine.setVisible(false);
        PersonalLine.setVisible(false);
        OffCode.setVisible(false);
        OffCodesScroll.setVisible(false);
        AuctionScroll.setVisible(true);
        AuctionLine.setVisible(true);
        CartPane.setVisible(false);
        CartLine.setVisible(false);
    }

    public void showCart(){
        BuyingHistoryScrollPane.setVisible(false);
        PersonalInformationPane.setVisible(false);
        HistoryLine.setVisible(false);
        PersonalLine.setVisible(false);
        OffCode.setVisible(false);
        OffCodesScroll.setVisible(false);
        AuctionScroll.setVisible(false);
        AuctionLine.setVisible(false);
        CartPane.setVisible(true);
        CartLine.setVisible(true);
    }

    public void showPaymentPane(){
        FadeTransition transition = new FadeTransition(Duration.seconds(1), PaymentOpening);
        transition.setFromValue(0.0);
        transition.setToValue(1.0);

        PaymentOpening.setOpacity(0.0);
        PaymentOpening.setVisible(true);

        transition.play();
    }

    private void setWallet(){
        wallet.setText(String.valueOf(MainPage.customer.getWallet()));
    }

    public void openPaymentGateway(ActionEvent event) throws IOException {
        if (!enteredAmount.getText().isEmpty()) {

            PaymentGateway.finalPrice = enteredAmount.getText();

            Parent root = FXMLLoader.load(getClass().getResource("PaymentGateway.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setTitle("Login Page");
            loginedPane.getScene().getWindow().hide();

            stage.setScene(scene);
            stage.show();
        }
        else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("please enter amount");
            alert.showAndWait();
        }
    }

    public void setPersonalInformation(){
        PersonalInformationName.setText(MainPage.customer.getFirstname());
        PersonalInformationFamily.setText(MainPage.customer.getLastname());
        PersonalInformationEmail.setText(MainPage.customer.getEmailAddress());
        PersonalInformationUsername.setText(MainPage.customer.getUsername());
        PersonalInformationPassword.setText(MainPage.customer.getPassword());
        PersonalInformationPhoneNumber.setText(MainPage.customer.getPhoneNumber());
    }

    protected ObservableList<Auction> AuctionListData;

    public ObservableList<Auction> getAuctionList(){
        String sql = "SELECT * FROM auction";
        ObservableList<Auction> listData = FXCollections.observableArrayList();
        connection = database.connectDB();

        try {
            if (connection != null) {
                prepare = connection.prepareStatement(sql);
            }
            result = prepare.executeQuery();

            Auction auction;

            while (result.next()) {
                String productid = result.getString("productid");
                auction = new Auction(
                        result.getString("productid"),
                        result.getString("remainingdays"),
                        result.getString("auctionprice")
                );
                listData.add(auction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    protected ObservableList<Product> ProductListInAuction;

    public ObservableList<Product> getAllProducts(){
        String sql = "SELECT * FROM product";
        ObservableList<Product> listData = FXCollections.observableArrayList();
        connection = database.connectDB();

        try {
            if (connection != null) {
                prepare = connection.prepareStatement(sql);
            }
            result = prepare.executeQuery();

            Product prod;

            while (result.next()) {
                String productid = result.getString("productid");
                prod = new Product(result.getString("name"),
                        result.getString("brand"),
                        Double.parseDouble(result.getString("point")),
                        Integer.parseInt(result.getString("numberofpoints")),
                        Integer.parseInt(result.getString("productid")),
                        result.getString("information"),
                        Double.parseDouble(result.getString("price")),
                        result.getString("type"),
                        result.getString("image"),
                        Integer.parseInt(result.getString("stock")),
                        null,
                        result.getString("productSeller"));

                listData.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    public void setAuctionGridPane(){
        AuctionListData = getAuctionList();
        ProductListInAuction = getAllProducts();
        AuctionGridPane.getChildren().clear();
        AuctionGridPane.getRowConstraints().clear();
        AuctionGridPane.getColumnConstraints().clear();
        int column = 0;
        int row = 0;
        for (int q = 0; q < AuctionListData.size(); q++) {
            for (int i = 0 ; i<ProductListInAuction.size() ; i++){
                if (AuctionListData.get(q).getProductId().equals(String.valueOf(ProductListInAuction.get(i).getProductId()))){
                    try {
                        FXMLLoader load = new FXMLLoader();
                        load.setLocation(getClass().getResource("AuctionLabel.fxml"));
                        Pane pane = load.load();
                        AuctionLabel Pl = load.getController();
                        Pl.setData(ProductListInAuction.get(i));
                        Pl.setData2(AuctionListData.get(q).getRemainingDays());

                        if (column == 1) {
                            column = 0;
                            row += 1;
                        }

                        AuctionGridPane.add(pane, column++, row);

                        GridPane.setMargin(pane, new Insets(1));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        OffCodesScroll.setStyle("-fx-background-color: white");
        OffCodeGrid.setStyle("-fx-background-color: white");
        BuyingHistoryScrollPane.setStyle("-fx-background-color: white");
        BuyingGridPane.setStyle("-fx-background-color: white");
        AuctionScroll.setStyle("-fx-background-color: white");
        AuctionGridPane.setStyle("-fx-background-color: white");
        setAuctionGridPane();
        wallet.setText(String.valueOf(MainPage.customer.getWallet()));
        try {
            setBuyingGridPane();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setOffCodesGridpane();
        if (swWallet){
            showCart();
            setWallet();
        }else ShowPersonalInformation();
        loginedPane.setVisible(true);
        unLoginedPane.setVisible(false);
        setPersonalInformation();
    }
}

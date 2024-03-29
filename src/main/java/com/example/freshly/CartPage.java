package com.example.freshly;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class CartPage implements Initializable,BackToMainPage {

    @FXML
    private Button ConfirmCart;

    @FXML
    private Label FinalPriceLabel;

    @FXML
    private Label FirstPriceLabel;

    @FXML
    private TextField OffCodeTextField;

    @FXML
    private GridPane ProductsGridPane;

    @FXML
    private TextField SearchTextField;

    @FXML
    private Pane loginedPane;

    @FXML
    private AnchorPane scrollPane;

    @FXML
    private Pane unLoginedPane;

    private Connection connection;
    private ResultSet result;
    private PreparedStatement prepare;

    public static String usedDiscountCode="";
    public static String finalUsedDiscountCode="";


    private Alert alert;
    private boolean sw;

    private ObservableList<Product> cardOfCart = FXCollections.observableArrayList();
    private HashMap<String,String> cart = new HashMap<>();

    private ArrayList<String> noStock = new ArrayList<>();

    @FXML
    void FixSearchTextField(MouseEvent event) {

    }

    @FXML
    void openLoginPage(MouseEvent event) {

    }


    public void setProductsGridPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductLabelInCard.fxml"));
        Object controller = loader.getController();
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

// Add the root node to the GridPane at column 0, row 0
        ProductsGridPane.add(root, 1, 1);
    }

    public ObservableList<String> GetCartList(){
        return readCartAndDiscountCodeOfCostumerFromDatabase.cartOfCostumer(MainPage.customer.getUsername());
    }
    private ObservableList<Product> IdToProduct(ObservableList<String> GetCartList){
        ObservableList<String> data = GetCartList;
        ObservableList<Product> products = FXCollections.observableArrayList();
        if (!data.isEmpty()) {
            for (String datum : data) {
                String[] infos = datum.split(separator());
                cart.put(infos[0], infos[1]);
            }
        }

        String sql = "SELECT * FROM product";
        connection = database.connectDB();

        try {
            if (connection != null) {
                prepare = connection.prepareStatement(sql);
            }
            result = prepare.executeQuery();

            Product prod;

            while (result.next()) {
                String productid = result.getString("productid");
                for (String cur:
                        cart.keySet()) {
                    if (cur.equals(productid)){
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
                        products.add(prod);
                    }
                }



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
    private static String separator(){
        return "_";
    }

    public void CartDisplay(boolean firstTime){

        if (firstTime) {
            cardOfCart.clear();
            cardOfCart.addAll(IdToProduct(GetCartList()));
        }


        int row = 0;
        int column = 0;

        ProductsGridPane.getChildren().clear();
        ProductsGridPane.getRowConstraints().clear();
        ProductsGridPane.getColumnConstraints().clear();

        for (int q = 0; q < cardOfCart.size(); q++) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("ProductLabelInCard.fxml"));
                Pane pane = load.load();
                ProductLabelInCard Pl = load.getController();
                Pl.setData(cardOfCart.get(q),cart.get(String.valueOf(cardOfCart.get(q).getProductId())));

                if (column == 1) {
                    column = 0;
                    row += 1;
                }

                ProductsGridPane.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(0));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void calculateCost(){
        double sum = 0;
        for (Product product : cardOfCart) {
            sum += (product.getPrice()*Integer.parseInt(cart.get(String.valueOf(product.getProductId()))));
        }
        FirstPriceLabel.setText(String.valueOf(sum));
        FinalPriceLabel.setText(String.valueOf(sum));
    }
    private String findStore(String prodId) throws SQLException {
        connection = database.connectDB();
        Statement statement=connection.createStatement();
        result = statement.executeQuery("SELECT name , products  FROM store");
        while (result.next()){
            String productIds = result.getString("products");
            String store=result.getString("name");
            String[] a=productIds.split("@");
            ArrayList<String>temp=new ArrayList<>();
            for (int i = 0; i < a.length; i++) {
                temp.add(a[i]);
            }
            if(temp.contains(prodId)){
                return store;
            }
        }
        return null;
    }
    public void finalBuy(ActionEvent event){
        checkStock();
        System.out.println(FinalPriceLabel.getText());
        if (noStock.isEmpty()) {
            if (MainPage.customer.getWallet() >= Double.parseDouble(FinalPriceLabel.getText())) {
                int wallet = (int) (MainPage.customer.getWallet() - Double.parseDouble(FinalPriceLabel.getText()));
                MainPage.customer.setWallet(wallet);
                connection = database.connectDB();

                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("UPDATE costumer SET wallet = '" + wallet + "'WHERE username = '" + MainPage.customer.getUsername() + "'");

                    for (String s:
                         cart.keySet()) {
                        String id = s;
                        String count = cart.get(s);
                        statement = connection.createStatement();
                        ResultSet resultSet =statement.executeQuery("SELECT stock FROM product WHERE productid = '"+id+"'");
                        int Stock =0;
                        if (resultSet.next()) {
                            Stock = Integer.parseInt(resultSet.getString("stock"));
                            System.out.println(Stock);
                            int count1 = Integer.parseInt(count);
                            Stock -= count1;
                            statement.executeUpdate("UPDATE product SET stock = '"+Stock+"' WHERE productid = '"+id+"'");
                        }

                        for (int i = 0; i < cardOfCart.size(); i++) {
                            if (String.valueOf(cardOfCart.get(i).getProductId()).equals(id)) {
                                cardOfCart.get(i).setStock(Stock);
                                //.
                                break;
                            }


                        }


                        System.out.println(Stock);

                        if(findStore(id)!=null){
                            String store=findStore(id);
                            statement = connection.createStatement();
                            try {
                                statement.executeUpdate("INSERT INTO  inputoutput (date,id,count,store) VALUES ('" + LocalDateTime.now() + "','" + id + "','" + "-" + count + "','" + store + "')");
                            }catch (Exception e){
                                System.out.println("ajkdshdkajfhak");
                            }
                        }


                        System.out.println();
                        Stock = 0;
                    }
                    statement = connection.createStatement();
                    result = statement.executeQuery("SELECT cart , history FROM costumer WHERE username = '" + MainPage.customer.getUsername() + "'");
                    String history = "";
                    String cart = "";
                    if (result.next()) {
                        cart = result.getString("cart");
                        history = result.getString("history");
                        history = result.getString("cart") +"@"+ history;
                    }
                    cart = "8585_1@6969_1";
                    statement = connection.createStatement();
                    statement.executeUpdate("UPDATE costumer SET cart='" + cart + "' , history = '" + history + "'  WHERE username = '" + MainPage.customer.getUsername() + "'");
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information message");
                    alert.setHeaderText(null);
                    alert.setContentText("successfully purchased");
                    Thread thread = new Thread(new EmailSender(MainPage.customer.getEmailAddress(), "Dear "+MainPage.customer.getFirstname()+" your purchased was successfully"));
                    thread.start();
                    if(!CartPage.finalUsedDiscountCode.equals("")){
                        connection=database.connectDB();
                        statement=connection.createStatement();
                        ObservableList<String> codes=MainPage.customer.getDiscountCode();
                        codes.remove(CartPage.finalUsedDiscountCode);
                        MainPage.customer.setDiscountCode(codes);
                        StringBuffer buffer=new StringBuffer();
                        for (int i = 0; i < codes.size(); i++) {
                            if(i+1<codes.size())
                                buffer.append(codes.get(i)+"@");
                            else
                                buffer.append(codes.get(i));
                        }
                        statement.executeUpdate("UPDATE costumer SET discountcode = '"+buffer+"' WHERE Username = '"+MainPage.customer.getUsername()+"'");
                    }
                    alert.showAndWait();
                    cardOfCart.clear();
                    CartDisplay(false);
                    FinalPriceLabel.setText("0");
                    FirstPriceLabel.setText("0");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }


            } else {
                try {
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("You dont have enough money in your wallet , Do you want to charge ?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        CostumerPage.swWallet = true;
                        Parent root = FXMLLoader.load(getClass().getResource("CostumerPage.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(root);

                        stage.setTitle("Login Page");
                        loginedPane.getScene().getWindow().hide();

                        stage.setScene(scene);
                        stage.show();
                    }
                    if (option.get().equals(ButtonType.NO)) {
                        alert.close();
                    }
                    /*
                     */
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < noStock.size(); i++) {
                buffer.append(noStock.get(i)+" ");
            }
            alert.setContentText("we dont have enough stock of these product : "+buffer+"");
            print();
            noStock.clear();
            alert.showAndWait();
        }
    }

    private void checkStock(){

        for (Product product : cardOfCart) {
            String prodId = String.valueOf(product.getProductId());
            int stock = Integer.parseInt(cart.get(prodId));
            if (stock <= product.getStock()) {

            } else {
                noStock.add(product.getName());
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
                ConfirmCart.getScene().getWindow().hide();

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
                ConfirmCart.getScene().getWindow().hide();

                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    @FXML
    void goToCostumerPage(){
        ConfirmCart.getScene().getWindow().hide();
        GoToCostumerPage.goToCostumerPage();
    }

    public void checkDiscountCode(MouseEvent mouseEvent){
        if (!OffCodeTextField.getText().isEmpty()) {
            ObservableList<String> discountCodes = readCartAndDiscountCodeOfCostumerFromDatabase.discountCodeOfCostumer(MainPage.customer.getUsername());
            usedDiscountCode = OffCodeTextField.getText();
            if (discountCodes.contains(usedDiscountCode)&&(!sw)){
                if (Double.parseDouble(FinalPriceLabel.getText()) >= 200_000){
                    finalUsedDiscountCode=usedDiscountCode;
                    sw=true;
                    FinalPriceLabel.setText(String.valueOf(Double.parseDouble(FinalPriceLabel.getText()) - 50000));
                }
                else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("حداقل مبلغ کل خرید برای استفاده از کد دویست هزار تومان میباشد");
                    alert.showAndWait();
                }
            }else {
                if (sw) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("you have Used Discount Code");
                    alert.showAndWait();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Discount Code");
                    alert.showAndWait();
                }
            }
        }
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

    public void print(){
        for (int i = 0; i < noStock.size(); i++) {
            System.out.println(noStock.get(i));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setProductsGridPane();
            CartDisplay(true);
            loginedPane.setVisible(true);
            unLoginedPane.setVisible(false);
            calculateCost();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < MainPage.customer.getDiscountCode().size(); i++) {
            System.out.println(MainPage.customer.getDiscountCode().get(i));
        }
    }
}

package com.example.freshly;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SellerPage implements Initializable,LoginChecker {

    @FXML
    private Button AddButton;

    @FXML
    private AnchorPane AddPane;

    @FXML
    private ImageView AddPaneImage;

    @FXML
    private GridPane AuctionGridPane;

    @FXML
    private Pane AuctionPane;

    @FXML
    private TextField BrandTextfield;

    @FXML
    private HBox ChatHbox;

    @FXML
    private Pane ChatPane;

    @FXML
    private ScrollPane ChatScroll;

    @FXML
    private TextField ChatTextField;

    @FXML
    private TextFlow ChatTextFlow;

    @FXML
    private VBox ChatVbox;

    @FXML
    private ComboBox<?> ChooseTypeComboBox;

    @FXML
    private Button ConfirmAuction;

    @FXML
    private DatePicker ExpertDatePicker;

    @FXML
    private Label ExpertDateText;

    @FXML
    private Label HealthValueText;

    @FXML
    private TextField HealthValueTextField;

    @FXML
    private TextField IDTextField;

    @FXML
    private TableColumn<?, ?> IdCol;

    @FXML
    private Label InfoCompanyName;

    @FXML
    private Label InfoEmail;

    @FXML
    private Label InfoFamily;

    @FXML
    private Label InfoName;

    @FXML
    private Pane InfoPane;

    @FXML
    private Label InfoPhoneNumber;

    @FXML
    private Label InfoUserName;

    @FXML
    private Pane MainPane;

    @FXML
    private TableColumn<?, ?> NameCol;

    @FXML
    private TextField NameTextfield;

    @FXML
    private TableColumn<?, ?> PriceCol;

    @FXML
    private TextField PriceTextfield;

    @FXML
    private TableView<?> ProductsTable;

    @FXML
    private TextField RemainingDaysTextField;

    @FXML
    private TableColumn<?, ?> StockCol;

    @FXML
    private TextField StockTextfield;

    @FXML
    private TableColumn<?, ?> TypeCol;

    @FXML
    private Label WeightText;

    @FXML
    private TextField WeightTextField;

    @FXML
    private Label sellerUsername;

    @FXML
    private Button sendButton;


    @FXML
    private Label Mammad;

    protected ObservableList<Product> cardListData = FXCollections.observableArrayList();

    private Alert alert;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private String path;
    public static Seller seller = null;
    String pattern = "^[0-9]+$";
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Socket socket;
    private int port = 1234;
    private String hostIp = "localhost";
    private Client client;

    public static String name = "temp";
    public static String price = "temp";

    public void SetChooseRole() {
        String[] RoleList = {"کالاهای اساسی و خواروبار", "مواد پروتئینی", "نوشیدنی", "تنقلات", "لبنیات", "سایر"};
        List<String> ItemList = new ArrayList<>();

        for (String data : RoleList) {
            ItemList.add(data);
        }

        ObservableList DataList = FXCollections.observableArrayList(ItemList);
        ChooseTypeComboBox.setItems(DataList);
    }

    public void showRelatedField() {
        if (ChooseTypeComboBox.getValue().equals("لبنیات") || ChooseTypeComboBox.getValue().equals("مواد پروتئینی")) {
            ExpertDateText.setVisible(true);
            ExpertDatePicker.setVisible(true);
            HealthValueText.setVisible(false);
            HealthValueTextField.setVisible(false);
            WeightText.setVisible(false);
            WeightTextField.setVisible(false);
        }
        if (ChooseTypeComboBox.getValue().equals("تنقلات")) {
            HealthValueText.setVisible(true);
            HealthValueTextField.setVisible(true);
            WeightText.setVisible(false);
            WeightTextField.setVisible(false);
            ExpertDatePicker.setVisible(false);
            ExpertDateText.setVisible(false);
        }
        if (ChooseTypeComboBox.getValue().equals("کالاهای اساسی و خواروبار")) {
            WeightText.setVisible(true);
            WeightTextField.setVisible(true);
            ExpertDateText.setVisible(false);
            ExpertDatePicker.setVisible(false);
            HealthValueTextField.setVisible(false);
            HealthValueText.setVisible(false);
        }
        if (ChooseTypeComboBox.getValue().equals("نوشیدنی")) {
            WeightText.setVisible(false);
            WeightTextField.setVisible(false);
            ExpertDateText.setVisible(false);
            ExpertDatePicker.setVisible(false);
            HealthValueTextField.setVisible(false);
            HealthValueText.setVisible(false);
        }
        if (ChooseTypeComboBox.getValue().equals("سایر")) {
            WeightText.setVisible(false);
            WeightTextField.setVisible(false);
            ExpertDateText.setVisible(false);
            ExpertDatePicker.setVisible(false);
            HealthValueTextField.setVisible(false);
            HealthValueText.setVisible(false);
        }
    }

    public void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image file", "*png", "*jpg"));

        File file = fileChooser.showOpenDialog(MainPane.getScene().getWindow());
        path = file.getAbsolutePath();
        if (file != null) {
            Image image = new Image(file.toURI().toString(), 110, 121, false, true);
            AddPaneImage.setImage(image);
        }
    }
    private void clearTextFields(){
        NameTextfield.setText("");
        BrandTextfield.setText("");
        PriceTextfield.setText("");
        PriceTextfield.setText("");
        StockTextfield.setText("");
        //ChooseTypeComboBox.getSelectionModel().clearSelection();
        path="";
        AddPaneImage.setImage(null);
    }

    public void addBtn() {
        if (checkEmptyFields()) {
            if (stockAndPriceRegex(PriceTextfield.getText()) || stockAndPriceRegex(StockTextfield.getText())) {
                AddProductToDataBase();
                clearTextFields();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect Data");
                alert.showAndWait();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }
    }

    private boolean checkEmptyFields() {
        boolean result;
        if (NameTextfield.getText().isEmpty() ||
                BrandTextfield.getText().isEmpty() ||
                PriceTextfield.getText().isEmpty() ||
                StockTextfield.getText().isEmpty() ||
                ChooseTypeComboBox.getSelectionModel().getSelectedItem() == null) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }

    private boolean stockAndPriceRegex(String string) {
        Pattern regex = Pattern.compile(pattern);

        Matcher matcher = regex.matcher(string);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /***/
    private boolean checkProductExist() {
        boolean result = false;
        connection = database.connectDB();
        try {
            if (connection != null) {
                statement = connection.prepareStatement("SELECT productSeller , name FROM Product WHERE productSeller = '" + seller.getUsername() + "' AND name='" + NameTextfield.getText() + "'");
            }
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = true;
            } else {
                result = false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    /***/
    private void AddProductToDataBase() {
        connection = database.connectDB();
        try {
            if (connection != null) {
                statement = connection.prepareStatement("INSERT INTO product (name,productid,price,point,numberofpoints,brand,uniqueproperties,image,information,type,stock,productSeller) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            }
            statement.setString(1, NameTextfield.getText());
            String id = String.valueOf(MakeProductId.productIdMaker());
            statement.setString(2, id);
            statement.setString(3, PriceTextfield.getText());
            statement.setString(4, String.valueOf(0));
            statement.setString(5, String.valueOf(0));
            statement.setString(6, BrandTextfield.getText());
            statement.setString(7, "");
            String nativePath = ImageCopy.copyfile(path, id);
            statement.setString(8, nativePath);
            statement.setString(9, "");
            if (ChooseTypeComboBox.getSelectionModel().getSelectedItem() == "کالاهای اساسی و خواروبار") {
                statement.setString(10, "groceries");
            }
            if (ChooseTypeComboBox.getSelectionModel().getSelectedItem() == "مواد پروتئینی"){
                statement.setString(10, "proteins");
            }
            if (ChooseTypeComboBox.getSelectionModel().getSelectedItem() == "نوشیدنی"){
                statement.setString(10, "drinks");
            }
            if (ChooseTypeComboBox.getSelectionModel().getSelectedItem() == "تنقلات"){
                statement.setString(10, "nuts");
            }
            if (ChooseTypeComboBox.getSelectionModel().getSelectedItem() == "لبنیات"){
                statement.setString(10, "dairy");
            }
            if (ChooseTypeComboBox.getSelectionModel().getSelectedItem() == "سایر"){
                statement.setString(10, "other");
            }
            statement.setInt(11, Integer.parseInt(StockTextfield.getText()));
            statement.setString(12, seller.getUsername());
            statement.executeUpdate();
            getSellerProducts();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteButton() throws IOException {
        if (checkEmptyFields()) {
            if (stockAndPriceRegex(PriceTextfield.getText()) || stockAndPriceRegex(StockTextfield.getText())) {
                deletingProductFromDatabase();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect Data");
                alert.showAndWait();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }
    }

    private void deletingProductFromDatabase() {
        connection = database.connectDB();
        try {
            statement = connection.prepareStatement("DELETE FROM Product WHERE name='" + NameTextfield.getText() + "' AND productseller='" + seller.getUsername() + "'");
            statement.executeUpdate();
            System.out.println("delete successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void updateButton(){
        if (checkEmptyFields()){
            if (checkProductExist()){
                AddProductToDataBase();
            }else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("There is no product by this information");
                alert.showAndWait();
            }
        }else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }
    }


    private ObservableList<Product> inventoryListData;

    public void setProductsTable() throws IOException {
        inventoryListData = inventoryDataList();

        IdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        PriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        StockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        //ProductsTable.setItems(inventoryListData);
    }

    public ObservableList<Product> inventoryDataList() {
        ObservableList<Product> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM product WHERE brand = 'p7'";

        connect = database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                Product prod = new Product(result.getString("name"),
                        result.getString("brand"),
                        Double.parseDouble(result.getString("point")),
                        Integer.parseInt(result.getString("numberofpoints")),
                        Integer.parseInt(result.getString("productid")),
                        result.getString("information"),
                        Double.parseDouble(result.getString("price")),
                        result.getString("type"),
                        result.getString("image"),
                        Integer.parseInt(result.getString("stock")),
                        new ArrayList<>(),
                        result.getString("productSeller"));

                listData.add(prod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    public void END(){
        InfoName.setText(seller.getFirstname());
        InfoFamily.setText(seller.getLastname());
        InfoPhoneNumber.setText(seller.getPhoneNumber());
        InfoUserName.setText(seller.getUsername());
        InfoEmail.setText(seller.getEmailAddress());
        InfoCompanyName.setText("Freshly");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            client = new Client(new Socket("localhost", 1234));
            System.out.println("Connected to Server");
            client.sendMessageToServer("!@#$Username :"+seller.getUsername());
            UserName = seller.getUsername();
            addtext();
            client.receiveMessageFromServer(ChatVbox);
            END();
            ChatVbox.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    ChatScroll.setVvalue((Double) newValue);
                }
            });

            setSellerProducts();
            //setAuctionInfo();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        SetChooseRole();
        getSellerProducts();
    }

    @Override
    public boolean isLogin() {
        return seller != null;
    }
    public void changeSellerUsernameTextField(){
        if (isLogin()){
            sellerUsername.setText(seller.getUsername());
        }else {
            sellerUsername.setText("");
        }
    }



    /*************************************/


    String UserName;

    private Statement statement1;


    public static void addLabel(String messageFromServer, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle(
                "-fx-background-color: rgb(233, 233, 235);" +
                        "-fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });

    }

    public void buttonClicked(ActionEvent actionEvent){
        String messageToSend = ChatTextField.getText();
        if (!messageToSend.isBlank()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            //int spaceIndex2 = messageToSend.indexOf(" ");
            //String message2 = messageToSend.substring(spaceIndex2 + 1);

            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);

            textFlow.setStyle(
                    "-fx-color: rgb(239, 242, 255);" +
                            "-fx-background-color: rgb(15, 125, 242);" +
                            "-fx-background-radius: 20px;");

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.925, 0.996));

            hBox.getChildren().add(textFlow);
            ChatVbox.getChildren().add(hBox);
            if (UserName.equals("admin")) {

                int spaceIndex = messageToSend.indexOf(" ");
                String recipient = "";
                String message = "";
                if (spaceIndex != -1) {
                    recipient = messageToSend.substring(1, spaceIndex); // نام کاربری دریافت کننده پیام
                    message = messageToSend.substring(spaceIndex + 1); // متن پیام خصوصی
                    ChatServer.sendPrivateMessage(message, "admin", recipient); // ارسال پیام خصوصی به دریافت کننده
                }
                connection = database.connectDB();
                try{
                    statement1 = connection.createStatement();
                    statement1.executeUpdate("INSERT INTO messages (sender,recipient,text,new,date) VALUES ('"+UserName+"','"+recipient+"','"+message+"','Yes','"+LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)+"')");
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }

            }else {

                ChatServer.sendPrivateMessage(messageToSend, UserName, "admin");

                connection = database.connectDB();
                try{
                    statement1 = connection.createStatement();
                    statement1.executeUpdate("INSERT INTO messages (sender,recipient,text,new,date) VALUES ('"+UserName+"','admin','"+messageToSend+"','Yes','"+LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)+"')");
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }

            client.sendMessageToServer(messageToSend);


            ChatVbox.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    ChatScroll.setVvalue((Double) newValue);
                }
            });

            ChatTextField.clear();
        }
    }
    public static void recieveMessage(String messageFromServer, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle(
                "-fx-background-color: rgb(233, 233, 235);" +
                        "-fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);
        vBox.getChildren().add(hBox);



        /*Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });*/
    }
    public void sentMessages(String messageToSend){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageToSend);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle(
                "-fx-color: rgb(239, 242, 255);" +
                        "-fx-background-color: rgb(15, 125, 242);" +
                        "-fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.925, 0.996));

        hBox.getChildren().add(textFlow);
        ChatVbox.getChildren().add(hBox);

        /*****/
        /*****/



        //client.sendMessageToServer(messageToSend);
        ChatTextField.clear();
    }

    /*public void ConfirmClicked(ActionEvent event){
            client.sendMessageToServer(Username.getText());

            UserName = Username.getText();





            client.receiveMessageFromServer(vbox);
    }*/
    /*public void refresh(ActionEvent event){
        addtext();
    }*/
    public void addtext(){
        connection = database.connectDB();
        try {
            statement1 = connection.createStatement();
            resultSet = statement1.executeQuery("SELECT * FROM messages");
            while (resultSet.next()){
                String senderText = resultSet.getString("sender");
                String recipientText = resultSet.getString("recipient");
                String text = resultSet.getString("text");
                if (senderText.equals(UserName)){
                    sentMessages(resultSet.getString("text"));
                    System.out.println(UserName +" : "+text);
                } else if (recipientText.equals(UserName)) {
                    recieveMessage(resultSet.getString("sender")+" : "+resultSet.getString("text"), ChatVbox);
                    System.out.println(resultSet.getString("sender")+" : "+text);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showInfoPane(){
        InfoPane.setVisible(true);
        ChatPane.setVisible(false);
        AuctionPane.setVisible(false);
        AddPane.setVisible(false);
    }
    public void showChatPane(){
        InfoPane.setVisible(false);
        ChatPane.setVisible(true);
        AuctionPane.setVisible(false);
        AddPane.setVisible(false);
    }
    public void showAuctionPane(){
        InfoPane.setVisible(false);
        ChatPane.setVisible(false);
        AuctionPane.setVisible(true);
        AddPane.setVisible(false);
        setSellerProducts();
    }

    public void showAddPane(){
        AddPane.setVisible(true);
        InfoPane.setVisible(false);
        ChatPane.setVisible(false);
        AuctionPane.setVisible(false);
    }

    public ObservableList<Product> getSellerProducts(){
        String sql = "SELECT * FROM product WHERE productSeller = '"+SellerPage.seller.getUsername()+"'";
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

    public void setSellerProducts(){
        cardListData.clear();
        cardListData.addAll(getSellerProducts());


        AuctionGridPane.getChildren().clear();
        AuctionGridPane.getRowConstraints().clear();
        AuctionGridPane.getColumnConstraints().clear();
        int column = 0;
        int row = 0;
        for (int q = 0; q < cardListData.size(); q++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("ProductLabelInAnbar.fxml"));
                Pane pane = load.load();
                ProductLabelInAnbar Pl = load.getController();
                Pl.setData(cardListData.get(q));

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


    public String GetPrice(String id){
        cardListData = getSellerProducts();
        String price = null;
        for (int i = 0 ; i<cardListData.size() ; i++){
            if (String.valueOf(cardListData.get(i).getProductId()).equals(id)){
                price=String.valueOf(cardListData.get(i).getPrice());
                break;
            }
        }
        return price;
    }

    public void setAuctionToDatabase(){
        String price = GetPrice(IDTextField.getText());
        System.out.println(price);
        connection = database.connectDB();
        try {
            if (connection != null) {
                statement = connection.prepareStatement("INSERT INTO auction (productid,remainingdays,auctionprice,date) VALUES (?,?,?,?)");
            }
            statement.setString(1, IDTextField.getText());
            statement.setString(2,RemainingDaysTextField.getText());
            statement.setString(3,"0");
            statement.setString(4, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            statement.executeUpdate();
            IDTextField.setText("");
            RemainingDaysTextField.setText("");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

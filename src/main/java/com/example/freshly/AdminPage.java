package com.example.freshly;

import javafx.application.Platform;
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
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminPage implements Initializable {


    @FXML
    private PieChart DayChart;
    @FXML
    private PieChart MonthChart;
    @FXML
    private PieChart YearChart;

    @FXML
    private HBox ChatHbox;

    @FXML
    private ScrollPane ChatScrollPane;

    @FXML
    private TextFlow ChatTextFlow;

    @FXML
    private VBox ChatVbox;

    @FXML
    private Pane ConfirmSeller;

    @FXML
    private Button ConfirmSellerButton;

    @FXML
    private GridPane ConfirmationGridPane;

    @FXML
    private Pane DefineStoreRoom;

    @FXML
    private Button FinancialButton;

    @FXML
    private Pane FinancialPane;

    @FXML
    private Pane MainPane;

    @FXML
    private Pane ReportStoreRoom;

    @FXML
    private Button SendMassageButton;

    @FXML
    private Pane SendMassagePane;

    @FXML
    private Pane StoreRoom;

    @FXML
    private Button StoreRoomButton;

    @FXML
    private Pane inventoryStoreRoom;

    @FXML
    private TextField ChatTextField;

    @FXML
    private TextField storeAddress;

    @FXML
    private TextField storeManagement;

    @FXML
    private TextField storeName;

    @FXML
    private TextField storeProduct;

    @FXML
    private Button update;

    @FXML
    private Button add;

    @FXML
    private Button delete;

    @FXML
    private PieChart FinancialChart;
    @FXML
    private PieChart FinancialChart1;

    ObservableList<Seller> requests = FXCollections.observableArrayList();


    public void switchtoFinancial(){
        ConfirmSeller.setVisible(false);
        StoreRoom.setVisible(false);
        FinancialPane.setVisible(true);
        SendMassagePane.setVisible(false);
    }

    public void switchtoMassages(){
        ConfirmSeller.setVisible(false);
        StoreRoom.setVisible(false);
        FinancialPane.setVisible(false);
        SendMassagePane.setVisible(true);
    }

    public void switchtoStoreRoom(){
        ConfirmSeller.setVisible(false);
        StoreRoom.setVisible(true);
        FinancialPane.setVisible(false);
        SendMassagePane.setVisible(false);
    }

    public void switchtoComfirmSeller(){
        ConfirmSeller.setVisible(true);
        StoreRoom.setVisible(false);
        FinancialPane.setVisible(false);
        SendMassagePane.setVisible(false);
        requestDisplayCard(true);
    }

    public void switchtoDefineStoreRoom(){
        ReportStoreRoom.setVisible(false);
        inventoryStoreRoom.setVisible(false);
        DefineStoreRoom.setVisible(true);
    }

    public void switchtoInventoryStoreRoom(){
        ReportStoreRoom.setVisible(false);
        inventoryStoreRoom.setVisible(true);
        DefineStoreRoom.setVisible(false);
    }

    public void switchtoReportStoreRoom(){
        ReportStoreRoom.setVisible(true);
        inventoryStoreRoom.setVisible(false);
        DefineStoreRoom.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            admin = new Admin(new Socket("localhost", 1234));

        System.out.println("Connected to Server");
        admin.sendMessageToServer("!@#$Username :admin");
        UserName = "admin";
        addtext();
        admin.receiveMessageFromServer(ChatVbox);
        requestDisplayCard(true);
        switchtoComfirmSeller();
        initializeDayChart();
        initializeMonthChart();
        initializeYearChart();
        setFinancialChartMonth();
        setFinancialChartYear();
        ChatVbox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                ChatScrollPane.setVvalue((Double) newValue);
            }
        });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int calculateNumberOfProducts(String productIds){
        int sum = 0;
        connection = database.connectDB();
        String [] ids = productIds.split("@");
        for (int i = 0; i < ids.length; i++) {
            try {
                statement1 = connection.createStatement();
                resultSet = statement1.executeQuery("SELECT stock FROM product WHERE productid = '"+ids[i]+"'");
                if (resultSet.next()) {
                    int number = Integer.parseInt(resultSet.getString("stock"));
                    sum += number;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return sum;
    }

    public void addbutton() throws SQLException {
        try {
            connection = database.connectDB();
            statement1 = connection.createStatement();
            String number = storeProduct.getText();
            statement1.executeUpdate("INSERT INTO store (name,address,manager,products,numberofproducts) VALUES ('" + storeName.getText() + "','" + storeAddress.getText() + "','" + storeManagement.getText() + "','" + storeProduct.getText() + "@" + "','" + calculateNumberOfProducts(number) + "')");
            statement1 = connection.createStatement();
            System.out.println(LocalDateTime.now());
            statement1.executeUpdate("INSERT INTO  inputoutput (date,id,count,store) VALUES ('" + LocalDateTime.now() + "','" + storeProduct.getText() + "','" +"+"+calculateNumberOfProducts(storeProduct.getText()) + "','" + storeName.getText() + "')");
        }catch (Exception e){
            System.out.println("موفقیت آمیز بود");
        }
    }
    public void updatebutton(){

        connection = database.connectDB();
        try {
            statement1 = connection.createStatement();
            resultSet = statement1.executeQuery("SELECT name , products FROM store WHERE name = '"+storeName.getText()+"'");
            if (resultSet.next()){
                String products = resultSet.getString("products");
                statement1.executeUpdate("UPDATE store SET address = '"+storeAddress.getText()+"' , manager = '"+storeManagement.getText()+"', products = '"+products+storeProduct.getText()+"@"+"' , numberofproducts = '"+calculateNumberOfProducts(products+storeProduct.getText()+"@")+"' WHERE name = '"+storeName.getText()+"'");
            }else {
                System.out.println("no store");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void deletebutton(){

        connection = database.connectDB();
        try {
            statement1 = connection.createStatement();
            resultSet = statement1.executeQuery("SELECT name FROM store WHERE name = '"+storeName.getText()+"'");
            if (resultSet.next()){
                statement1 = connection.createStatement();
                statement1.executeUpdate("DELETE FROM store WHERE name = '"+storeName.getText()+"'");
                System.out.println("deleted");
            }else {
                System.out.println("no store");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void csvReport() throws SQLException, IOException {
        try {
        connection = database.connectDB();
        String query = "SELECT * FROM inputoutput";
        statement1 = connection.createStatement();
        ResultSet rs = statement1.executeQuery(query);

        FileWriter csvWriter = new FileWriter("mytable.csv");
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Write CSV headers
        for (int i = 1; i <= columnCount; i++) {
            csvWriter.append(metaData.getColumnName(i));
            if (i < columnCount) {
                csvWriter.append(",");
            }
        }
        csvWriter.append("\n");

        // Write CSV data
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                csvWriter.append(rs.getString(i));
                if (i < columnCount) {
                    csvWriter.append(",");
                }
            }
            csvWriter.append("\n");
        }

        csvWriter.flush();
        csvWriter.close();
        connection.close();
    } catch (SQLException | IOException e) {
        System.err.println("Got an exception!");
        System.err.println(e.getMessage());
    }
    }

    /*********************************************/
    String UserName;

    private Connection connection;
    private Statement statement1;
    private PreparedStatement prepare;
    private ResultSet result;
    private ResultSet resultSet;
    private Admin admin;

    private ObservableList<Seller> getRequests(){

            String sql = "SELECT * FROM seller WHERE varified = 'false'";

            ObservableList<Seller> listData = FXCollections.observableArrayList();
            connection = database.connectDB();

            try {
                if (connection != null) {
                    prepare = connection.prepareStatement(sql);
                }
                result = prepare.executeQuery();

                Seller seller;

                while (result.next()) {
                    seller = new Seller(result.getString("Username"),
                                result.getString("Password"),
                                result.getString("FirstName"),
                                result.getString("LastName"),
                                result.getString("PhoneNumber"),
                                result.getString("EmailAddress"),
                            "seller",
                                result.getString("Company"));

                    listData.add(seller);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return listData;
        }

        public void requestDisplayCard(boolean firstTime){
            if (firstTime) {
                requests.clear();
                requests.addAll(getRequests());
            }

            int row = 0;
            int column = 0;

            ConfirmationGridPane.getChildren().clear();
            ConfirmationGridPane.getRowConstraints().clear();
            ConfirmationGridPane.getColumnConstraints().clear();

            for (int q = 0; q < requests.size(); q++) {

                try {
                    FXMLLoader load = new FXMLLoader();
                    load.setLocation(getClass().getResource("Confirmation.fxml"));
                    Pane pane = load.load();
                    Confirmation pl = load.getController();
                    pl.setData(requests.get(q));

                    if (column == 1) {
                        column = 0;
                        row += 1;
                    }

                    ConfirmationGridPane.add(pane, column++, row);

                    GridPane.setMargin(pane, new Insets(1));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }



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

            admin.sendMessageToServer(messageToSend);


            ChatVbox.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    ChatScrollPane.setVvalue((Double) newValue);
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

    public void initializeDayChart() {
        ArrayList<Integer> result = Report.getDay("inputoutput");

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("ورودی", result.get(0)),
                new PieChart.Data("خروجی", result.get(1))
        );
        DayChart.setData(pieChartData);

    }
    public void initializeMonthChart() {
        ArrayList<Integer> result = Report.getMonth("inputoutput");

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("ورودی", result.get(0)),
                new PieChart.Data("خروجی", result.get(1))
        );
        MonthChart.setData(pieChartData);
    }
    public void initializeYearChart() {
        ArrayList<Integer> result = Report.getYear("inputoutput");

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("ورودی", result.get(0)),
                new PieChart.Data("خروجی", result.get(1))
        );
        YearChart.setData(pieChartData);
    }

    public void showDayChart(){
        DayChart.setVisible(true);
        MonthChart.setVisible(false);
        YearChart.setVisible(false);
    }
    public void showMonthChart(){
        DayChart.setVisible(false);
        MonthChart.setVisible(true);
        YearChart.setVisible(false);
    }
    public void showYearChart(){
        DayChart.setVisible(false);
        MonthChart.setVisible(false);
        YearChart.setVisible(true);
    }

    public void setFinancialChartMonth(){
        ArrayList<Integer> result = Report.getMonth("financial");

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("درآمد", result.get(0)),
                new PieChart.Data("هزینه", 10000000)
        );
        FinancialChart.setData(pieChartData);
    }
    public void setFinancialChartYear(){
        ArrayList<Integer> result = Report.getYear("financial");

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("درآمد", result.get(0)),
                new PieChart.Data("هزینه", 10000000)
        );
        FinancialChart1.setData(pieChartData);
    }

    public void showChart(){
        FinancialChart.setVisible(true);
        FinancialChart1.setVisible(false);
    }
    public void showChart1(){
        FinancialChart.setVisible(false);
        FinancialChart1.setVisible(true);
    }
}

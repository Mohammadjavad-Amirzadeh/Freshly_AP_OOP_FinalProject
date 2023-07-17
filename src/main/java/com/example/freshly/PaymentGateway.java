package com.example.freshly;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class PaymentGateway implements Initializable {

    @FXML
    private ImageView BankLogo;

    @FXML
    private Button BuyingButton;

    @FXML
    private Button CancelButton;

    @FXML
    private TextField CaptchaCodeTextField;

    @FXML
    private ImageView CaptchaPicture0;

    @FXML
    private ImageView CaptchaPicture1;

    @FXML
    private ImageView CaptchaPicture10;

    @FXML
    private ImageView CaptchaPicture11;

    @FXML
    private ImageView CaptchaPicture12;

    @FXML
    private ImageView CaptchaPicture2;

    @FXML
    private ImageView CaptchaPicture3;

    @FXML
    private ImageView CaptchaPicture4;

    @FXML
    private ImageView CaptchaPicture5;

    @FXML
    private ImageView CaptchaPicture6;

    @FXML
    private ImageView CaptchaPicture7;

    @FXML
    private ImageView CaptchaPicture8;

    @FXML
    private ImageView CaptchaPicture9;

    @FXML
    private TextField CardNumber1;

    @FXML
    private TextField CardNumber2;

    @FXML
    private TextField CardNumber3;

    @FXML
    private TextField CardNumber4;

    @FXML
    private Label CardNumberData1;

    @FXML
    private Label CardNumberData2;

    @FXML
    private Label CardNumberData3;

    @FXML
    private Label CardNumberData4;

    @FXML
    private Label Cvv2Number;

    @FXML
    private TextField Cvv2TextField;

    @FXML
    private Label ExpertDate;

    @FXML
    private TextField ExpertDateTextField;

    @FXML
    private TextField ExpertMonthTextField;

    @FXML
    private Label ExpertYear;

    @FXML
    private Pane MainPane;

    @FXML
    private TextField PasswordTextField;

    @FXML
    private Button SendPassword;

    private String CardNumberString;
    public static String finalPrice;
    private Alert alert;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public void ChangeTextFields(){
        CardNumber1.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() == 4) {
                CardNumber2.requestFocus();
                CardNumberString = CardNumber1.getText();
                System.out.println(CardNumberString);
            }
        });
        CardNumber2.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() == 4) {
                CardNumber3.requestFocus();
                CardNumberString = CardNumberString + CardNumber2.getText();
                System.out.println(CardNumberString);
                if (CardNumberString.startsWith("603799")){
                    Image image = new Image(getClass().getResource("/com/example/freshly/Assets/BankMelli.png").toString());
                    BankLogo.setImage(image);
                }
                if (CardNumberString.startsWith("589210")){
                    Image image = new Image(getClass().getResource("/com/example/freshly/Assets/BankSepah.png").toString());
                    BankLogo.setImage(image);
                }
                if (CardNumberString.startsWith("627353")){
                    Image image = new Image(getClass().getResource("/com/example/freshly/Assets/BankTejarat.png").toString());
                    BankLogo.setImage(image);
                }
                if (CardNumberString.startsWith("621986")){
                    Image image = new Image(getClass().getResource("/com/example/freshly/Assets/BankSaman.png").toString());
                    BankLogo.setImage(image);
                }
                if (CardNumberString.startsWith("610433")){
                    Image image = new Image(getClass().getResource("/com/example/freshly/Assets/BankMellat.png").toString());
                    BankLogo.setImage(image);
                }
            }
        });

        CardNumber3.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() == 4) {
                CardNumber4.requestFocus();
                CardNumberString = CardNumberString + CardNumber3.getText();
            }
        });

        CardNumber4.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() == 4) {
                Cvv2TextField.requestFocus();
                CardNumberString = CardNumberString + CardNumber4.getText();
            }
        });

        Cvv2TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() == 4) {
                ExpertMonthTextField.requestFocus();
            }
        });

        ExpertMonthTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() == 2) {
                ExpertDateTextField.requestFocus();
            }
        });

        ExpertDateTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() == 2) {
                CaptchaCodeTextField.requestFocus();
            }
        });

        CaptchaCodeTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() == 4) {
                PasswordTextField.requestFocus();
            }
        });
    }

    public void setCardData(){
        CardNumber1.textProperty().addListener((obs, oldText, newText) -> {
            CardNumberData1.setText(newText);
        });
        CardNumber2.textProperty().addListener((obs, oldText, newText) -> {
            CardNumberData2.setText(newText);
        });
        CardNumber3.textProperty().addListener((obs, oldText, newText) -> {
            CardNumberData3.setText(newText);
        });
        CardNumber4.textProperty().addListener((obs, oldText, newText) -> {
            CardNumberData4.setText(newText);
        });
        Cvv2TextField.textProperty().addListener((obs, oldText, newText) -> {
            Cvv2Number.setText(newText);
        });
        ExpertMonthTextField.textProperty().addListener((obs, oldText, newText) -> {
            ExpertDate.setText(newText);
        });
        ExpertDateTextField.textProperty().addListener((obs, oldText, newText) -> {
            ExpertYear.setText(newText);
        });
    }

    public boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public void checkFields(){
        if (!isNumeric(CardNumber1.getText()) || !isNumeric(CardNumber2.getText()) || !isNumeric(CardNumber3.getText()) ||!isNumeric(CardNumber4.getText()) ||
                !isNumeric(Cvv2TextField.getText()) || !isNumeric(ExpertMonthTextField.getText()) || !isNumeric(ExpertDateTextField.getText())||
                !isNumeric(PasswordTextField.getText())){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Input");
            alert.showAndWait();
        }
    }

    public void setTextFields(){
        CardNumber1.setFocusTraversable(true);
    }



    public boolean isEmpty1(){
        boolean result = false;
        //Todo captcha
        result = CardNumber1.getText().isEmpty() || CardNumber2.getText().isEmpty() || CardNumber3.getText().isEmpty() || CardNumber4.getText().isEmpty() || Cvv2TextField.getText().isEmpty() || ExpertMonthTextField.getText().isEmpty() || ExpertDateTextField.getText().isEmpty() || !CaptchaChecker.checkInputCaptcha(CaptchaCodeTextField.getText(), rand);
        return result;
    }
    public boolean passwordChecker(){
        return PasswordTextField.getText().equals(String.valueOf(pass));
    }

    private int pass;
    private int rand;
    public void sendPass(){
        if (isEmpty1()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("incorrect captcha");
            alert.showAndWait();
        }else {
            pass=RandomNumber.getRand2();
            Thread thread = new Thread(new EmailSender(MainPage.customer.getEmailAddress(),"This is A Message From Freshly Team" +
                    "Your Password is : "+pass+""));
            thread.start();
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info Message");
            alert.setHeaderText(null);
            alert.setContentText("Password sent");
            alert.showAndWait();
        }
    }
    public void PayButton(ActionEvent event) throws SQLException, IOException {
        if (passwordChecker()){
            System.out.println("Done");

            connection = database.connectDB();
            statement=connection.createStatement();
            statement.executeUpdate("INSERT INTO financial (count,date) VALUES ('"+"+"+finalPrice+"','"+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) +"')");

            statement = connection.createStatement();
            MainPage.customer.setWallet(MainPage.customer.getWallet()+Integer.parseInt(finalPrice));
            int finalWallet = MainPage.customer.getWallet();
            statement.executeUpdate("UPDATE costumer SET wallet = '"+finalWallet+"' WHERE Username = '"+MainPage.customer.getUsername()+"'");
            Thread thread = new Thread(new EmailSender(MainPage.customer.getEmailAddress(),"your purchase done successfully"));
            CostumerPage.swWallet = true;
            Parent root = FXMLLoader.load(getClass().getResource("CostumerPage.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setTitle("Login Page");
            ExpertDate.getScene().getWindow().hide();

            stage.setScene(scene);
            stage.show();
        }else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("password is not correct");
            alert.showAndWait();
            System.out.println("ریدی");
        }
    }
    public void cancelButton(ActionEvent event) throws IOException {
        CostumerPage.swWallet = true;
        Parent root = FXMLLoader.load(getClass().getResource("CostumerPage.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setTitle("Login Page");
        ExpertDate.getScene().getWindow().hide();

        stage.setScene(scene);
        stage.show();
    }

    public void setCaptchaImage(int input){
        CaptchaPicture0.setVisible(false);
        CaptchaPicture1.setVisible(false);
        CaptchaPicture2.setVisible(false);
        CaptchaPicture3.setVisible(false);
        CaptchaPicture4.setVisible(false);
        CaptchaPicture5.setVisible(false);
        CaptchaPicture6.setVisible(false);
        CaptchaPicture7.setVisible(false);
        CaptchaPicture8.setVisible(false);
        CaptchaPicture9.setVisible(false);
        CaptchaPicture10.setVisible(false);
        CaptchaPicture11.setVisible(false);
        CaptchaPicture12.setVisible(false);
        if (input==100){
            CaptchaPicture0.setVisible(true);
        }
        if (input==101){
            CaptchaPicture1.setVisible(true);
        }
        if (input==102){
            CaptchaPicture2.setVisible(true);
        }
        if (input==103){
            CaptchaPicture3.setVisible(true);
        }
        if (input==104){
            CaptchaPicture4.setVisible(true);
        }
        if (input==105){
            CaptchaPicture5.setVisible(true);
        }
        if (input==106){
            CaptchaPicture6.setVisible(true);
        }
        if (input==107){
            CaptchaPicture7.setVisible(true);
        }
        if (input==108){
            CaptchaPicture8.setVisible(true);
        }
        if (input==109){
            CaptchaPicture9.setVisible(true);
        }
        if (input==110){
            CaptchaPicture10.setVisible(true);
        }
        if (input==111){
            CaptchaPicture11.setVisible(true);
        }
        if (input==112){
            CaptchaPicture12.setVisible(true);
        }
    }

    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTextFields();
        ChangeTextFields();
        setCardData();
        rand = RandomNumber.getRand();
        setCaptchaImage(rand);
        System.out.println(rand);
    }
}

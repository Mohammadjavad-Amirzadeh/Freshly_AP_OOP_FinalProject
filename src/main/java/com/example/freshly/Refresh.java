package com.example.freshly;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Refresh {
    private static final LocalDateTime time = LocalDateTime.now();
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    public static void delete(){

        connection = database.connectDB();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT date , remainingdays FROM auction");
            while (resultSet.next()){
                String date = resultSet.getString("date");
                String remainingDays = resultSet.getString("remainingdays");
                String expireTime = addDaysToDate(date,Integer.parseInt(remainingDays));
                if (expireTime.equals(time.format(DateTimeFormatter.ISO_LOCAL_DATE))){
                    statement = connection.createStatement();
                    statement.executeUpdate("DELETE FROM auction WHERE date = '"+date+"'");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void deleteMessages(){

        connection = database.connectDB();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT date FROM messages");
            while (resultSet.next()){
                String date = resultSet.getString("date");
                String remainingDays = "7";
                String expireTime = addDaysToDate(date,Integer.parseInt(remainingDays));
                if (expireTime.equals(time.format(DateTimeFormatter.ISO_LOCAL_DATE))){
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery("SELECT winner , productid FROM auction WHERE date = '"+date+"'");
                    if (resultSet.next()){
                        String winner = resultSet.getString("winner");
                        String productid = resultSet.getString("productid");
                        Statement statement1 = connection.createStatement();
                        ResultSet resultSet1 = statement1.executeQuery("SELECT history FROM costumer WHERE Username = '"+winner+"'");
                        if (resultSet1.next()){
                            String history = resultSet1.getString("history");
                            Statement statement2 = connection.createStatement();
                            statement2.executeUpdate("UPDATE cosutmer SET history = '"+history+productid+"@"+"' WHERE Username = '"+winner+"'");
                        }
                    }
                    statement = connection.createStatement();
                    statement.executeUpdate("DELETE FROM auction WHERE date = '"+date+"'");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static String addDaysToDate(String date, int daysToAdd) {
        // تبدیل تاریخ به شیء LocalDate
        LocalDate localDate = LocalDate.parse(date);

        // افزودن تعداد روز به تاریخ
        localDate = localDate.plusDays(daysToAdd);

        // تبدیل تاریخ به رشته با فرمت yyyy-mm-dd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String newDate = localDate.format(formatter);

        // بازگرداندن تاریخ جدید
        System.out.println(newDate);
        return newDate;
    }

    public static void main(String[] args) {
        delete();
    }
}

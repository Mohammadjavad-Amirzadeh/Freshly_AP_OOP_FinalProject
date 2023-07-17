package com.example.freshly;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Report {

    private static ArrayList<String> report(String table) {
        ArrayList<String> returnArray = new ArrayList<>();

        try {

            Connection connection = database.connectDB();
            String sql = "SELECT date, count FROM "+table;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                String date = resultSet.getString("date");
                String count = resultSet.getString("count");

                String entry = "";
                String exit = "";
                if (count.startsWith("+")){
                    entry = count.substring(1);
                    exit = "0";
                }else {
                    entry = "0";
                    exit = count.substring(1);
                }
                returnArray.add(date);
                returnArray.add(entry);
                returnArray.add(exit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnArray;
    }


    public static ArrayList<Integer> getDay(String table){
        ArrayList<Integer> result = new ArrayList<>();

        ArrayList<String> report = report(table);
        LocalDate date = LocalDate.now();
        String localDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        ArrayList<Integer> entry_exit = new ArrayList<>();
        for (int i = 0; i < report.size(); i++) {
            if (report.get(i).equals(localDate)){
                entry_exit.add(Integer.parseInt(report.get(i+1)));
                entry_exit.add(Integer.parseInt(report.get(i+2)));
            }
        }

        int entry=0;
        int exit=0;
        for (int i = 0; i < entry_exit.size(); i++) {
            if(i%2==0)
                entry+=entry_exit.get(i);
            else
                exit+= entry_exit.get(i);
        }

        result.add(entry);
        result.add(exit);

        return result;
    }

    public static ArrayList<Integer> getMonth(String table){
        ArrayList<Integer> result = new ArrayList<>();

        ArrayList<String> report = report(table);
        LocalDate date = LocalDate.now();
        String localDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String localMonth = localDate.substring(0,7);
        System.out.println(localMonth);
        ArrayList<Integer> entry_exit = new ArrayList<>();
        for (int i = 0; i < report.size(); i++) {
            if (report.get(i).contains(localMonth)){
                entry_exit.add(Integer.parseInt(report.get(i+1)));
                entry_exit.add(Integer.parseInt(report.get(i+2)));
            }
        }
        int entry=0;
        int exit=0;
        for (int i = 0; i < entry_exit.size(); i++) {
            if(i%2==0)
                entry+=entry_exit.get(i);
            else
                exit+= entry_exit.get(i);
        }
        result.add(entry);
        result.add(exit);

        return result;
    }

    public static ArrayList<Integer> getYear(String table){
        ArrayList<Integer> result = new ArrayList<>();

        ArrayList<String> report = report(table);
        LocalDate date = LocalDate.now();
        String localDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String localYear = localDate.substring(0,4);
        System.out.println(localYear);
        ArrayList<Integer> entry_exit = new ArrayList<>();
        for (int i = 0; i < report.size(); i++) {
            if (report.get(i).contains(localYear)){
                entry_exit.add(Integer.parseInt(report.get(i+1)));
                entry_exit.add(Integer.parseInt(report.get(i+2)));
            }
        }
        int entry=0;
        int exit=0;
        for (int i = 0; i < entry_exit.size(); i++) {
            if(i%2==0)
                entry+=entry_exit.get(i);
            else
                exit+= entry_exit.get(i);
        }
        result.add(entry);
        result.add(exit);

        return result;
    }

    public static void main(String[] args) {
        ArrayList<Integer> a = getDay("financial");
        for (int i = 0; i < a.size(); i++) {
            System.out.println(a.get(i));
        }
    }
}
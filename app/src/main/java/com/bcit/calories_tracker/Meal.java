package com.bcit.calories_tracker;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Meal implements Serializable {
    String name;
    String cal;
    String carb;
    String fat;
    String protein;
    int quantity;
    String date;

    public Meal(String name, String cal, String carb, String fat, String protein, int quantity) {
        this.name = name;
        this.cal = cal;
        this.carb = carb;
        this.fat = fat;
        this.protein = protein;
        this.quantity = quantity;
    }

    public Meal(String name, String cal, String carb, String fat, String protein, String date, int quantity) {
        this(name, cal, carb, fat, protein, quantity);
        this.setDate(date);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    public String getCarb() {
        return carb;
    }

    public void setCarb(String carb) {
        this.carb = carb;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static String calculateTotalCal(Meal[] meals) {
        int total_calories = 0;
        for (Meal meal : meals) {
            String calorieCount = meal.getCal();
            String calValue = calorieCount.replaceAll("[^.0-9]", "");
            total_calories += Float.parseFloat(calValue);
        }
        return total_calories + " cal";
    }

    public static String calculateTotalCal(ArrayList<Meal> meals) {
        int total_calories = 0;
        for (Meal meal : meals) {
            String calorieCount = meal.getCal();
            String calValue = calorieCount.replaceAll("[^.0-9]", "");
            total_calories += Float.parseFloat(calValue);
        }
        return total_calories + "";
    }

    public static String getMealDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}

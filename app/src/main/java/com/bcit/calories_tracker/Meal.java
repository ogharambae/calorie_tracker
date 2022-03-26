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
    String cholesterol;
    String sodium;
    String vitaA;
    String vitaB;
    String vitaC;
    String vitaD;
    String calcium;
    String iron;

    public Meal(String name, String cal, String carb, String fat, String protein, int quantity, String vitaA, String cholesterol,
    String sodium, String vitaB, String vitaC, String vitaD, String calcium, String iron) {
        this.name = name;
        this.cal = cal;
        this.carb = carb;
        this.fat = fat;
        this.protein = protein;
        this.quantity = quantity;
        this.vitaA = vitaA;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.vitaB = vitaB;
        this.vitaC = vitaC;
        this.vitaD = vitaD;
        this.calcium = calcium;
        this.iron = iron;
    }

    public Meal(String name, String cal, String carb, String fat, String protein, String date, int quantity, String vitaA, String cholesterol,
                String sodium, String vitaB, String vitaC, String vitaD, String calcium, String iron) {
        this(name, cal, carb, fat, protein, quantity, vitaA, cholesterol, sodium, vitaB, vitaC, vitaD, calcium, iron);
        this.setDate(date);
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public String getSodium() {
        return sodium;
    }

    public String getVitaB() {
        return vitaB;
    }

    public String getVitaC() {
        return vitaC;
    }

    public String getVitaD() {
        return vitaD;
    }

    public String getCalcium() {
        return calcium;
    }

    public String getIron() {
        return iron;
    }

    public String getVitaA() {
        return vitaA;
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

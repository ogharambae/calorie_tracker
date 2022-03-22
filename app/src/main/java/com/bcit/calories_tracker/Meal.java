package com.bcit.calories_tracker;

import java.io.Serializable;

public class Meal implements Serializable {
    String name;
    String cal;
    String carb;
    String fat;
    String protein;
    int quantity;

    public Meal(String name, String cal, String carb, String fat, String protein, int quantity) {
        this.name = name;
        this.cal = cal;
        this.carb = carb;
        this.fat = fat;
        this.protein = protein;
        this.quantity = quantity;
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
}

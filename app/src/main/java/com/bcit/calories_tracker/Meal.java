package com.bcit.calories_tracker;

import java.io.Serializable;

public class Meal implements Serializable {
    String name;
    int quantity;
    int calories;

    public Meal(String name, int quantity, int calories) {
        this.name = name;
        this.quantity = quantity;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}

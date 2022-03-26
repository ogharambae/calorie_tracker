package com.bcit.calories_tracker;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MealImporter implements Runnable {

    Activity activity;
    DataRequest dataRequest;

    public MealImporter(Activity activity, DataRequest dataRequest) {
        this.activity = activity;
        this.dataRequest = dataRequest;
    }

    @Override
    public void run() {
        String json = readFromAsset(activity, "meals.json");
        try {
            Gson gson = new Gson();
            MealFile.Root records = gson.fromJson(json, MealFile.Root.class);
            ArrayList<Meal> importMeals = new ArrayList<>();
            for (MealFile.SurveyFood food : records.surveyFoods) {
                String name = food.description.toLowerCase();

                MealFile.FoodNutrient proteinNutrient = (MealFile.FoodNutrient) food.foodNutrients.stream().filter(f -> f.nutrient.name.equals("Protein")).toArray()[0];
                String protein = proteinNutrient.amount + " " + proteinNutrient.nutrient.unitName;
                MealFile.FoodNutrient fatNutrient = (MealFile.FoodNutrient) food.foodNutrients.stream().filter(f -> f.nutrient.name.equals("Total lipid (fat)")).toArray()[0];
                String fat = fatNutrient.amount + " " + fatNutrient.nutrient.unitName;
                MealFile.FoodNutrient carbNutrient = (MealFile.FoodNutrient) food.foodNutrients.stream().filter(f -> f.nutrient.name.equals("Carbohydrate, by difference")).toArray()[0];
                String carb = carbNutrient.amount + " " + carbNutrient.nutrient.unitName;
                MealFile.FoodNutrient calNutrient = (MealFile.FoodNutrient) food.foodNutrients.stream().filter(f -> f.nutrient.name.equals("Energy")).toArray()[0];
                String cal = calNutrient.amount + " " + calNutrient.nutrient.unitName;
                MealFile.FoodNutrient vitaANutrient = (MealFile.FoodNutrient) food.foodNutrients.stream().filter(f -> f.nutrient.name.equals("Vitamin A, RAE")).toArray()[0];
                String vitaA = vitaANutrient.amount + " " + vitaANutrient.nutrient.unitName;

                MealFile.FoodNutrient cholesterolNutrient = (MealFile.FoodNutrient) food.foodNutrients.stream().filter(f -> f.nutrient.name.equals("Cholesterol")).toArray()[0];
                String cholesterol = cholesterolNutrient.amount + " " + cholesterolNutrient.nutrient.unitName;
                MealFile.FoodNutrient sodiumNutrient = (MealFile.FoodNutrient) food.foodNutrients.stream().filter(f -> f.nutrient.name.equals("Sodium, Na")).toArray()[0];
                String sodium = sodiumNutrient.amount + " " + sodiumNutrient.nutrient.unitName;
                MealFile.FoodNutrient vitaBNutrient = (MealFile.FoodNutrient) food.foodNutrients.stream().filter(f -> f.nutrient.name.equals("Vitamin B-6")).toArray()[0];
                String vitaB = vitaBNutrient.amount + " " + vitaBNutrient.nutrient.unitName;
                MealFile.FoodNutrient vitaCNutrient = (MealFile.FoodNutrient) food.foodNutrients.stream().filter(f -> f.nutrient.name.equals("Vitamin C, total ascorbic acid")).toArray()[0];
                String vitaC = vitaCNutrient.amount + " " + vitaCNutrient.nutrient.unitName;
                MealFile.FoodNutrient vitaDNutrient = (MealFile.FoodNutrient) food.foodNutrients.stream().filter(f -> f.nutrient.name.equals("Vitamin D (D2 + D3)")).toArray()[0];
                String vitaD = vitaDNutrient.amount + " " + vitaDNutrient.nutrient.unitName;
                MealFile.FoodNutrient calciumNutrient = (MealFile.FoodNutrient) food.foodNutrients.stream().filter(f -> f.nutrient.name.equals("Calcium, Ca")).toArray()[0];
                String calcium = calciumNutrient.amount + " " + calciumNutrient.nutrient.unitName;
                MealFile.FoodNutrient ironNutrient = (MealFile.FoodNutrient) food.foodNutrients.stream().filter(f -> f.nutrient.name.equals("Iron, Fe")).toArray()[0];
                String iron = ironNutrient.amount + " " + ironNutrient.nutrient.unitName;

                importMeals.add(new Meal(name, cal, carb, fat, protein, 1, vitaA, cholesterol, sodium, vitaB, vitaC, vitaD, calcium, iron));
            }

            Meal[] meals = new Meal[importMeals.size()];
            for (int i = 0; i < importMeals.size(); i++) {
                meals[i] = importMeals.get(i);
            }
            this.dataRequest.onDataReceived(meals);
        } catch (Exception e) {
            // we never know :)
            Log.e("error parsing", e.toString());
        }
    }

    private String readFromAsset(final Activity act, final String fileName) {
        String text = "";
        try {
            InputStream is = act.getAssets().open(fileName);

            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}

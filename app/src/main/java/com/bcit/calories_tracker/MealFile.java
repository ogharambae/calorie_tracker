package com.bcit.calories_tracker;

import java.util.ArrayList;

public class MealFile {
    // import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
    public class Nutrient {
        public int id;
        public String number;
        public String name;
        public int rank;
        public String unitName;
    }

    public class FoodNutrient {
        public String type;
        public int id;
        public Nutrient nutrient;
        public double amount;
    }

    public class FoodAttributeType {
        public int id;
        public String name;
        public String description;
    }

    public class FoodAttribute {
        public int id;
        public String name;
        public String value;
        public FoodAttributeType foodAttributeType;
    }

    public class WweiaFoodCategory {
        public int wweiaFoodCategoryCode;
        public String wweiaFoodCategoryDescription;
    }

    public class InputFood {
        public int id;
        public String unit;
        public String portionDescription;
        public String portionCode;
        public String foodDescription;
        public int sequenceNumber;
        public double amount;
        public int ingredientCode;
        public double ingredientWeight;
        public String ingredientDescription;
    }

    public class MeasureUnit {
        public int id;
        public String name;
        public String abbreviation;
    }

    public class FoodPortion {
        public int id;
        public MeasureUnit measureUnit;
        public String modifier;
        public double gramWeight;
        public int sequenceNumber;
        public String portionDescription;
    }

    public class SurveyFood {
        public String foodClass;
        public String description;
        public ArrayList<FoodNutrient> foodNutrients;
        public ArrayList<FoodAttribute> foodAttributes;
        public String foodCode;
        public String startDate;
        public String endDate;
        public WweiaFoodCategory wweiaFoodCategory;
        public int fdcId;
        public String dataType;
        public String publicationDate;
        public ArrayList<InputFood> inputFoods;
        public ArrayList<FoodPortion> foodPortions;
    }

    public class Root {
        public ArrayList<SurveyFood> surveyFoods;
    }


}

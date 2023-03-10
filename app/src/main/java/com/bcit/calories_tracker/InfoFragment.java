package com.bcit.calories_tracker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    FirebaseFirestore db;
    Meal[] meals;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Meal[] mParam1;
    private String mParam2;

    public InfoFragment() {
        // Required empty public constructor
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param meals Parameter 1.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(Meal[] meals) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, meals);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Meal[]) getArguments().getSerializable(ARG_PARAM1);
        }

        if (mParam1 != null && mParam1.length > 0) {
            meals = mParam1;
        } else {
            loadJSONFromHttpRequest();
//            loadJSONFromAsset();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar_info);
        // Hide progress bar if data is loaded
        if (meals != null && meals.length > 0) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        EditText editText = view.findViewById(R.id.editText_info_search);
        Button button = view.findViewById(R.id.button_info);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_info);
        TextView textView = view.findViewById(R.id.textView_info_noResults);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userSearch = editText.getText().toString();
                if (userSearch.equals("")) {
                    textView.setText("");
                    recyclerView.setVisibility(View.GONE);
                } else {
                    List<Meal> resultList = getSearchResult(userSearch);
                    if (resultList.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        textView.setText("");
                        Meal[] results = getSearchResult(userSearch).toArray(new Meal[resultList.size()]);
                        InfoRecyclerViewAdapter adapter = new InfoRecyclerViewAdapter(results);
                        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                        recyclerView.setAdapter(adapter);
                    } else {
                        textView.setText("Food item of that name does not exist!");
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                EditText editText = view.findViewById(R.id.editText_info_search);
                String userSearch = editText.getText().toString();
                List<Meal> resultList = getSearchResult(userSearch);
                TextView textView = view.findViewById(R.id.textView_info_noResults);
                if (resultList.size() > 0) {
                    textView.setText("");
                    Meal[] results = getSearchResult(userSearch).toArray(new Meal[resultList.size()]);
                    InfoRecyclerViewAdapter adapter = new InfoRecyclerViewAdapter(results);
                    RecyclerView recyclerView = view.findViewById(R.id.recyclerView_info);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    recyclerView.setAdapter(adapter);
                } else {
                    textView.setText("Food item of that name does not exist!");
                }

                LinearLayout layout = view.findViewById(R.id.layout_info);
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
            }
        });
    }

    public void loadJSONFromHttpRequest() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        MainActivity mainActivity = (MainActivity) getActivity();
        MealImporter mealImporter = new MealImporter(getActivity(), new DataRequest() {
            @Override
            public void onDataReceived(Meal[] importedMeals) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        meals = importedMeals;
                        mainActivity.setFoods(meals);
                    }
                });
            }
        });
        executorService.submit(mealImporter);
    }

    public void loadJSONFromAsset() {
        String json = readFromAsset(getActivity(), "meals.json");
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

            meals = new Meal[importMeals.size()];
            for (int i = 0; i < importMeals.size(); i++) {
                meals[i] = importMeals.get(i);
            }
            ((MainActivity) getActivity()).setFoods(meals);
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

    public void getFoodItems() {
        List<Meal> listOfFood = new ArrayList<>();
        db.collection("foods")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Debug", document.getData().toString());

                                listOfFood.add(
                                        new Meal(
                                                document.getData().get("name").toString(),
                                                document.getData().get("cal").toString(),
                                                document.getData().get("carb").toString(),
                                                document.getData().get("fat").toString(),
                                                document.getData().get("protein").toString(),
                                                1,
                                                document.getData().get("vitaA").toString(),
                                                document.getData().get("cholesterol").toString(),
                                                document.getData().get("sodium").toString(),
                                                document.getData().get("vitaB").toString(),
                                                document.getData().get("vitaC").toString(),
                                                document.getData().get("vitaD").toString(),
                                                document.getData().get("calcium").toString(),
                                                document.getData().get("iron").toString()

                                        )
                                );
                            }
                        } else {
                            Log.w("Debug", "Error getting documents.", task.getException());
                        }
                        meals = listOfFood.toArray(new Meal[listOfFood.size()]);
                    }
                });
    }

    public List<Meal> getSearchResult(String searchKeyWord) {
        List<Meal> mealList = new ArrayList<>();
        for (Meal meal : meals) {
            if (meal.getName().contains(searchKeyWord)) {
                mealList.add(meal);
            }
        }
        return mealList;
    }

}
package com.bcit.calories_tracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomepageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomepageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseFirestore db;
    private ArrayList<Meal> user_meals;


    public HomepageFragment() {
        db = FirebaseFirestore.getInstance();
        user_meals = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomepageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomepageFragment newInstance() {
        HomepageFragment fragment = new HomepageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    private void populateRecycler(View view) {
        Meal[] allMeals = user_meals.toArray(new Meal[user_meals.size()]);
        RecyclerView rcv = view.findViewById(R.id.recycler_homepage);
        HomePageMealsRecycler mealAdapter =
                new HomePageMealsRecycler(allMeals);
        rcv.setAdapter(mealAdapter);
        rcv.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void populateTotalCaloriesBurned(View view) {

        String cals = Meal.calculateTotalCal(user_meals);
        TextView caloriesBurnedToday = view.findViewById(R.id.cals_burned_homepage);
        ProgressBar caloriesBar = view.findViewById(R.id.calories_bar_homepage);
        caloriesBar.setMax(2000);
        caloriesBar.setProgress(Integer.parseInt(cals));
        String yourCalories = cals + " Calories";
        caloriesBurnedToday.setText(yourCalories);
    }

    private void populateMeal(View view, String userID) {
        DocumentReference docRef = db.collection("input-meals")
                .document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("DocumentSnapshot data: ", document.getData().toString());
                        HashMap documentData = (HashMap) document.getData();
                        HashMap dayOfFood = (HashMap) documentData.get(Meal.getMealDate());

                        if (dayOfFood == null) {
                            displayNoMealInputToday(view);
                        } else {
                            displayMealToday(view, dayOfFood);
                        }
                    } else {
                        Log.d("No such document", "Nothing");
                    }
                } else {
                    Log.d("get failed with ", task.getException().toString());
                }
            }
        });
    }

    void displayNoMealInputToday(View view) {
        CardView cardView = view.findViewById(R.id.cardView_homepage);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_homepage);

        cardView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    void displayMealToday(View view, HashMap dayOfFood) {
        CardView cardView = view.findViewById(R.id.cardView_homepage);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_homepage);

        cardView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        ArrayList allMeals = (ArrayList) dayOfFood.get("meals");

        for (int i = 0; i < allMeals.size(); i++) {
            HashMap meal =
                    (HashMap) allMeals.get(i);

            user_meals.add(
                    new Meal((String) meal.get("name"),
                            (String) meal.get("cal"),
                            (String) meal.get("carb"),
                            (String) meal.get("fat"),
                            (String) meal.get("protein"),
                            2,
                            (String) meal.get("vitaminA"),
                            (String) meal.get("cholesterol"),
                            (String) meal.get("sodium"),
                            (String) meal.get("vitaminB"),
                            (String) meal.get("vitaminC"),
                            (String) meal.get("vitaminD"),
                            (String) meal.get("calcium"),
                            (String) meal.get("iron")
                    ));

            populateRecycler(view);
            populateTotalCaloriesBurned(view);
        }
    }

    void getMeals(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            String userId = user.getUid();
            populateMeal(view, userId);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMeals(view);
        Button button = view.findViewById(R.id.button_homepage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).switchToInfoFragment();
            }
        });
    }
}
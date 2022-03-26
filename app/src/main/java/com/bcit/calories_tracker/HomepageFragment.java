package com.bcit.calories_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
//    private String userId;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomepageFragment() {
        db = FirebaseFirestore.getInstance();
        user_meals = new ArrayList<>();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        userId = user.getUid();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomepageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomepageFragment newInstance(String param1, String param2) {
        HomepageFragment fragment = new HomepageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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


    void getMeals(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        DocumentReference docRef = db.collection("input-meals")
                .document(userId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("DocumentSnapshot data: ", document.getData().toString());

                        HashMap dayOfFood = (HashMap) document.getData().get(Meal.getMealDate());
                        ArrayList allMeals = (ArrayList) dayOfFood.get("meals");

                        System.out.println(allMeals);

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
                    } else {
                        Log.d("No such document", "Nothing");
                    }
                } else {
                    Log.d("get failed with ", task.getException().toString());
                }
            }
        });


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
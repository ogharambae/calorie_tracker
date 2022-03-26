package com.bcit.calories_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Meal mealDetails;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public MealDetailFragment(Meal meal) {
        // Required empty public constructor
        mealDetails = meal;
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mealDetails Parameter 1.
     * @return A new instance of fragment MealDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MealDetailFragment newInstance(Meal mealDetails) {
        MealDetailFragment fragment = new MealDetailFragment(mealDetails);
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, mealDetails);
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
        return inflater.inflate(R.layout.fragment_meal_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView name = view.findViewById(R.id.mealDetail_fragment_meal_details);
        TextView protein = view.findViewById(R.id.protein_mealDetails);
        TextView fat = view.findViewById(R.id.fat_mealDetails);
        TextView calories = view.findViewById(R.id.cal_mealDetails);
        TextView carb = view.findViewById(R.id.carbs_mealDetails);

        name.setText(mealDetails.getName());
        protein.setText(mealDetails.getProtein() + "\nProtein");
        fat.setText(mealDetails.getFat() + "\nFat");
        calories.setText(mealDetails.getCal());
        carb.setText(mealDetails.getCarb() + "\nCarbs");

        Button button = view.findViewById(R.id.button_meal_detail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                String date = Meal.getMealDate();

                DocumentReference docRef = db.collection("input-meals").document(userId);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            if (document.exists()) {
                                HashMap<String, Object> data = (HashMap<String, Object>) document.getData();
                                ArrayList<Object> existedMeals = null;

                                for (String key : data.keySet()) {
                                    if (key.equals(date)) {
                                        existedMeals = (ArrayList<Object>) ((HashMap<String, Object>) data.get(key)).get("meals");
                                    }
                                }

                                if (existedMeals != null) {
                                    HashMap<String, ArrayList<Object>> mData = new HashMap<>();
                                    existedMeals.add(mealDetails);
                                    mData.put("meals", existedMeals);

                                    db.collection("input-meals")
                                            .document(userId)
                                            .update(FieldPath.of(date), mData);
                                } else {
                                    HashMap<String, ArrayList<Meal>> mData = new HashMap<>();
                                    ArrayList<Meal> meals = new ArrayList<>();
                                    meals.add(mealDetails);
                                    mData.put("meals", meals);

                                    db.collection("input-meals")
                                            .document(userId)
                                            .update(FieldPath.of(date), mData);
                                }
                            } else {
                                HashMap<String, ArrayList<Meal>> mData = new HashMap<>();
                                ArrayList<Meal> meals = new ArrayList<>();
                                meals.add(mealDetails);
                                mData.put("meals", meals);

                                db.collection("input-meals")
                                        .document(userId)
                                        .update(FieldPath.of(date), mData);
                            }
                        }
                    }
                });
            }
        });
    }
}
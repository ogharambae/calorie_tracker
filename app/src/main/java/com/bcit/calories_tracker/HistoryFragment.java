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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseFirestore db;
    private ArrayList<Meal[]> user_meals;
    private Integer total_calories;
    private String userId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        db = FirebaseFirestore.getInstance();
        user_meals = new ArrayList<>();
        total_calories = 0;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMeals(view);

    }


    void getMeals(View view) {
        DocumentReference docRef = db.collection("input-meals").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        HashMap<String, Object> data = (HashMap<String, Object>) document.getData();
                        for (String key : data.keySet()) {
                            ArrayList<Object> meals = (ArrayList<Object>) ((HashMap<String, Object>) data.get(key)).get("meals");
                            Meal[] dailyMeals = new Meal[meals.size()];
                            int idx = 0;
                            for (Object meal : meals) {
                                Meal m = new Meal(
                                        ((HashMap<String, String>) meal).get("name"),
                                        ((HashMap<String, String>) meal).get("cal"),
                                        ((HashMap<String, String>) meal).get("carb"),
                                        ((HashMap<String, String>) meal).get("fat"),
                                        ((HashMap<String, String>) meal).get("protein"),
                                        key,
                                        1
                                );
                                dailyMeals[idx] = m;
                                idx++;
                            }
                            user_meals.add(dailyMeals);
                        }


                        RecyclerView rcv = view.findViewById(R.id.recyclerView_history);
                        HistoryRecyclerViewAdapter mealAdapter =
                                new HistoryRecyclerViewAdapter(user_meals);
                        rcv.setAdapter(mealAdapter);
                        rcv.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    } else {
                        // TODO: Handle no meal
                        System.out.println();
                    }
                } else {
                    Toast.makeText(view.getContext(),
                            "Something's wrong, please try again!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
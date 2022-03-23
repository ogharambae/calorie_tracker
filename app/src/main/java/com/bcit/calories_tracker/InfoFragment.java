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
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    FirebaseFirestore db;
    Meal[] meals;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoFragment() {
        // Required empty public constructor
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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
        getFoodItems();
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
        // Search function goes here

        Button button = view.findViewById(R.id.button_info);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                EditText editText = view.findViewById(R.id.editText_info_search);
                String userSearch = editText.getText().toString();
                Meal[] results = getSearchResult(userSearch).toArray(new Meal[meals.length]);
                InfoRecyclerViewAdapter adapter = new InfoRecyclerViewAdapter(results);
                RecyclerView recyclerView = view.findViewById(R.id.recyclerView_info);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.setAdapter(adapter);
            }
        });
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
                                                1
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
        for (Meal meal: meals) {
            if (meal.getName().contains(searchKeyWord)) {
                mealList.add(meal);
            }
        }
        return mealList;
    }

}
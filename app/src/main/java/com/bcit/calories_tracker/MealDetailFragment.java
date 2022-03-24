package com.bcit.calories_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public MealDetailFragment(Meal meal) {
        // Required empty public constructor
        mealDetails = meal;
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
        TextView calories = view.findViewById(R.id.calories_mealDetails);
        TextView carb = view.findViewById(R.id.carb_mealDetails);

        name.setText(mealDetails.getName());
        protein.setText(mealDetails.getProtein());
        fat.setText(mealDetails.getFat());
        calories.setText(mealDetails.getCal());
        carb.setText(mealDetails.getCarb());

    }
}
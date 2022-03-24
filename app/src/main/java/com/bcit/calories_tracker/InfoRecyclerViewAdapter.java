package com.bcit.calories_tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


public class InfoRecyclerViewAdapter extends
        RecyclerView.Adapter<InfoRecyclerViewAdapter.ViewHolder> {

    private Meal[] meals;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView cal;
        private View thisView;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textView_info_item_name);
            cal = view.findViewById(R.id.textView_info_item_cal);
            thisView = view;
        }

        public TextView getName() {
            return name;
        }

        public TextView getCal() {
            return cal;
        }

        public View getThisView() {
            return thisView;
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public InfoRecyclerViewAdapter(Meal[] dataSet) {
        meals = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.info_item_meal, viewGroup, false); //error here should be expected, this is a template

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Meal meal = meals[position];
        viewHolder.getName().setText(meals[position].getName());
        viewHolder.getCal().setText(meals[position].getCal());

        View clickResult = viewHolder.getThisView();
        clickResult.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println("Yes");

                AppCompatActivity activity = (AppCompatActivity) clickResult.getContext();
                Fragment mealDetails = new MealDetailFragment(meal);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView_main, mealDetails)
                        .addToBackStack(null).commit();

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return meals.length;
    }
}

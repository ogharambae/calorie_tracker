package com.bcit.calories_tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class HomePageMealsRecycler extends RecyclerView.Adapter<HomePageMealsRecycler.ViewHolder> {

    private Meal[] localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodName;
        private final TextView calories;
        private final TextView fat;

        public ViewHolder(View view) {
            super(view);
            foodName = view.findViewById(R.id.foodName_homepage);
            calories = view.findViewById(R.id.cals_homepage);
            fat = view.findViewById(R.id.fat_homepage);

        }

        public TextView getFoodName() {
            return foodName;
        }

        public TextView getCalories() {
            return calories;
        }

        public TextView getFat() {
            return fat;
        }


    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public HomePageMealsRecycler(Meal[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.homepage_recycler, viewGroup, false); //error here should be expected, this is a template

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getFoodName().setText(localDataSet[position].getName());
        viewHolder.getCalories().setText(localDataSet[position].getCal());
        viewHolder.getFat().setText(localDataSet[position].getFat());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
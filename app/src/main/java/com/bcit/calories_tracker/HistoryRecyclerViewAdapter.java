package com.bcit.calories_tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;


public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Meal[]> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTotalCal;
        private final TextView textViewDate;

        public ViewHolder(View view) {
            super(view);

            textViewTotalCal = view.findViewById(R.id.textView_history_item_totalcal); //error here should be expected, this is a template
            textViewDate = view.findViewById(R.id.textView_history_item_date); //error here should be expected, this is a template
        }

        public TextView getTextViewTotalCal() {
            return textViewTotalCal;
        }

        public TextView getTextViewDate() {
            return textViewDate;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public HistoryRecyclerViewAdapter(ArrayList<Meal[]> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history_item_meal, viewGroup, false); //error here should be expected, this is a template

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Meal[] meals = localDataSet.get(position);
        viewHolder.getTextViewTotalCal().setText(Meal.calculateTotalCal(meals));
        viewHolder.getTextViewDate().setText(meals[0].getDate());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
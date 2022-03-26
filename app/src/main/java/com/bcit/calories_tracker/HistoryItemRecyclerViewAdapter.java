package com.bcit.calories_tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class HistoryItemRecyclerViewAdapter extends RecyclerView.Adapter<HistoryItemRecyclerViewAdapter.ViewHolder> {

    private Meal[] localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewItemIdx;
        private final TextView textViewItemName;
        private final TextView textViewItemProtein;
        private final TextView textViewItemFat;
        private final TextView textViewItemCarb;
        private final TextView textViewItemCal;

        public ViewHolder(View view) {
            super(view);

            textViewItemIdx = view.findViewById(R.id.textView_history_detail_item_idx);
            textViewItemName = view.findViewById(R.id.textView_history_detail_name);
            textViewItemCal = view.findViewById(R.id.textView_history_detail_cal);
            textViewItemFat = view.findViewById(R.id.textView_history_detail_fat);
            textViewItemProtein = view.findViewById(R.id.textView_history_detail_protein);
            textViewItemCarb = view.findViewById(R.id.textView_history_detail_carb);
        }

        public TextView getTextViewItemIdx() {
            return textViewItemIdx;
        }

        public TextView getTextViewItemName() {
            return textViewItemName;
        }

        public TextView getTextViewItemProtein() {
            return textViewItemProtein;
        }

        public TextView getTextViewItemFat() {
            return textViewItemFat;
        }

        public TextView getTextViewItemCarb() {
            return textViewItemCarb;
        }

        public TextView getTextViewItemCal() {
            return textViewItemCal;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public HistoryItemRecyclerViewAdapter(Meal[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history_item_detail_meal, viewGroup, false); //error here should be expected, this is a template

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Meal m = localDataSet[position];
        viewHolder.getTextViewItemIdx().setText("" + (position + 1));
        viewHolder.getTextViewItemName().setText(Meal.convertToTitleCaseIteratingChars(m.getName()));
        viewHolder.getTextViewItemCal().setText("Calories: " + m.getCal());
        viewHolder.getTextViewItemFat().setText("Fat: " + m.getFat());
        viewHolder.getTextViewItemProtein().setText("Protein: " + m.getProtein());
        viewHolder.getTextViewItemCarb().setText("Carb: " + m.getCarb());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }


}
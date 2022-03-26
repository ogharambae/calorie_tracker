package com.bcit.calories_tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Meal[]> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTotalCal;
        private final TextView textViewDate;
        private View thisView;

        public ViewHolder(View view) {
            super(view);

            textViewTotalCal = view.findViewById(R.id.textView_history_detail_cal);
            textViewDate = view.findViewById(R.id.textView_history_item_date);
            thisView = view;
        }

        public TextView getTextViewTotalCal() {
            return textViewTotalCal;
        }

        public TextView getTextViewDate() {
            return textViewDate;
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
        View wholeView = viewHolder.getThisView();

        RecyclerView rcv = wholeView.findViewById(R.id.recyclerView_item_history);
        HistoryItemRecyclerViewAdapter mealAdapter =
                new HistoryItemRecyclerViewAdapter(meals);
        rcv.setAdapter(mealAdapter);
        rcv.setLayoutManager(new LinearLayoutManager(wholeView.getContext()));
//
//        wholeView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AppCompatActivity activity = (AppCompatActivity) wholeView.getContext();
//                Fragment fragment = HistoryDetailFragment.newInstance("test", "test");
//                activity.getSupportFragmentManager()
//                        .beginTransaction()
//                        .setCustomAnimations(
//                                R.anim.slide_in,  // enter
//                                R.anim.fade_out,  // exit
//                                R.anim.fade_in,   // popEnter
//                                R.anim.slide_out  // popExit
//                        )
//                        .replace(R.id.fragmentContainerView_main, fragment)
//                        .commit();
//
//            }
//        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
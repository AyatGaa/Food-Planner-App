package com.example.foodplanner.planscreen.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.example.foodplanner.R;
import com.example.foodplanner.favortitescreen.view.FavoriteAdapter;
import com.example.foodplanner.favortitescreen.view.OnDeleteMealClickListener;
import com.example.foodplanner.homescreen.view.HomeScreenFragmentDirections;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    Context context;
    List<PlannedMeal> mealPlannedList;
    OnDeletePlanMealListener onDeletePlanMealListener;

    public PlanAdapter(Context context, List<PlannedMeal> mealPlannedList, OnDeletePlanMealListener onDeletePlanMealListener) {
        this.context = context;
        this.mealPlannedList = mealPlannedList;
        this.onDeletePlanMealListener = onDeletePlanMealListener;
    }

    public void updateMeals(List<PlannedMeal> newMeals) {
        this.mealPlannedList = newMeals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.plan_meal_card, parent, false);
        PlanAdapter.ViewHolder vh = new PlanAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.ViewHolder holder, int position) {
        PlannedMeal meal = mealPlannedList.get(position);

        holder.mealPlanName.setText(meal.getMealName());

        Glide.with(context).load(meal.getMealImage())
                .error(R.drawable.cutlery_primary_color)
                .into(holder.mealPlanImage);

        holder.deletePlanBtn.setOnClickListener(view -> {
            onDeletePlanMealListener.onDeletePlanMealClick(meal);
        });

        holder.planCardConstrainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Meal m = new Meal();
                m.setIdMeal(meal.getIdMeal());
                PlanFragmentDirections.ActionPlanFragmentToDetailedMealFragment action =
                        PlanFragmentDirections.actionPlanFragmentToDetailedMealFragment(m);

                Navigation.findNavController(v).navigate(action);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mealPlannedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mealPlanName;
        public ImageView mealPlanImage;
        public ImageButton deletePlanBtn;
        ConstraintLayout planCardConstrainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mealPlanName = itemView.findViewById(R.id.mealPlanName);
            mealPlanImage = itemView.findViewById(R.id.mealPlanImage);
            deletePlanBtn = itemView.findViewById(R.id.deletePlanBtn);
            planCardConstrainLayout = itemView.findViewById(R.id.planCardConstrainLayout);
        }
    }
}

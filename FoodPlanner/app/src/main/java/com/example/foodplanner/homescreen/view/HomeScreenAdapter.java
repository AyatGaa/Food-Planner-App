package com.example.foodplanner.homescreen.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.utils.AppFunctions;

import java.util.List;

public class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenAdapter.ViewHolder> {
    List<Meal> mealList;
    Context context;
    OnMealClickListener onMealClickListener;

    public HomeScreenAdapter(List<Meal> mealList, Context context, OnMealClickListener onMealClickListener) {
        this.mealList = mealList;
        this.context = context;
        this.onMealClickListener = onMealClickListener;
    }


    @NonNull
    @Override
    public HomeScreenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.meal_card, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull HomeScreenAdapter.ViewHolder holder, int position) {
        Meal meal = mealList.get(position);

        holder.txtMealName.setText(meal.getStrMeal());

        Glide.with(context).load(meal.getStrMealThumb())
                .error(R.drawable.notfound)
                .into(holder.mealCardImage);

        String countryCode = AppFunctions.getCountryCode(meal.getStrArea()).toLowerCase();
        Glide.with(context).load("https://flagcdn.com/w320/" + countryCode + ".png")
                .error(R.drawable.notfound)
                .into(holder.countryFlag);

        holder.itemView.setOnClickListener(view -> {
            onMealClickListener.onMealClick(meal);
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void setList(List<Meal> meals) {
        mealList = meals;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtMealName;
        public ImageView mealCardImage, countryFlag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMealName = itemView.findViewById(R.id.mealName);
            mealCardImage = itemView.findViewById(R.id.mealCardImage);
            countryFlag = itemView.findViewById(R.id.countryFlag);
        }
    }
}

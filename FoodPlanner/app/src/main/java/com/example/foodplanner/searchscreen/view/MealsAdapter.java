package com.example.foodplanner.searchscreen.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.homescreen.view.HomeScreenAdapter;
import com.example.foodplanner.homescreen.view.OnMealClickListener;
import com.example.foodplanner.utils.AppFunctions;

import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.ViewHolder> {

    List<Meal> mealList;
    Context context;
    OnMealClickListener onMealClickListener;

    public MealsAdapter(List<Meal> mealList, Context context, OnMealClickListener onMealClickListener) {
        this.mealList = mealList;
        this.context = context;
        this.onMealClickListener = onMealClickListener;
    }


    @NonNull
    @Override
    public MealsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.meal_card, parent, false);
        MealsAdapter.ViewHolder vh = new MealsAdapter.ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull MealsAdapter.ViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        Log.i("TAG", "onBindViewHolder: hehr " + meal.getIdMeal());
        holder.txtMealName.setText(meal.getStrMeal());

        Glide.with(context).load(meal.getStrMealThumb())
                .error(R.drawable.ic_launcher_background)
                .into(holder.mealCardImage);

        String countryCode = AppFunctions.getCountryCode(meal.getStrArea()).toLowerCase();
        Log.i("TAG", "onBindViewHolder: " + countryCode);
        Glide.with(context).load("https://flagcdn.com/w320/" + countryCode + ".png")
                .error(R.drawable.ic_launcher_background)
                .into(holder.countryFlag);
//        holder.itemView.setOnClickListener(view -> {
//            onMealClickListener.onMealClick(meal);
//            Log.i("TAG", "onClick: meal clicked");
//        });


        Log.i("TAG", "onBindViewHolder: MEAl clicked");
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

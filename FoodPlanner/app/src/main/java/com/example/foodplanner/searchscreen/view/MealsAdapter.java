package com.example.foodplanner.searchscreen.view;

import android.content.Context;
import android.util.Log;
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

import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.ViewHolder> {

    List<Meal> mealList;
    Context context;
    OnSearchMealClickListener onMealClickListener;

    public MealsAdapter(List<Meal> mealList, Context context, OnSearchMealClickListener onMealClickListener) {
        this.mealList = mealList;
        this.context = context;
        this.onMealClickListener = onMealClickListener;
    }


    @NonNull
    @Override
    public MealsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.meal_search_card, parent, false);
        MealsAdapter.ViewHolder vh = new MealsAdapter.ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull MealsAdapter.ViewHolder holder, int position) {
        Meal meal = mealList.get(position);



         holder.txtMealName.setText(meal.getStrMeal());

        Glide.with(context).load(meal.getStrMealThumb())
                .error(R.drawable.ic_launcher_background)
                .into(holder.mealCardImage);

//        String countryCode = AppFunctions.getCountryCode(meal.getStrArea()).toLowerCase();
//        Log.d("SearchFragment", "onBindViewHolder: "+countryCode );
//        Glide.with(context).load("https://flagcdn.com/w320/" + countryCode + ".png")
//                .error(R.drawable.ic_launcher_background)
//                .into(holder.countryFlag);

        holder.itemView.setOnClickListener(view -> {
            onMealClickListener.onSearchMealClick(meal);
        });


        Log.i("TAG", "onBindViewHolder: MEAl clicked");
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void setList(List<Meal> meals) {
        this.mealList.clear();
        this.mealList.addAll(meals);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtMealName;

        public ImageView mealCardImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMealName = itemView.findViewById(R.id.mealSearchCardName);
            mealCardImage = itemView.findViewById(R.id.mealSearchCardImage);

//            txtMealName = itemView.findViewById(R.id.mealName);
//            mealCardImage = itemView.findViewById(R.id.mealCardImage);


        }
    }

}

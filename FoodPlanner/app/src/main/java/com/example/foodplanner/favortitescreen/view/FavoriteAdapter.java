package com.example.foodplanner.favortitescreen.view;

import android.content.Context;
import android.util.Log;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.homescreen.view.HomeScreenAdapter;
import com.example.foodplanner.utils.AppFunctions;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    Context context;
    List<Meal> mealFavoriteList;
    OnDeleteMealClickListener onDeleteMealClickListener;

    public FavoriteAdapter(Context context, List<Meal> mealFavoriteList, OnDeleteMealClickListener onDeleteMealClickListener) {
        this.onDeleteMealClickListener = onDeleteMealClickListener;
        this.context = context;
        this.mealFavoriteList = mealFavoriteList;
    }
    public void updateData(List<Meal> newMeals) {
        this.mealFavoriteList.clear();
        this.mealFavoriteList.addAll(newMeals);
        Log.d("fav", "Adapter updated with " + newMeals.size() + " meals"); // Debugging

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.favorite_meal_card, parent, false);
        FavoriteAdapter.ViewHolder vh = new FavoriteAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        Meal meal = mealFavoriteList.get(position);
        Log.i("TAG", "onBindViewHolder: hehr " + meal.getIdMeal());
        holder.mealFavoriteName.setText(meal.getStrMeal());

        Glide.with(context).load(meal.getStrMealThumb())
                .error(R.drawable.cutlery_primary_color)
                .into(holder.mealFavoriteImage);
        holder.mealFavoriteCategory.setText(meal.getStrCategory());


        holder.deleteFavoriteBtn.setOnClickListener(view -> {
            onDeleteMealClickListener.onDeleteMealClick(meal);
            Log.i("TAG", "onClick: meal delted");
        });


        Log.i("TAG", "onBindViewHolder: MEAl clicked");
    }

    @Override
    public int getItemCount() {
        return mealFavoriteList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView mealFavoriteName,mealFavoriteCategory;
        public ImageView mealFavoriteImage;
        public ImageButton deleteFavoriteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mealFavoriteName = itemView.findViewById(R.id.mealFavoriteName);
            mealFavoriteImage = itemView.findViewById(R.id.mealFavoriteImage);
            mealFavoriteCategory = itemView.findViewById(R.id.mealFavoriteCategory);
            deleteFavoriteBtn = itemView.findViewById(R.id.deleteFavoriteBtn);
        }
    }
}

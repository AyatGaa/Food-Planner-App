package com.example.foodplanner.detailedmeal.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Models.ingredient.Ingredient;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.homescreen.view.HomeScreenAdapter;
import com.example.foodplanner.utils.AppFunctions;

import java.util.List;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    List<String> ingredientList;
    Context context;


    public IngredientAdapter(List<String> ingredientList, Context context) {
        this.ingredientList = ingredientList;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredient_card, parent, false);
        IngredientAdapter.ViewHolder vh = new IngredientAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        String ingred = ingredientList.get(position);
        Log.i("TAG", "onBindViewHolder: hehr " + ingred);


        holder.ingredientName.setText(ingred);


        Log.i("TAG", "ingreddienttss: " + ingred);

        Glide.with(context).load("https://www.themealdb.com/images/ingredients/" + ingred + ".png")
                .error(R.drawable.ic_launcher_background)
                .into(holder.ingredientImage);

        Log.i("TAG", "onBindViewHolder: MEAl clicked");
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView ingredientImage;
        TextView ingredientName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
            ingredientName = itemView.findViewById(R.id.ingredientName);
        }
    }
}

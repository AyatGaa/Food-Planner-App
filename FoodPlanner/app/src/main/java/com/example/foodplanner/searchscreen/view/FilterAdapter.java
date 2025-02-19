package com.example.foodplanner.searchscreen.view;

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
import com.example.foodplanner.Models.area.Area;
import com.example.foodplanner.Models.category.Category;
import com.example.foodplanner.Models.ingredient.Ingredient;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.homescreen.view.HomeScreenAdapter;
import com.example.foodplanner.utils.AppFunctions;

import java.util.List;

public class FilterAdapter<T> extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    Context context;
    List<T> filterList;

    public FilterAdapter(List<T> filterList, Context context) {
        this.context = context;

        this.filterList = filterList;
    }

    @NonNull
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_card, parent, false);
        FilterAdapter.ViewHolder vh = new FilterAdapter.ViewHolder(view);
        return vh;
    }


    public void updateList(List<T> newList) {
        this.filterList = newList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.ViewHolder holder, int position) {
        Object c = filterList.get(position);

        if (c instanceof Category) {
            holder.filterName.setText(((Category) c).getStrCategory());
            Glide.with(context).load(((Category) c).getStrCategoryThumb()).into(holder.filterImage);

        } else if (c instanceof Area) {
            holder.filterName.setText(((Area) c).getStrArea());

            String countryCode = AppFunctions.getCountryCode(((Area) c).getStrArea()).toLowerCase();
            Log.i("TAG", "onBindViewHolder: " + countryCode);
            Glide.with(context).load("https://flagcdn.com/w320/" + countryCode + ".png").into(holder.filterImage);

        } else if (c instanceof Ingredient) {

            holder.filterName.setText(((Ingredient) c).getStrIngredient());
            Ingredient ingredient = (Ingredient) c;
            String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredient.getStrIngredient() + ".png";
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.filterImage);

        } else if (c instanceof Meal) {

            holder.filterName.setText(((Meal) c).getStrMeal());
            Glide.with(context).load(((Meal) c).getStrMealThumb()).into(holder.filterImage);
        }

    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView filterName;
        ImageView filterImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filterName = itemView.findViewById(R.id.categoryName);
            filterImage = itemView.findViewById(R.id.categoryImage);
        }
    }
}

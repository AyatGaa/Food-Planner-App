package com.example.foodplanner.detailedmeal.view;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Models.ingredient.Ingredient;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.utils.AppFunctions;

import java.util.ArrayList;
import java.util.List;

public class DetailedMealFragment extends Fragment {


    ImageView mealImageDetailed, countryFlagDetailed;
    TextView mealNameDetailed, instructionsTextDetailed,detailedMealHeader,countryFlagName;
    RecyclerView ingredientsRecyclerView;
    IngredientAdapter ingredientsAdapter;
    Button btnAddToFavoritesDetailed, btnAddToPlanMealDetailed;
    List<String> ingredientList = new ArrayList<>();

    void setupUI(View view){
        ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        mealImageDetailed = view.findViewById(R.id.mealImageDetailed);
        mealNameDetailed = view.findViewById(R.id.mealNameDetailed);
        instructionsTextDetailed = view.findViewById(R.id.instructionsTextDetailed);
        btnAddToFavoritesDetailed = view.findViewById(R.id.btnAddToFavoritesDetailed);
        countryFlagDetailed = view.findViewById(R.id.countryFlagDetailed);
        detailedMealHeader = view.findViewById(R.id.detailedMealHeader);
        btnAddToPlanMealDetailed = view.findViewById(R.id.btnAddToPlanMealDetailed);
        countryFlagName = view.findViewById(R.id.countryFlagName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meal_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI(view);
        ingredientsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ingredientsRecyclerView.setLayoutManager(layoutManager);

        Meal meal = DetailedMealFragmentArgs.fromBundle(getArguments()).getDetailedMeal();
        ingredientList = getIngredient(meal);
        setIngredineUi(meal);

        ingredientsAdapter = new IngredientAdapter(ingredientList, requireContext());
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        ingredientsAdapter.notifyDataSetChanged();

    }
  void setIngredineUi(Meal meal){
      detailedMealHeader.setText( meal.getStrMeal());
        mealNameDetailed.setText(meal.getStrMeal());
      countryFlagName.setText(meal.getStrArea());
        String instructions = meal.getStrInstructions();
        instructions = instructions.replaceAll("\r\n|\r|\n", "\n\n");
        instructionsTextDetailed.setText(instructions);

      String countryCode = AppFunctions.getCountryCode(meal.getStrArea()).toLowerCase();

      Glide.with(requireContext()).load("https://flagcdn.com/w320/" + countryCode + ".png")
              .error(R.drawable.ic_launcher_background)
              .into(countryFlagDetailed);

      Glide.with(requireContext()).load(meal.getStrMealThumb())
              .error(R.drawable.ic_launcher_background)
              .into(mealImageDetailed);
    }

    List<String> getIngredient(Meal meal) {
        List<String> ingredientsList = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            try {
                String ingredient = (String) meal.getClass().getDeclaredField("strIngredient" + i).get(meal);
                if (ingredient != null && !ingredient.trim().isEmpty()) {
                    ingredientsList.add(ingredient);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i("TAG", "Extracted Ingredients: " + ingredientsList);
        return ingredientsList;
    }

}

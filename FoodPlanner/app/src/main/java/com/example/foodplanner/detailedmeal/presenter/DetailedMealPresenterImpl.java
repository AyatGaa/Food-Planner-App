package com.example.foodplanner.detailedmeal.presenter;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.Repository.modelrepoitory.PlanRepository;
import com.example.foodplanner.detailedmeal.view.DetailedMealView;
import com.example.foodplanner.utils.AppFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailedMealPresenterImpl implements DetailedMealPresenter{

    MealRepository mealRepository;
    PlanRepository planRepository;
    DetailedMealView detailedMealView;

    public DetailedMealPresenterImpl(MealRepository mealRepository, DetailedMealView detailedMealView , PlanRepository planRepository) {
        this.mealRepository = mealRepository;
        this.detailedMealView = detailedMealView;
        this.planRepository = planRepository;
    }

    @Override
    public void onAddToFavourite(Meal meal) {
            mealRepository.insertFavoriteMeal(meal);
    }

    @Override
    public void onAddToPlan(PlannedMeal planedMeal) {
        planRepository.insertPlannedMeal(planedMeal);
    }

    @Override
    public boolean isFutureDate(String selectedDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date mealDate = simpleDateFormat.parse(selectedDate);
            Date currentDate = new Date();

            return !mealDate.before(simpleDateFormat.parse(simpleDateFormat.format(currentDate)));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public String extractYoutubeVideoId(String url) {
        String pattern = "^(?:https?:\\/\\/)?(?:www\\.)?(?:youtube\\.com\\/(?:[^\\/]+\\/.*|(?:v|e(?:mbed)?)\\/|.*[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


    @Override
    public List<String> getIngredient(Meal meal) {
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

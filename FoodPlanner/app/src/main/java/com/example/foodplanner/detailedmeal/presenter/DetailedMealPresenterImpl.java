package com.example.foodplanner.detailedmeal.presenter;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.detailedmeal.view.DetailedMealView;

public class DetailedMealPresenterImpl implements DetailedMealPresenter{

    MealRepository mealRepository;
    DetailedMealView detailedMealView;

    public DetailedMealPresenterImpl(MealRepository mealRepository, DetailedMealView detailedMealView) {
        this.mealRepository = mealRepository;
        this.detailedMealView = detailedMealView;
    }

    @Override
    public void onAddToFavourite(Meal meal) {
            mealRepository.insertFavoriteMeal(meal);
    }
}

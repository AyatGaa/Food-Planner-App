package com.example.foodplanner.favortitescreen.presenter;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.favortitescreen.view.FavoriteScreenView;

public class FavoriteScreenPresenterImpl implements FavoriteScreenPresenter{
 private FavoriteScreenView favoriteScreenView;
 private MealRepository mealRepository;



    public FavoriteScreenPresenterImpl(FavoriteScreenView favoriteScreenView, MealRepository mealRepository) {
        this.favoriteScreenView = favoriteScreenView;
        this.mealRepository = mealRepository;
    }

   @Override
    public void getFavoriteMeals() {
        mealRepository.getAllFavouriteMeals();
    }

    @Override
    public void addMealToFavorite(Meal meal) {
        mealRepository.insertFavoriteMeal(meal);
    }

    @Override
    public void deleteMealFromFavorite(Meal meal) {
        mealRepository.deleteFavouriteMeal(meal);
    }
}

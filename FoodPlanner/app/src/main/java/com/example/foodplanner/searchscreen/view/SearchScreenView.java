package com.example.foodplanner.searchscreen.view;

import com.example.foodplanner.Models.area.Area;
import com.example.foodplanner.Models.category.Category;
import com.example.foodplanner.Models.ingredient.Ingredient;
import com.example.foodplanner.Models.meals.Meal;

import java.util.List;

public interface SearchScreenView {
    void showOnNoConnectionSearch();

    void clearSearchUI();

    void setNoConnectionSearchUI();

    //area
    void showListOfMealByArea(List<Meal> meals);

    void showAllAreas(List<Area> areas);

    //ingredient
    void showListOfMealByIngredient(List<Meal> meals);

    void showAllIngredients(List<Ingredient> ingredients);

    //category
    void showAllCategories(List<Category> categories);

    void showMealList(List<Meal> meals);


    void showListOfMealByCategorty(List<Meal> meals);

    void showMealDetailsSearch(Meal meal);
}

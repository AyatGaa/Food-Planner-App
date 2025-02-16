package com.example.foodplanner.searchscreen.view;

import com.example.foodplanner.Models.area.Area;
import com.example.foodplanner.Models.category.Category;
import com.example.foodplanner.Models.ingredient.Ingredient;
import com.example.foodplanner.Models.meals.Meal;

import java.util.List;

public interface SearchScreenView {

   void showListOfMealByCategorty(List<Meal> meals);
   void showListOfMealByArea(List<Meal> meals);
   void showListOfMealByIngredient(List<Meal> meals);

    void showAllAreas(List<Area> areas);

    void showAllCategories(List<Category> categories);

    void  showAllIngredients(List<Ingredient> ingredients);

    void showMealList(List<Meal> meals);
}

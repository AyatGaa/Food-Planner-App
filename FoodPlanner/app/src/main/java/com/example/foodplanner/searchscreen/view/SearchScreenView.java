package com.example.foodplanner.searchscreen.view;

import com.example.foodplanner.Models.category.Category;
import com.example.foodplanner.Models.meals.Meal;

import java.util.List;

public interface SearchScreenView {

   void showListOfMealByCategorty(List<Meal> meals);
   void showListOfMealByArea(List<Meal> meals);
   void showListOfMealByIngredient(List<Meal> meals);
}

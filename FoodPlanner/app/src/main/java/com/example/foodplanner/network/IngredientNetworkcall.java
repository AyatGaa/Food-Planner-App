package com.example.foodplanner.network;

import com.example.foodplanner.Models.ingredient.Ingredient;

import java.util.List;

public interface IngredientNetworkcall {

  void  onIngredientSuccess(List<Ingredient> ingredientList);

   void onIngredientFailure(String errorMsg);
}

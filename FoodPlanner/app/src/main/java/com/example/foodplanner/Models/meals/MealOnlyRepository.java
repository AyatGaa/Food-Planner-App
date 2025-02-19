package com.example.foodplanner.Models.meals;

import com.example.foodplanner.network.callbacks.NetworkCallback;
import com.example.foodplanner.network.callbacks.RandomMealCallback;

public interface MealOnlyRepository {
  void getAllMeals(NetworkCallback callBack);
  void getRandomMeal(RandomMealCallback callBack);
}

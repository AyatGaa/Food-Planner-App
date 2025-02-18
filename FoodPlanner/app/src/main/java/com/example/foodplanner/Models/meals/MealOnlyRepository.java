package com.example.foodplanner.Models.meals;

import com.example.foodplanner.network.NetworkCallback;
import com.example.foodplanner.network.RandomMealCallback;

public interface MealOnlyRepository {
  void getAllMeals(NetworkCallback callBack);
  void getRandomMeal(RandomMealCallback callBack);
}

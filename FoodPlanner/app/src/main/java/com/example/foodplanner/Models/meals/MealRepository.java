package com.example.foodplanner.Models.meals;

import com.example.foodplanner.network.NetworkCallback;
import com.example.foodplanner.network.RandomMealCallback;

import java.util.List;

public interface MealRepository {
  void getAllMeals(NetworkCallback callBack);
  void getRandomMeal(RandomMealCallback callBack);
}

package com.example.foodplanner.Models.meals;

import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

public interface MealRepository {
  void getAllMeals(NetworkCallback callBack);
}

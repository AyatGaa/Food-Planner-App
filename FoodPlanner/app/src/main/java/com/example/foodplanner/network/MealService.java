package com.example.foodplanner.network;

import com.example.foodplanner.Models.meals.Meals;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MealService {


    @GET("search.php")
    Call<Meals> getAllMeals(@Query("s") String letter);
}

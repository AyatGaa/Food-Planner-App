package com.example.foodplanner.network;

import com.example.foodplanner.Models.area.Areas;
import com.example.foodplanner.Models.category.Categories;
import com.example.foodplanner.Models.meals.Meals;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {
    @GET("search.php")
    Call<Meals> getAllMeals(@Query("s") String letter);

    @GET("categories.php") // 14 list of 14 category each one has its details
    Call<Categories> getAllCategories();

    @GET("list.php?a=list")
    Call<Areas> gatAllAreas();
}

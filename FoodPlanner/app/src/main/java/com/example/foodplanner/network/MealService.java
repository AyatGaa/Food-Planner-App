package com.example.foodplanner.network;

import com.example.foodplanner.Models.area.Areas;
import com.example.foodplanner.Models.category.Categories;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.Meals;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {
    @GET("search.php")
    Single<Meals> getAllMeals(@Query("s") String letter);

    @GET("categories.php") // 14 list of 14 category each one has its details
    Single<Categories> getAllCategories();

    @GET("list.php?a=list")
    Single<Areas> gatAllAreas();

    @GET("random.php")
    Single<Meals> getRandomMeal();
}

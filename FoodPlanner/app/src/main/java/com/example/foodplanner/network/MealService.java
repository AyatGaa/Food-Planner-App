package com.example.foodplanner.network;

import com.example.foodplanner.Models.area.Areas;
import com.example.foodplanner.Models.category.Categories;
import com.example.foodplanner.Models.ingredient.Ingredients;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.Meals;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MealService {


    @GET("filter.php")
    Single<Meals> filterMealByCategory(@Query("c") String category);
    @GET("filter.php")
    Single<Meals> filterMealByArea(@Query("a") String area);
    @GET("filter.php")
    Single<Meals> filterMealByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Observable<Meals> getMealsByCategory(@Query("c") String category);

    @GET("filter.php")
    Observable<Meals> getMealsByArea(@Query("a") String area);

    @GET("filter.php")
    Observable<Meals> getMealsByIngredient(@Query("i") String ingredient);

    @GET("lookup.php")
    Observable<Meal> getMealById(@Query("i") String mealId);


    @GET("list.php?a=list")
    Single<Areas> getAllAreas();
    @GET("categories.php") // 14 list of 14 category each one has its details
    Single<Categories> getAllCategories();


    @GET("list.php?i=list")
    Single<Ingredients> getAllIngredients();

    @GET("search.php")
    Single<Meals> getAllMeals(@Query("s") String mealName);


    @GET("random.php")
    Single<Meals> getRandomMeal();
}

package com.example.foodplanner.Repository.modelrepoitory;

import android.util.Log;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.Meals;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSource;
import com.example.foodplanner.network.AreaCallback;
import com.example.foodplanner.network.CategoryCallback;
import com.example.foodplanner.network.FilterRemoteDataSource;
import com.example.foodplanner.network.IngredientNetworkcall;
import com.example.foodplanner.network.MealRemoteDataSource;
import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealRepositoryImpl implements MealRepository {
    MealRemoteDataSource mealRemoteDataSource;
    FavouriteMealLocalDataSource favouriteMealLocalDataSource;
    FilterRemoteDataSource crds;
    private static MealRepositoryImpl mealRepository = null;

    public MealRepositoryImpl(MealRemoteDataSource mealRemoteDataSource, FavouriteMealLocalDataSource favouriteMealLocalDataSource, FilterRemoteDataSource filterRemoteDataSource) {
        this.mealRemoteDataSource = mealRemoteDataSource;
        this.favouriteMealLocalDataSource = favouriteMealLocalDataSource;
        this.crds = filterRemoteDataSource;
    }

    public static synchronized MealRepositoryImpl getInstance(MealRemoteDataSource mealRemoteDataSource, FavouriteMealLocalDataSource favouriteMealLocalDataSource, FilterRemoteDataSource filterRemoteDataSource) {
        if (mealRepository == null) {
            mealRepository = new MealRepositoryImpl(mealRemoteDataSource, favouriteMealLocalDataSource, filterRemoteDataSource);
        }
        return mealRepository;
    }


    @Override
    public Observable<List<Meal>> getAllFavouriteMeals(String userId) {
        return favouriteMealLocalDataSource.getAllFavouriteMeals(userId);
    }

    @Override
    public Observable<Meals> getMealsByCategory(NetworkCallback callBack, String category) {
        return crds.getMealsByCategory(callBack, category);

    }

    @Override
    public Observable<Meals> getMealsByArea(NetworkCallback callBack, String area) {
        return crds.getMealsByArea(callBack, area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(meals -> {
                    if (meals == null || meals.getMeals() == null || meals.getMeals().isEmpty()) {
                        Log.w("Chip", "No meals found for area: " + area);
                        callBack.onFailure("No meals found for area: " + area);
                        return new Meals();  // Return an empty Meals object to avoid null issues
                    } else {
                        Log.d("Chip", "Meals found: " + meals.getMeals().size());
                        callBack.onSuccessArea(meals.getMeals()); // Notify callback of success
                        return meals;
                    }
                })
                .doOnError(throwable -> {
                    Log.e("Chip", "Error fetching meals by area: " + throwable.getMessage());
                    callBack.onFailure(throwable.getMessage());
                });
    }



    @Override
    public Observable<Meals> getMealsByIngredient(NetworkCallback callBack, String ingredient) {
        return crds.getMealsByIngredient(callBack, ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void insertFavoriteMeal(Meal meal) {
        favouriteMealLocalDataSource.insertFavoriteMeal(meal);
    }

    @Override
    public void deleteFavouriteMeal(Meal meal) {
        favouriteMealLocalDataSource.deleteFavouriteMeal(meal);
    }

    @Override
    public void mealNetworkCall(NetworkCallback callBack) {
        mealRemoteDataSource.mealNetworkCall(callBack);
    }

    @Override
    public void filterByCategory(NetworkCallback callBack, String category) {
        crds.filterMealByCategory(callBack, category);
    }

    @Override
    public void filterByArea(NetworkCallback callBack, String area) {
        crds.filterMealByArea(callBack, area);
    }

    @Override
    public void filterByIngredient(NetworkCallback callBack, String ingredient) {
        crds.filterMealByIngredient(callBack, ingredient);
    }

    @Override
    public void getAllCategories(CategoryCallback callBack) {
        crds.categoryNetworkCall(callBack);
    }

    @Override
    public void getAllAreas(AreaCallback callBack) {
        Log.d("REPO", "getAllAreas: Calling RemoteDataSource222");
        crds.areaNetworkCall(callBack);

    }

    @Override
    public void getAllIngredients(IngredientNetworkcall callBack) {
        crds.ingredientNetworkCall(callBack);
    }

    @Override
    public Single<Meals> searchMealByName(NetworkCallback callBack, String mealName) {
        return mealRemoteDataSource.getAllMeals(callBack, mealName);
    }

}

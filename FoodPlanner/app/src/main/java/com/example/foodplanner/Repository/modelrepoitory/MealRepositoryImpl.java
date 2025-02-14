package com.example.foodplanner.Repository.modelrepoitory;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSource;
import com.example.foodplanner.network.MealRemoteDataSource;
import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class MealRepositoryImpl implements MealRepository{
    MealRemoteDataSource mealRemoteDataSource;
    FavouriteMealLocalDataSource  favouriteMealLocalDataSource;

    private static MealRepositoryImpl mealRepository = null;

    public MealRepositoryImpl(MealRemoteDataSource mealRemoteDataSource, FavouriteMealLocalDataSource favouriteMealLocalDataSource) {
        this.mealRemoteDataSource = mealRemoteDataSource;
        this.favouriteMealLocalDataSource = favouriteMealLocalDataSource;
    }

    public static synchronized MealRepositoryImpl getInstance(MealRemoteDataSource mealRemoteDataSource, FavouriteMealLocalDataSource favouriteMealLocalDataSource) {
        if (mealRepository == null) {
            mealRepository = new MealRepositoryImpl(mealRemoteDataSource, favouriteMealLocalDataSource);
        }
        return mealRepository;
    }


    @Override
    public Observable<List<Meal>> getAllFavouriteMeals() {
        return favouriteMealLocalDataSource.getAllFavouriteMeals();
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
}

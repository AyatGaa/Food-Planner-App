package com.example.foodplanner.Models.meals;

import android.content.Context;

import com.example.foodplanner.network.datasources.MealRemoteDataSource;
import com.example.foodplanner.network.callbacks.NetworkCallback;
import com.example.foodplanner.network.callbacks.RandomMealCallback;

public class MealOnlyRepositoryImpl implements MealOnlyRepository {

    MealRemoteDataSource remoteDataSource;

    private static MealOnlyRepositoryImpl mealRepository = null;

    public MealOnlyRepositoryImpl(MealRemoteDataSource remoteDataSource , Context context ) {
        this.remoteDataSource = remoteDataSource;
    }

    public static MealOnlyRepositoryImpl getInstance(MealRemoteDataSource remoteDataSource, Context context) {
        if (mealRepository == null) {
            mealRepository = new MealOnlyRepositoryImpl(remoteDataSource , context);

        }
        return mealRepository;
    }


    @Override
    public void getAllMeals(NetworkCallback callBack) {
        remoteDataSource.mealNetworkCall(callBack);
    }

    @Override
    public void getRandomMeal(RandomMealCallback callBack) {
        remoteDataSource.randomMealNetworkCall(callBack);
    }


}


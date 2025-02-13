package com.example.foodplanner.Models.meals;

import com.example.foodplanner.network.MealRemoteDataSource;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;
import com.example.foodplanner.network.NetworkCallback;

import java.util.Collections;
import java.util.List;

public class MealRepositoryImpl implements MealRepository {

    MealRemoteDataSource remoteDataSource;

    private static MealRepositoryImpl mealRepository = null;

    public MealRepositoryImpl(MealRemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static MealRepositoryImpl getInstance(MealRemoteDataSource remoteDataSource) {

        if (mealRepository == null) {
            mealRepository = new MealRepositoryImpl(remoteDataSource);

        }
        return mealRepository;
    }


    @Override
    public void getAllMeals(NetworkCallback callBack) {
        remoteDataSource.mealNetworkCall(callBack);
    }

    @Override
    public void getRandomMeal(NetworkCallback callBack) {
        remoteDataSource.randomMealNetworkCall(callBack);
    }


}


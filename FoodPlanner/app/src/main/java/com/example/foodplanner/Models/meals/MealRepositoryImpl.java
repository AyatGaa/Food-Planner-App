package com.example.foodplanner.Models.meals;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.foodplanner.network.MealRemoteDataSource;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;
import com.example.foodplanner.network.NetworkCallback;
import com.example.foodplanner.network.RandomMealCallback;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MealRepositoryImpl implements MealRepository {

    MealRemoteDataSource remoteDataSource;

    private static final String PREF_NAME = "RandomMealPrefs";
    private static final String KEY_MEAL = "RandomMeal";
    private static final String KEY_DATE = "MealDate";

    private final SharedPreferences sharedPreferences;

    private final Gson gson;

    private static MealRepositoryImpl mealRepository = null;

    public MealRepositoryImpl(MealRemoteDataSource remoteDataSource , Context context ) {
        this.remoteDataSource = remoteDataSource;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        this.gson = new Gson();
    }

    public static MealRepositoryImpl getInstance(MealRemoteDataSource remoteDataSource, Context context) {

        if (mealRepository == null) {
            mealRepository = new MealRepositoryImpl(remoteDataSource , context);

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


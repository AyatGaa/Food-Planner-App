package com.example.foodplanner.network;

import android.util.Log;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.Meals;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealRemoteDataSourceImpl implements MealRemoteDataSource {

    private final String MEAL_PATH = "https://www.themealdb.com/api/json/v1/1/";

    MealService mealService;
    private static MealRemoteDataSourceImpl mealRemoteDataSource = null;

    public MealRemoteDataSourceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MEAL_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mealService = retrofit.create(MealService.class);
    }

    public static synchronized MealRemoteDataSourceImpl getInstance() {
        if (mealRemoteDataSource == null) {
            mealRemoteDataSource = new MealRemoteDataSourceImpl();
        }
        return mealRemoteDataSource;
    }

    @Override
    public void mealNetworkCall(NetworkCallback callBack) {


        List<Meal> mealData = new ArrayList<>();
        Call<Meals> mealsCall = mealService.getAllMeals(" ");
        mealsCall.enqueue(new Callback<Meals>() {

            @Override
            public void onResponse(Call<Meals> call, Response<Meals> response) {
                if (response.isSuccessful()) {


                    mealData.addAll(response.body().getMeals());
                    if (mealData.size() > 0) {
                        callBack.onSuccess(mealData);
                        Log.i("TAG", "onResponse: " + mealData.size() + mealData.get(0));
                    }

                }

            }

            @Override
            public void onFailure(Call<Meals> call, Throwable t) {
                callBack.onFailure(t.getMessage());
                t.printStackTrace();
                Log.i("TAG", "onFailure: somthin wrongg  ");
            }
        });
    }
}

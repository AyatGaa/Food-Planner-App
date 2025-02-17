package com.example.foodplanner.network;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.Meals;
import com.google.gson.Gson;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
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
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
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
        Single<Meals> mealsSingle = mealService.getAllMeals(" ");
        mealsSingle.subscribeOn(Schedulers.io())
                .map(meals -> meals.getMeals())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealList -> {
                            callBack.onSuccess(mealList);
                            Log.i("TAG", "mealNetworkCall: witth rx done" + mealList.size());
                        },
                        error -> {
                            callBack.onFailure(error.getMessage());
                            Log.i("TAG", "mealNetworkCall: witth rx fail" + error.getMessage());
                        });
    }

    @Override
    public void randomMealNetworkCall(RandomMealCallback callBack) {
        Single<Meals> mealsSingle = mealService.getRandomMeal();
        mealsSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealList -> {
                            callBack.onRandomMealSuccess(mealList.getMeals().get(0));
                            Log.i("TAG", "randomMealNetworkCall:  done" );
                        },
                        error -> {
                            callBack.onRandomMealFailure(error.getMessage());
                            Log.i("TAG", "mealNetwrandomMealNetworkCallorkCall:  fail" + error.getMessage());
                        });
    }

    @Override
    public void filterMealByArea(NetworkCallback callBack, String area) {
            mealService.filterMealByArea(area).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(a ->a.getMeals())
                    .subscribe(

                            mealList -> {
                                Log.d("REPO", "filterMealByArea: hhhh" + mealList.size());
                                callBack.onSuccess(mealList);
                            },
                            error -> {
                                Log.d("REPO", "filterMealByArea: ");
                            }
                    );
    }

    @Override
    public Single<Meals> getAllMeals(NetworkCallback callBack, String mealName) {
        return mealService.getAllMeals(mealName);
//        .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());

    }




    @Override
    public void filterMealByIngredient(NetworkCallback callBack, String ingredient) {

    }

    @Override
    public void searchMealByName(NetworkCallback callBack, String mealName) {

    }

    @Override
    public void getAllAreas(NetworkCallback callBack) {

    }
}

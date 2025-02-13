package com.example.foodplanner.network;

import android.util.Log;

import com.example.foodplanner.Models.category.Categories;
import com.example.foodplanner.Models.category.Category;
import com.google.android.material.imageview.ShapeableImageView;

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

public class CategoryRemoteDataSourceImpl implements CategoryRemoteDataSource {

    private final String MEAL_PATH = "https://www.themealdb.com/api/json/v1/1/";

    MealService categoryService;
    private static CategoryRemoteDataSourceImpl categoryRemoteDataSource = null;


    public CategoryRemoteDataSourceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MEAL_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        categoryService = retrofit.create(MealService.class);
    }

    public static synchronized CategoryRemoteDataSourceImpl getInstance() {
        if (categoryRemoteDataSource == null) {
            categoryRemoteDataSource = new CategoryRemoteDataSourceImpl();
        }
        return categoryRemoteDataSource;
    }

    @Override
    public void categoryNetworkCall(NetworkCallback callBack) {
        Single<Categories> categoriesSingle = categoryService.getAllCategories();
        categoriesSingle
                .subscribeOn(Schedulers.io())
                .map(categories -> categories.getCategories())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        catList -> {
                            callBack.onCategorySuccess(catList);
                            Log.d("TAG", "categoryNetworkCall: wit retroofit" + catList.size());
                        },
                        error -> {
                            callBack.onCategoryFailure(error.getMessage());
                            Log.d("TAG", "categoryNetworkCall: in retro fail" + error.getMessage());
                        }
                );

    }
}

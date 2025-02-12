package com.example.foodplanner.network;

import android.util.Log;

import com.example.foodplanner.Models.category.Categories;
import com.example.foodplanner.Models.category.Category;

import java.util.ArrayList;
import java.util.List;

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
        List<Category> categoryList = new ArrayList<>();

        Call<Categories> categoryCall = categoryService.getAllCategories();
        categoryCall.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                categoryList.addAll(response.body().getCategories());
                callBack.onCategorySuccess(categoryList);
                Log.i("TAG", "onResponse: Caegory call Done " + categoryList.size());
                Log.i("TAG", "onResponse: Caegory call Done " + categoryList.get(0).getStrCategory());
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                callBack.onCategoryFailure(t.getMessage());
                Log.i("TAG", "onFailure: category call fail" + t.getMessage());
            }
        });


    }
}

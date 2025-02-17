package com.example.foodplanner.network;

import android.util.Log;

import com.example.foodplanner.Models.category.Categories;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.Meals;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilterRemoteDataSourceImpl implements FilterRemoteDataSource {

    private final String MEAL_PATH = "https://www.themealdb.com/api/json/v1/1/";

    MealService categoryService;
    private static FilterRemoteDataSourceImpl categoryRemoteDataSource = null;


    public FilterRemoteDataSourceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MEAL_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        categoryService = retrofit.create(MealService.class);
    }

    public static synchronized FilterRemoteDataSourceImpl getInstance() {
        if (categoryRemoteDataSource == null) {
            categoryRemoteDataSource = new FilterRemoteDataSourceImpl();
        }
        return categoryRemoteDataSource;
    }

    @Override
    public void categoryNetworkCall(CategoryCallback callBack) {
        Single<Categories> categoriesSingle = categoryService.getAllCategories();
        categoriesSingle
                .subscribeOn(Schedulers.io())
                .map(categories -> categories.getCategories())
                .observeOn(AndroidSchedulers.mainThread())
                .map(catList -> catList)
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
    @Override
    public void areaNetworkCall(AreaCallback callBack) {
        Log.d("REPO", "getAllAreas: Calling RemoteDataSource");
        categoryService.getAllAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(a -> a.getAreas())
                .map(catList -> catList)
                .subscribe(
                        areaList -> {
                            callBack.onAreaSuccess(areaList);
                            Log.d("REPO", "onAreaSuccess: " + areaList.size());

                        },
                        error -> {
                            callBack.onAreaFailure(error.getMessage());
                            Log.e("REPO", "Error: " + error.getMessage());
                        }
                );
    }

    @Override
    public void ingredientNetworkCall(IngredientNetworkcall callBack) {
        categoryService.getAllIngredients()
                .subscribeOn(Schedulers.io())
                .map(ingredients -> ingredients.getIngredients())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ingredientList -> {
                            callBack.onIngredientSuccess(ingredientList);
                            Log.d("TAG", "ingredientNetworkCall: Retrieved " + ingredientList.size() + " ingredients");
                        },
                        error -> {
                            callBack.onIngredientFailure(error.getMessage());
                            Log.d("TAG", "ingredientNetworkCall: Failed - " + error.getMessage());
                        }
                );
    }


    @Override
    public void filterMealByCategory(NetworkCallback callBack, String category) {
        categoryService.filterMealByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(categories -> categories.getMeals())
                .subscribe(
                        mealList -> {
                            callBack.onSuccess(mealList);
                            Log.d("cat", "filterMealByCategory: " + mealList.size());
                        },
                        error -> {
                            callBack.onFailure(error.getMessage());
                            Log.i("cat", "filterMealByCategory: " + error.getMessage());
                        }

                );
    }

    @Override
    public void filterMealByArea(NetworkCallback callBack, String area) {
        categoryService.filterMealByArea(area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(areas -> areas.getMeals())
                .subscribe(
                        mealList -> {
                            callBack.onSuccess(mealList);
                            Log.d("cat", "filterMealByArea: " + mealList.size());
                        },
                        error -> {
                            callBack.onFailure(error.getMessage());
                            Log.i("cat", "filterMealByArea: " + error.getMessage());
                        }
                );
    }

    @Override
    public void filterMealByIngredient(NetworkCallback callBack, String ingredient) {
        categoryService.filterMealByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ingredients -> ingredients.getMeals())
                .subscribe(
                        mealList -> {

                            callBack.onSuccess(mealList);
                            Log.d("cat", "filterMealByIngredient: " + mealList.size());
                        },
                        error -> {
                            callBack.onFailure(error.getMessage());
                            Log.i("cat", "filterMealByIngredient: " + error.getMessage());
                        }

                );
    }

    @Override
    public Observable<Meals> getMealsByCategory(NetworkCallback callBack, String category) {
        return categoryService.getMealsByCategory(category);

    }

    public Observable<Meals> getMealsByArea(NetworkCallback callBack, String area) {
        return categoryService.getMealsByArea(area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(meals -> Log.d("api", "Meals: " + meals))
                .doOnError(throwable -> Log.e("api", "Error: " + throwable.getMessage()));
    }


    @Override
    public Observable<Meals> getMealsByIngredient(NetworkCallback callBack, String ingredient) {
        return categoryService.getMealsByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public Observable<Meals> getMealById(String mealId) {
        return categoryService.getMealById(mealId);
    }
}

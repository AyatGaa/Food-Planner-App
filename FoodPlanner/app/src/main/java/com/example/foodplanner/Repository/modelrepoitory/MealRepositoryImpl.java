package com.example.foodplanner.Repository.modelrepoitory;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.Meals;
import com.example.foodplanner.backup.BackupMealFirebase;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSource;
import com.example.foodplanner.network.AreaCallback;
import com.example.foodplanner.network.CategoryCallback;
import com.example.foodplanner.network.FilterRemoteDataSource;
import com.example.foodplanner.network.IngredientNetworkcall;
import com.example.foodplanner.network.MealDetailCallback;
import com.example.foodplanner.network.MealRemoteDataSource;
import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealRepositoryImpl implements MealRepository {
    MealRemoteDataSource mealRemoteDataSource;
    FavouriteMealLocalDataSource favouriteMealLocalDataSource;
    FilterRemoteDataSource crds;
    BackupMealFirebase favFireBase;
    private static MealRepositoryImpl mealRepository = null;

    public MealRepositoryImpl(MealRemoteDataSource mealRemoteDataSource,
                              FavouriteMealLocalDataSource favouriteMealLocalDataSource,
                              FilterRemoteDataSource filterRemoteDataSource,
                              BackupMealFirebase backupMealFirebase) {
        this.mealRemoteDataSource = mealRemoteDataSource;
        this.favouriteMealLocalDataSource = favouriteMealLocalDataSource;
        this.crds = filterRemoteDataSource;
        this.favFireBase = backupMealFirebase;
    }

    public static synchronized MealRepositoryImpl getInstance(MealRemoteDataSource mealRemoteDataSource,
                                                              FavouriteMealLocalDataSource favouriteMealLocalDataSource,
                                                              FilterRemoteDataSource filterRemoteDataSource
            , BackupMealFirebase backupMealFirebase) {
        if (mealRepository == null) {
            mealRepository = new MealRepositoryImpl(mealRemoteDataSource, favouriteMealLocalDataSource, filterRemoteDataSource, backupMealFirebase);
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


    @SuppressLint("CheckResult")
    @Override
    public Observable<Meals> getMealsByIngredient(NetworkCallback callBack, String ingredient) {

        return crds.getMealsByIngredient(callBack, ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public void insertFavoriteMeal(Meal meal) {
        favouriteMealLocalDataSource.insertFavoriteMeal(meal);
        //firbase also
        favFireBase.addMealToFirebase(meal, meal.getUserId());
    }

    @Override
    public void deleteFavouriteMeal(Meal meal) {
        favouriteMealLocalDataSource.deleteFavouriteMeal(meal);
        favFireBase.deleteMealFromFirebase(meal, meal.getUserId());
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

    @Override
    public void addMealToFirebase(Meal meal, String userId) {
        favFireBase.addMealToFirebase(meal, userId);
    }

    @Override
    public Observable<List<Meal>> getFavouriteMealsFromFirebase(String userId) {
        return favFireBase.getFavouriteMealsFromFirebase(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void deleteMealFromFirebase(Meal meal, String userId) {
        favFireBase.deleteMealFromFirebase(meal, userId);
    }

    public void fetchMealDetails(String mealId, MealDetailCallback callback) {

        crds.getMealById(mealId)
                .subscribeOn(Schedulers.io())   // Perform network request on a background thread
                .observeOn(AndroidSchedulers.mainThread())  // Switch to the main thread to update the UI
                .subscribe(new Observer<Meals>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // You can show a loading indicator here if needed
                    }

                    @Override
                    public void onNext(@NonNull Meals meals) {
                        if (meals != null && meals.getMeals() != null && !meals.getMeals().isEmpty()) {
                            Meal meal = meals.getMeals().get(0);
                            callback.onMealDetailsFetched(meal);
                        } else {
                            callback.onFailure("Meal not found");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callback.onFailure("Error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // You can hide the loading indicator here if needed
                    }
                });
    }
}


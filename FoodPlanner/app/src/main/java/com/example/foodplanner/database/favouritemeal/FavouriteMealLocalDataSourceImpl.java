package com.example.foodplanner.database.favouritemeal;

import android.content.Context;
import android.util.Log;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.database.AppDataBase;

import java.util.Collection;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavouriteMealLocalDataSourceImpl implements FavouriteMealLocalDataSource{

   private MealDAO mealDAO;

   Context context;
   private Observable<List<Meal>> savedMeals;

   private static FavouriteMealLocalDataSourceImpl instance;


   private FavouriteMealLocalDataSourceImpl(Context context) {
       this.context = context;
       AppDataBase db = AppDataBase.getInstance(context);
       mealDAO = db.getMealDAO();
      // savedMeals = mealDAO.getAllFavouriteMeals(); // take it once !
   }

   public static FavouriteMealLocalDataSourceImpl getInstance(Context context) {
       if (instance == null) {
           instance = new FavouriteMealLocalDataSourceImpl(context);
       }
       return instance;
   }

    @Override
    public Observable<List<Meal>> getAllFavouriteMeals(String userId) {
        return mealDAO.getAllFavouriteMeals(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()); // observe here to be uptodate
    }


    @Override
    public void insertFavoriteMeal(Meal meal) {

        Log.d("fav", "Inserting meal for user: " + meal.getUserId());
        mealDAO.insertFavoriteMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ()-> Log.i("data", "insertFavoriteMeal: inserted from local da")
                        ,error -> Log.i("data", "insertFavoriteMeal: error"+ error.getMessage())
                );
    }

    @Override
    public void deleteFavouriteMeal(Meal meal) {
        mealDAO.deleteFavouriteMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(

                        ()-> Log.i("data", "deleteFavouriteMeal: deleted from local da")
                        ,error -> Log.i("data", "deleteFavouriteMeal: error")
                );
    }
}

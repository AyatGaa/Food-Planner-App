package com.example.foodplanner.database.plannedmeal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.example.foodplanner.database.AppDataBase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlannedMealLocalDataSourceImpl implements PlannedMealLocalDataSource {

    PlanMealDao planMealDao;
    Context context;
    Observable<List<PlannedMeal>> savedPlannedMeals;
    Observable<List<PlannedMeal>> savedMeals;

    private static PlannedMealLocalDataSourceImpl instance;

    public PlannedMealLocalDataSourceImpl(Context context) {
        this.context = context;
        AppDataBase db = AppDataBase.getInstance(context);
        planMealDao = db.getPlannedMealDAO();
        //  savedMeals = planMealDao.getAllPlannedMeals();
        //savedPlannedMeals = planMealDao.getMealsForDate("2025-02-14");
    }

    public static PlannedMealLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new PlannedMealLocalDataSourceImpl(context);
        }
        return instance;

    }

    @Override
    public Observable<List<PlannedMeal>> getMealsForDate(String selectedDate) {
        return savedPlannedMeals = planMealDao.getMealsForDate(selectedDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()); // observe here to be uptodate;;
    }

    @Override
    public Observable<List<PlannedMeal>> getPlannedMealsByDate(String userId, String date) {
        return savedPlannedMeals = planMealDao.getPlannedMealsByDate(userId, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()); // observe here to be uptodate;
    }


    @SuppressLint("CheckResult")
    @Override
    public void insertPlannedMeal(PlannedMeal plannedMeal) {// is already exist or not
        planMealDao.isMealPlanned(plannedMeal.getIdMeal(), plannedMeal.getDate())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealsNum -> {
                            if (mealsNum == 0) {
                                planMealDao.insertPlannedMeal(plannedMeal)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                () -> {
                                                    Toast.makeText(context, "Meal Added Successfully!", Toast.LENGTH_SHORT).show();
                                                },
                                                error -> Log.i("data", "insertFavoriteMeal: error" + error.getMessage())
                                        );
                            }
                        }, error -> {
                            Log.e("PlanRepository", "Error checking meal existence", error);
                        });


    }

    @Override
    public Observable<List<PlannedMeal>> getAllPlannedMeals() {
        return savedMeals = planMealDao.getAllPlannedMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void deletePlannedMeal(PlannedMeal plannedMeal) {
        planMealDao.deletePlannedMeal(plannedMeal).subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.i("data", "deletePlannedMeal: deleted from local da")
                        , error -> Log.i("data", "deletePlannedMeal: error")
                );
    }

    @Override
    public void deletePastMeals(String currentDate) {
        planMealDao.deletePastMeals(currentDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<PlannedMeal> getPlannedMealById(String mealId) {
        return planMealDao.getPlannedMealById(mealId);
    }


}

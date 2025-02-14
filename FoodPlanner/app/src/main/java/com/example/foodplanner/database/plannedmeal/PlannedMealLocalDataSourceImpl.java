package com.example.foodplanner.database.plannedmeal;

import android.content.Context;
import android.util.Log;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.example.foodplanner.database.AppDataBase;

import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlannedMealLocalDataSourceImpl implements PlannedMealLocalDataSource {

    PlanMealDao planMealDao;
    Context context;
    private Observable<List<PlannedMeal>> savedPlannedMeals;
    private Observable<List<Meal>> savedMeals;

    private static PlannedMealLocalDataSourceImpl instance;

    public PlannedMealLocalDataSourceImpl(Context context) {
        this.context =context;
        AppDataBase db = AppDataBase.getInstance(context);
        planMealDao = db.getPlannedMealDAO();
        savedMeals = planMealDao.getAllPlannedMeals();
        savedPlannedMeals = planMealDao.getMealsForDate("2025-02-14");
    }

    public static PlannedMealLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new PlannedMealLocalDataSourceImpl(context);
        }
        return instance;

    }

    @Override
    public Observable<List<PlannedMeal>> getMealsForDate(String selectedDate) {
        return savedPlannedMeals;
    }

    @Override
    public Observable<List<Meal>> getAllPlannedMeals() {
        return savedMeals;
    }



    @Override
    public void insertPlannedMeal(PlannedMeal plannedMeal) {
        planMealDao.insertPlannedMeal(plannedMeal).subscribeOn(Schedulers.io())
                .subscribe(
                        ()-> Log.i("data", "insertPlannedMeal: inserted from local da")
                        ,error -> Log.i("data", "insertPlannedMeal: error")
                );
    }

    @Override
    public void deletePlannedMeal(PlannedMeal plannedMeal) {
            planMealDao.deletePlannedMeal(plannedMeal).subscribeOn(Schedulers.io())
                    .subscribe(
                            ()-> Log.i("data", "deletePlannedMeal: deleted from local da")
                            ,error -> Log.i("data", "deletePlannedMeal: error")
                    );
    }

    @Override
    public void deletePastMeals(String currentDate) {

    }
}

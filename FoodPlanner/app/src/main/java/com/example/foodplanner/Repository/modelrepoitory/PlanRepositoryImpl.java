package com.example.foodplanner.Repository.modelrepoitory;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.example.foodplanner.backup.favouritmeals.FavoriteMealFirebase;
import com.example.foodplanner.database.plannedmeal.PlannedMealLocalDataSource;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlanRepositoryImpl implements PlanRepository {

    PlannedMealLocalDataSource plannedMealLocalDataSource;

    private static PlanRepositoryImpl planRepository = null;
    FavoriteMealFirebase planFireBase;

    public PlanRepositoryImpl(PlannedMealLocalDataSource plannedMealLocalDataSource,
                              FavoriteMealFirebase planMealFirebase) {
        this.planFireBase = planMealFirebase;
        this.plannedMealLocalDataSource = plannedMealLocalDataSource;
    }

    public static synchronized PlanRepositoryImpl getInstance(PlannedMealLocalDataSource plannedMealLocalDataSource, FavoriteMealFirebase planMealFirebase) {
        if (planRepository == null) {
            planRepository = new PlanRepositoryImpl(plannedMealLocalDataSource, planMealFirebase);
        }
        return planRepository;
    }


    @Override
    public Observable<PlannedMeal> getPlannedMealById(String mealId) {
        return  plannedMealLocalDataSource.getPlannedMealById(mealId)
                .subscribeOn(Schedulers.io())  // Run in background thread
                .observeOn(AndroidSchedulers.mainThread()); // Observe on UI thread
    }


    @Override
    public void addPlannedMealToFirebase(PlannedMeal plannedMeal, String userId, String plannedDate) {
        planFireBase.addPlannedMealToFirebase(plannedMeal, userId, plannedDate);
    }

    @Override
    public Observable<List<PlannedMeal>> getPlannedMealsFromFirebase(String userId, String plannedDate) {
        return planFireBase.getPlannedMealsFromFirebase(userId, plannedDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void deletePlannedMealToFirebase(PlannedMeal plannedMeal, String userId, String plannedDate) {
        planFireBase.deletePlannedMealToFirebase(plannedMeal, userId, plannedDate);
    }

    @Override
    public Observable<List<PlannedMeal>> getPlannedMealsByDate(String userId, String date) {
        return plannedMealLocalDataSource.getPlannedMealsByDate(userId, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void insertPlannedMeal(PlannedMeal plannedMeal) {
        plannedMealLocalDataSource.insertPlannedMeal(plannedMeal);
        //fb
        planFireBase.addPlannedMealToFirebase(plannedMeal, plannedMeal.getUserId(), plannedMeal.getDate());
    }

    @Override
    public Observable<List<PlannedMeal>> getAllPlannedMeals() {
        return plannedMealLocalDataSource.getAllPlannedMeals();
    }

    @Override
    public void deletePlannedMeal(PlannedMeal plannedMeal) {
        plannedMealLocalDataSource.deletePlannedMeal(plannedMeal);
        //fb
        planFireBase.deletePlannedMealToFirebase(plannedMeal, plannedMeal.getUserId(), plannedMeal.getDate());
    }

    //NOT USED
    @Override
    public Observable<List<PlannedMeal>> getMealsForDate(String selectedDate) {
        return plannedMealLocalDataSource.getMealsForDate(selectedDate);
    }

    @Override
    public void deletePastMeals(String currentDate) {
        plannedMealLocalDataSource.deletePastMeals(currentDate);
    }
}

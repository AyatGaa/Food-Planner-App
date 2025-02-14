package com.example.foodplanner.Repository.modelrepoitory;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.example.foodplanner.database.plannedmeal.PlannedMealLocalDataSource;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class PlanRepositoryImpl implements PlanRepository {

    PlannedMealLocalDataSource plannedMealLocalDataSource;

    private static PlanRepositoryImpl planRepository = null;

    public PlanRepositoryImpl(PlannedMealLocalDataSource plannedMealLocalDataSource) {
        this.plannedMealLocalDataSource = plannedMealLocalDataSource;
    }

    public static synchronized PlanRepositoryImpl getInstance(PlannedMealLocalDataSource plannedMealLocalDataSource) {
        if (planRepository == null) {
            planRepository = new PlanRepositoryImpl(plannedMealLocalDataSource);
        }
        return planRepository;
    }

    @Override
    public Observable<List<PlannedMeal>> getMealsForDate(String selectedDate) {
        return plannedMealLocalDataSource.getMealsForDate(selectedDate);
    }

    @Override
    public Observable<List<Meal>> getAllPlannedMeals() {
        return plannedMealLocalDataSource.getAllPlannedMeals();
    }

    @Override
    public void insertPlannedMeal(PlannedMeal plannedMeal) {
        plannedMealLocalDataSource.insertPlannedMeal(plannedMeal);
    }

    @Override
    public void deletePlannedMeal(PlannedMeal plannedMeal) {
        plannedMealLocalDataSource.deletePlannedMeal(plannedMeal);
    }

    @Override
    public void deletePastMeals(String currentDate) {
        plannedMealLocalDataSource.deletePastMeals(currentDate);
    }
}

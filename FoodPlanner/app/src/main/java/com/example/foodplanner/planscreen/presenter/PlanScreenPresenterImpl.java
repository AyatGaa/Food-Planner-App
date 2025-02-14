package com.example.foodplanner.planscreen.presenter;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.Repository.modelrepoitory.PlanRepository;
import com.example.foodplanner.planscreen.view.PlanScreenView;

public class PlanScreenPresenterImpl implements PlanScreenPresenter{

    PlanScreenView planScreenView;
    PlanRepository planRepository;


    public PlanScreenPresenterImpl(PlanScreenView planScreenView, PlanRepository planRepository) {
        this.planRepository = planRepository;
        this.planScreenView = planScreenView;

    }
    @Override
    public void getMealsForDate(String selectedDate) {
        planRepository.getMealsForDate(selectedDate);
    }

    @Override
    public void getAllPlannedMeals() {
        planRepository.getAllPlannedMeals();
    }

    @Override
    public void addMealToPlan(PlannedMeal meal) {
            planRepository.insertPlannedMeal(meal);
    }

    @Override
    public void deleteMealFromPlan(PlannedMeal meal) {
            planRepository.deletePlannedMeal(meal);
    }
}

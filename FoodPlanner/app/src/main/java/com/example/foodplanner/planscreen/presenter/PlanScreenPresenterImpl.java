package com.example.foodplanner.planscreen.presenter;

import android.util.Log;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.example.foodplanner.Repository.modelrepoitory.PlanRepository;
import com.example.foodplanner.planscreen.view.PlanScreenView;
import com.example.foodplanner.utils.AppFunctions;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlanScreenPresenterImpl implements PlanScreenPresenter {

    PlanScreenView planScreenView;
    PlanRepository planRepository;


    public PlanScreenPresenterImpl(PlanScreenView planScreenView, PlanRepository planRepository) {
        this.planRepository = planRepository;
        this.planScreenView = planScreenView;

    }

    @Override
    public void getMealsForDate(String selectedDate) {
        String userId = AppFunctions.getCurrentUserId();
        planRepository.getPlannedMealsByDate(userId, selectedDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        plannedMeals -> {
                            planScreenView.showPlannedMeals(plannedMeals);
                            Log.i("data", "Meals fetched for date: " + selectedDate);
                        },
                        error -> {
                            Log.e("data", "Error fetching meals for date: " + selectedDate, error);
                        } );
    }
    @Override
    public void deleteMealFromPlan(PlannedMeal meal) {
        planRepository.deletePlannedMeal(meal);
        planScreenView.showSnackBar(meal);
    }

    @Override
    public void getPlannedMealsFirebase(String plannedDate) {
        String userId = AppFunctions.getCurrentUserId();
        Log.d("fb", "Fetching meals for user: " + userId); // Log userId for debugging

        planRepository.getPlannedMealsFromFirebase(userId, plannedDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {

                            for (PlannedMeal meal : meals) {

                                planRepository.insertPlannedMeal(meal);
                            }
                        },
                        error -> {
                            Log.e("fb", "Error fetching PLanned meals: " + error.getMessage());

                        }
                );
    }


    //NOT USED
    @Override
    public void getAllPlannedMeals() {

       // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userId = AppFunctions.getCurrentUserId();
        if(userId != null) {
            Log.d("fav", "Fetching meals for user: " + userId); // Debug log

            planRepository.getAllPlannedMeals().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            meals -> {
                                for (PlannedMeal meal : meals) {
                                    Log.d("fav", "Meal: " + meal.getMealName() + ", ID: " + meal.getIdMeal() + ", UserID: " + meal.getUserId());
                                }
                              planScreenView.showPlannedMeals(meals);
                            },

                            error -> Log.d("data", "Error getting favorite meals", error) // Error handling
                    );
        }else {

            Log.d("data", "User not signed in, can't fetch favorite meals.");
        }


    }

    @Override
    public void addMealToPlan(PlannedMeal meal) {
         planRepository.insertPlannedMeal(meal);
    }

    @Override
    public void addMealToPlanV2(Meal meal, String date) {
//        String userID = AppFunctions.getCurrentUserId();
//            // PlannedMeal=> String userId, String idMeal, String mealName, String mealImage, String date
//            PlannedMeal plannedMeal = new PlannedMeal(userID, meal.getIdMeal(), meal.getStrMeal(), meal.getStrMealThumb(), date);
//               planRepository.insertPlannedMeal(plannedMeal);

    }

}

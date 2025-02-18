package com.example.foodplanner.backup.planmeals;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class PlannedMealFirebaseImpl implements PlannedMealFirebase{

    FirebaseDatabase database;
    DatabaseReference dbRef;
    private static PlannedMealFirebaseImpl instance = null;

    public PlannedMealFirebaseImpl() {

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("planned_meal");
    }

    public static PlannedMealFirebaseImpl getInstance() {
        if (instance == null) {
            instance = new PlannedMealFirebaseImpl();

        }
        return instance;
    }




}

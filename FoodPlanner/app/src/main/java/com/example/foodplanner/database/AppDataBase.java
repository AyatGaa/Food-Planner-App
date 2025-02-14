package com.example.foodplanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.example.foodplanner.database.favouritemeal.MealDAO;
import com.example.foodplanner.database.plannedmeal.PlanMealDao;

@Database(entities = {Meal.class,PlannedMeal.class}, version = 2)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase appDataBase = null;

    public abstract MealDAO getMealDAO();
    public abstract PlanMealDao getPlannedMealDAO();

    public static synchronized AppDataBase getInstance(Context context){
        if(appDataBase == null){
            appDataBase = Room.databaseBuilder(context, AppDataBase.class, "meals.db").build();
        }
        return appDataBase;
    }


}

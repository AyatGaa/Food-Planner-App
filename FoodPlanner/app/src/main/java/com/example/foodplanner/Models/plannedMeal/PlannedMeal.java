package com.example.foodplanner.Models.plannedMeal;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;




@Entity(tableName = "planned_meals")
public class PlannedMeal {
    @PrimaryKey
    @NonNull
    private String idMeal;
    private String mealName;
    private String mealImage;
    private String date; // Scheduled date in format YYYY-MM-DD
    private String time;

    public PlannedMeal() {
    }

    public PlannedMeal(String idMeal, String mealName, String mealImage, String date, String time) {
        this.idMeal = idMeal;
        this.mealName = mealName;
        this.mealImage = mealImage;
        this.date = date;
        this.time = time;
    }

    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealImage() {
        return mealImage;
    }

    public void setMealImage(String mealImage) {
        this.mealImage = mealImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

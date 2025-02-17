package com.example.foodplanner.Models.plannedMeal;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;




@Entity(tableName = "planned_meals")
public class PlannedMeal {
    @PrimaryKey(autoGenerate = true)
    int id;

    @NonNull
    private String userId;
    private String idMeal;
    private String mealName;
    private String mealImage;
    private String date; // YYYY-MM-DD
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public PlannedMeal(@NonNull String userId, String idMeal, String mealName, String mealImage, String date) {
        this.userId = userId;
        this.idMeal = idMeal;
        this.mealName = mealName;
        this.mealImage = mealImage;
        this.date = date;
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

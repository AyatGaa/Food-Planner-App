package com.example.foodplanner.Models.area;

import com.google.gson.annotations.SerializedName;

public class Area {
    @SerializedName("strArea")
    public String strArea;



    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }
}

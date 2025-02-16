package com.example.foodplanner.Models.area;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Areas {
    @SerializedName("meals")
    private List<Area> areas;

    public List<Area> getAreas() {
        return areas;
    }
}

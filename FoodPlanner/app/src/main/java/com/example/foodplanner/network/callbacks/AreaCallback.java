package com.example.foodplanner.network.callbacks;

import com.example.foodplanner.Models.area.Area;

import java.util.List;

public interface AreaCallback {

    void onAreaSuccess(List<Area> areas);
    void onAreaFailure(String errorMessage);
}

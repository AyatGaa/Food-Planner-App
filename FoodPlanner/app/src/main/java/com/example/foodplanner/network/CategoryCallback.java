package com.example.foodplanner.network;

import com.example.foodplanner.Models.category.Category;

import java.util.List;

public interface CategoryCallback {

    void onCategorySuccess(List<Category> categories);
    void onCategoryFailure(String errorMessage);
}

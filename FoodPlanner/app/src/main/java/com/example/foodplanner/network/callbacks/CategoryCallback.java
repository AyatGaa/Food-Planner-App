package com.example.foodplanner.network.callbacks;

import com.example.foodplanner.Models.category.Category;

import java.util.List;

public interface CategoryCallback {

    void onCategorySuccess(List<Category> categories);
    void onCategoryFailure(String errorMessage);
}

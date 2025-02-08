package com.example.foodplanner.utils;

import android.view.View;

import androidx.navigation.Navigation;

public abstract class AppFunctions {

    public static void navigateTo(View view, int layoutId){
        Navigation.findNavController(view).navigate(layoutId);
    }

}

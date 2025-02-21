package com.example.foodplanner;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.network.callbacks.NetworkCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class MainActivity extends AppCompatActivity implements NetworkCallback {
    BottomNavigationView bottomNavigationView;
    NavController navController;


    void hideShowNavBar() {

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mainNavHostFragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Hide bottom navigation bar on splash Screen
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.splashScreenFragment || destination.getId() == R.id.signInFragment
                        || destination.getId() == R.id.introScreenFragment || destination.getId() == R.id.signUpFragment) {
                    bottomNavigationView.setVisibility(View.GONE);

                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);

                }
            });
        } else {
            Log.e("MainActivity", "NavHostFragment is NULL");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideShowNavBar();

    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onSuccess(List<Meal> meals) {
        Log.i("TAG", "onSuccess: getMeals in main " + meals.size());

    }



    @Override
    public void onSuccessArea(List<Meal> meals) {
        Log.i("Chip", "onSuccess: get Meals in main " + meals.size());

    }

    @Override
    public void onFailure(String errorMessage) {
        Log.i("TAG", "onFailure: something wrong main");
    }


}
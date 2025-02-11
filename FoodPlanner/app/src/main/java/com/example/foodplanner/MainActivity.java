package com.example.foodplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.foodplanner.Models.category.Category;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.network.CategoryRemoteDataSource;
import com.example.foodplanner.network.CategoryRemoteDataSourceImpl;
import com.example.foodplanner.network.MealRemoteDataSource;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;
import com.example.foodplanner.network.NetworkCallback;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class MainActivity extends AppCompatActivity implements NetworkCallback {
    MealRemoteDataSource rds;
    CategoryRemoteDataSource crds;
    BottomNavigationView bottomNavigationView;
    NavController navController;
    BottomAppBar bottomAppBar;

    void hideShowNavBar(){
        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mainNavHostFragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Hide bottom navigation on Splash Screen
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.splashScreenFragment || destination.getId() == R.id.signInFragment) {
                    bottomNavigationView.setVisibility(View.GONE);
                    bottomAppBar.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    bottomAppBar.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Log.e("MainActivity", "NavHostFragment is NULL");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("TAG", "onCreate: 2bel el set");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideShowNavBar();
        Log.d("TAG", "onCreate: b3d set");
//        rds = MealRemoteDataSourceImpl.getInstance();
//        rds.mealNetworkCall(MainActivity.this);

//        crds = CategoryRemoteDataSourceImpl.getInstance();
//        crds.categoryNetworkCall(MainActivity.this);

    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onSuccess(List<Meal> meals) {
        Log.i("TAG", "onSuccess: geot measl in main " + meals.size());
        Log.i("TAG", "onSuccess: geot measl in main " + meals.get(0).getStrCategory());

    }

    @Override
    public void onFailure(String errorMessage) {
        Log.i("TAG", "onFailure: somthin wrong main");
    }

    @Override
    public void onCategorySuccess(List<Category> categories) {
        Log.i("TAG", "onSuccess: geot measl in main " + categories.size());
        Log.i("TAG", "onSuccess: geot measl in main " + categories.get(0));
    }

    @Override
    public void onCategoryFailure(String errorMessage) {
        Log.i("TAG", "onFailure: somthin wrong mainssssss");
    }
}
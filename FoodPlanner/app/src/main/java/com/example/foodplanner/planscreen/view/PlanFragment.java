package com.example.foodplanner.planscreen.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.Repository.modelrepoitory.MealRepositoryImpl;
import com.example.foodplanner.Repository.modelrepoitory.PlanRepository;
import com.example.foodplanner.Repository.modelrepoitory.PlanRepositoryImpl;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSourceImpl;
import com.example.foodplanner.database.plannedmeal.PlannedMealLocalDataSourceImpl;
import com.example.foodplanner.favortitescreen.presenter.FavoriteScreenPresenterImpl;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;
import com.example.foodplanner.planscreen.presenter.PlanScreenPresenter;
import com.example.foodplanner.planscreen.presenter.PlanScreenPresenterImpl;

import java.util.List;

public class PlanFragment extends Fragment implements PlanScreenView {

    PlanScreenPresenter planScreenPresenter;

    public PlanFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlanRepository repo = PlanRepositoryImpl.getInstance(PlannedMealLocalDataSourceImpl.getInstance(requireContext()));
        MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), FavouriteMealLocalDataSourceImpl.getInstance(getContext()));

        planScreenPresenter = new PlanScreenPresenterImpl(this, repo);
        planScreenPresenter.addMealToPlan(new PlannedMeal("id","name","image","2025-02-14","55"));
        planScreenPresenter.addMealToPlan(new PlannedMeal("id2","name2","image4","2025-02-15","55"));
        planScreenPresenter.addMealToPlan(new PlannedMeal("id3","name3","image6","2025-02-16","55"));
    planScreenPresenter.getMealsForDate("2025-02-14");
    planScreenPresenter.getAllPlannedMeals();
    planScreenPresenter.deleteMealFromPlan(new PlannedMeal("id3","name3","image6","2025-02-16","55"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void showPlannedMeals(List<PlannedMeal> meals) {
        Log.d("plan", "showPlannedMeals: fragg" + meals.size() + meals.get(0).getMealName());
    }
}
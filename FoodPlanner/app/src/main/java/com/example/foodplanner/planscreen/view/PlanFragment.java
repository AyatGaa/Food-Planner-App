package com.example.foodplanner.planscreen.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.modelrepoitory.MealRepositoryImpl;
import com.example.foodplanner.Repository.modelrepoitory.PlanRepository;
import com.example.foodplanner.Repository.modelrepoitory.PlanRepositoryImpl;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSourceImpl;
import com.example.foodplanner.database.plannedmeal.PlannedMealLocalDataSourceImpl;

import com.example.foodplanner.network.FilterRemoteDataSourceImpl;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;
import com.example.foodplanner.planscreen.presenter.PlanScreenPresenter;
import com.example.foodplanner.planscreen.presenter.PlanScreenPresenterImpl;
import com.example.foodplanner.utils.AppFunctions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;


public class PlanFragment extends Fragment implements PlanScreenView, OnDeletePlanMealListener {

    PlanScreenPresenter planScreenPresenter;
    PlanAdapter planAdapter;
    CalendarView calendarView;
    String selectedDate;
    RecyclerView planRecyclerView;
    LinearLayout signInLayoutPlan;
    Button signInButtonPlan;

    public PlanFragment() {
        // Required empty public constructor
    }

    void setUi(View v) {
        calendarView = v.findViewById(R.id.planCalender);
        planRecyclerView = v.findViewById(R.id.recyclerViewPlannedMeals);
        signInLayoutPlan = v.findViewById(R.id.signInLayoutPlan);
        signInButtonPlan = v.findViewById(R.id.signInButtonPlan);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlanRepository repo = PlanRepositoryImpl.getInstance(PlannedMealLocalDataSourceImpl.getInstance(requireContext()));
        MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), FavouriteMealLocalDataSourceImpl.getInstance(getContext()), FilterRemoteDataSourceImpl.getInstance());
        planScreenPresenter = new PlanScreenPresenterImpl(this, repo);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        setUi(view);
        boolean userStatus = AppFunctions.isAuthenticated();
        if (!userStatus) {// is not auth=> need to sign in
            signInLayoutPlan.setVisibility(View.VISIBLE);
            planRecyclerView.setVisibility(View.GONE);
            calendarView.setVisibility(View.GONE);
        } else { // is auth then go through app ..

            signInLayoutPlan.setVisibility(View.GONE);
            planRecyclerView.setVisibility(View.VISIBLE);
            calendarView.setVisibility(View.VISIBLE);

            planRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            planAdapter = new PlanAdapter(requireContext(), new ArrayList<>(), this);
            planRecyclerView.setAdapter(planAdapter);

            selectedDate = getTodayDate();
             planScreenPresenter.getMealsForDate(selectedDate); // to show already existed meal

            calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
                selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, (month + 1), dayOfMonth);
                planScreenPresenter.getMealsForDate(selectedDate);
            });


        }

        signInButtonPlan.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_planFragment_to_signInFragment);
        });

        return view;
    }

    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format(Locale.getDefault(), "%d-%02d-%02d", year, (month + 1), day);

    }

    @Override
    public void showPlannedMeals(List<PlannedMeal> meals) {
        if (planAdapter != null) {
            planAdapter.updateMeals(meals);
        } else {
            planRecyclerView.setAdapter(planAdapter);
        }
    }

    @Override
    public void showSnackBar(PlannedMeal plannedMeal) {
        View rootView = requireActivity().findViewById(R.id.bottomNavigationView);
        Snackbar snackbar = Snackbar.make(planRecyclerView, "Meal deleted", Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(Color.parseColor("#B8B8B8"));
        snackbar.setTextColor(Color.WHITE);
        snackbar.setAnchorView(rootView);
        snackbar.show();
    }

    @Override
    public void onDeletePlanMealClick(PlannedMeal plannedMeal) {
        planScreenPresenter.deleteMealFromPlan(plannedMeal);
    }
}
package com.example.foodplanner.homescreen.view;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.MealOnlyRepository;
import com.example.foodplanner.Models.meals.MealOnlyRepositoryImpl;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.Repository.modelrepoitory.MealRepositoryImpl;
import com.example.foodplanner.Repository.modelrepoitory.PlanRepository;
import com.example.foodplanner.Repository.modelrepoitory.PlanRepositoryImpl;
import com.example.foodplanner.backup.favouritmeals.FavoriteMealFirebaseImpl;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSourceImpl;
import com.example.foodplanner.database.plannedmeal.PlannedMealLocalDataSourceImpl;
import com.example.foodplanner.homescreen.presenter.HomeScreenPresenter;
import com.example.foodplanner.homescreen.presenter.HomeScreenPresenterImpl;
import com.example.foodplanner.network.FilterRemoteDataSourceImpl;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;
import com.example.foodplanner.utils.AppFunctions;
import com.example.foodplanner.utils.BottomSheetFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeScreenFragment extends Fragment implements HomeScreenView, OnMealClickListener {

    ImageView mealOfTheDayImage;
    TextView mealOfTheDayTitle, mealOfTheDayInstructions, cozyMeals;
    RecyclerView homeRecyclerView;
    String selectedDate;
    LottieAnimationView loadingView;
    ConstraintLayout homeScreenConstraintLayout, mainContentLayout;
    LinearLayout mealDayCardLayout;
    HomeScreenAdapter homeScreenAdapter;
    HomeScreenPresenter homeScreenPresenter;
    List<Meal> meals = new ArrayList<>();


    void setupUI(View view) {
        mealOfTheDayImage = view.findViewById(R.id.mealOfTheDayImage);
        mealOfTheDayTitle = view.findViewById(R.id.mealOfTheDayTitle);
        mealOfTheDayInstructions = view.findViewById(R.id.mealOfTheDayInstructions);
        homeRecyclerView = view.findViewById(R.id.cozyMealRecyclerView);
        homeScreenConstraintLayout = view.findViewById(R.id.homeScreenConstraintLayout);
        cozyMeals = view.findViewById(R.id.cozyMeals);
        loadingView = view.findViewById(R.id.loadingView);
        homeScreenConstraintLayout = view.findViewById(R.id.homeScreenConstraintLayout);
        mainContentLayout = view.findViewById(R.id.mainContentLayout);
        mealDayCardLayout = view.findViewById(R.id.mealDayCardLayout);
    }

    public HomeScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        setupUI(view);
        showLoadingView();

        homeRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        homeRecyclerView.setLayoutManager(layoutManager);

        MealOnlyRepository mealRepo = MealOnlyRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), requireContext());
        MealRepository mealRepository = MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(),
                FavouriteMealLocalDataSourceImpl.getInstance(getContext()),
                FilterRemoteDataSourceImpl.getInstance(),
                FavoriteMealFirebaseImpl.getInstance());
        PlanRepository planRepository = PlanRepositoryImpl.getInstance(PlannedMealLocalDataSourceImpl.getInstance(requireContext()), FavoriteMealFirebaseImpl.getInstance());

        homeScreenPresenter = new HomeScreenPresenterImpl(this, mealRepo, requireContext(), mealRepository, planRepository);
        homeScreenAdapter = new HomeScreenAdapter(meals, getContext(), this);
        homeRecyclerView.setAdapter(homeScreenAdapter);

        homeScreenPresenter.getMeals();
        homeScreenPresenter.getRandomMeal();

        homeScreenPresenter.checkInternetConnection();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        homeScreenPresenter.getFavoriteMealsFirebase();
        selectedDate = getTodayDate();
        homeScreenPresenter.getPlannedMealsFirebase(selectedDate);
    }

    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format(Locale.getDefault(), "%d-%02d-%02d", year, (month + 1), day);

    }

    @Override
    public void setRandmoMealCard(Meal meal) {
        Glide.with(requireContext()).load(meal.getStrMealThumb())
                .error(R.drawable.notfound)
                .into(mealOfTheDayImage);

        mealOfTheDayTitle.setText(meal.getStrMeal());
        mealOfTheDayInstructions.setText(meal.getStrInstructions());
        mealDayCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRandomMealClick(meal);
            }
        });
    }

    @Override
    public void onRandomMealClick(Meal meal) {
        HomeScreenFragmentDirections.ActionHomeScreenFragmentToDetailedMealFragment action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToDetailedMealFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void showLoadingView() {
        loadingView.setVisibility(View.VISIBLE);
        mainContentLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoadingView() {
        loadingView.setVisibility(View.GONE);
        mainContentLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void showOnNoConnection() {
        if (!AppFunctions.isConnected(requireContext())) {
            clearUI();
            setNoConnectionUI();
            BottomSheetFragment bottomSheet = new BottomSheetFragment();
            bottomSheet.show(getParentFragmentManager(), "NoConnectionBottomSheet");
        }
    }

    @Override
    public void clearUI() {
        mainContentLayout.removeAllViews();
    }

    @Override
    public void setNoConnectionUI() {

        cozyMeals.setText(getString(R.string.no_connection));
        cozyMeals.setGravity(Gravity.TOP);


        ConstraintLayout.LayoutParams textParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.topMargin = 80;
        cozyMeals.setLayoutParams(textParams);

        homeScreenConstraintLayout.addView(loadingView);
        homeScreenConstraintLayout.addView(cozyMeals);
    }

    @Override
    public void showMeals(List<Meal> meals) {

        if (meals != null && !meals.isEmpty()) {
            hideLoadingView();
        } else {
            showLoadingView();
        }

        homeScreenAdapter = new HomeScreenAdapter(meals, getContext(), this);
        homeRecyclerView.setAdapter(homeScreenAdapter);
        homeScreenAdapter.setList(meals);
        homeScreenAdapter.notifyDataSetChanged();

    }

    @Override
    public void onMealClick(Meal meal) {

        HomeScreenFragmentDirections.ActionHomeScreenFragmentToDetailedMealFragment action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToDetailedMealFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }
}
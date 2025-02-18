package com.example.foodplanner.homescreen.view;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodplanner.MainActivity;
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
    HomeScreenPresenter homeScreenPresenter;
    ProgressBar progressBarMealOfDay;
    HomeScreenAdapter homeScreenAdapter;
    LinearLayout mealDayCardLayout;
    ConstraintLayout homeScreenConstraintLayout;

    List<Meal> meals = new ArrayList<>();


    void setupUI(View view) {
        mealOfTheDayImage = view.findViewById(R.id.mealOfTheDayImage);
        mealOfTheDayTitle = view.findViewById(R.id.mealOfTheDayTitle);
        mealOfTheDayInstructions = view.findViewById(R.id.mealOfTheDayInstructions);
        homeRecyclerView = view.findViewById(R.id.cozyMealRecyclerView);
        homeScreenConstraintLayout = view.findViewById(R.id.homeScreenConstraintLayout);
        progressBarMealOfDay = view.findViewById(R.id.progressBarMealOfDay);
        cozyMeals = view.findViewById(R.id.cozyMeals);
        mealDayCardLayout = view.findViewById(R.id.mealDayCardLayout);
    }

    public HomeScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        setupUI(view);
        showProgressBar();
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
        homeScreenPresenter = new HomeScreenPresenterImpl(this, mealRepo, requireContext(),mealRepository , planRepository);
        homeScreenAdapter = new HomeScreenAdapter(meals, getContext(), this);
        Log.i("TAG", "onCreateView: here" + meals.size());
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
        Glide.with(getContext()).load(meal.getStrMealThumb())
                .error(R.drawable.ic_launcher_background)
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
    public void showProgressBar() {
        progressBarMealOfDay.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBarMealOfDay.setVisibility(View.GONE);
    }

    @Override
    public void onRandomMealClick(Meal meal) {
        Log.i("TAG", "onRandomMealClick: clicked");
        HomeScreenFragmentDirections.ActionHomeScreenFragmentToDetailedMealFragment action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToDetailedMealFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
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
        homeScreenConstraintLayout.removeAllViews();
    }

    @Override
    public void setNoConnectionUI() {

        cozyMeals.setText(getString(R.string.no_connection));
        cozyMeals.setGravity(Gravity.CENTER);

        // Layout Params for TextView
        ConstraintLayout.LayoutParams textParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        cozyMeals.setLayoutParams(textParams);

        // Create ImageView
        ImageView noConnectionImage = new ImageView(requireContext());
        noConnectionImage.setId(View.generateViewId());
        noConnectionImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        // Layout Params for ImageView
        ConstraintLayout.LayoutParams imageParams =
                new ConstraintLayout.LayoutParams(200, 400);

        imageParams.bottomToTop = cozyMeals.getId();
        imageParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        imageParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        imageParams.topMargin = 60;
        noConnectionImage.setLayoutParams(imageParams);

        Glide.with(requireContext())
                .load(R.drawable.cutlery_primary_color)
                .error(R.drawable.ic_launcher_background)
                .into(noConnectionImage);

        homeScreenConstraintLayout.addView(noConnectionImage);
        homeScreenConstraintLayout.addView(cozyMeals);
    }

    @Override
    public void setBottomNavEnabled(boolean isConnected) {
        ((MainActivity) requireActivity()).setBottomNavEnabled(isConnected);
    }

    @Override
    public void showMeals(List<Meal> meals) {

        if (meals.size() > 0) {
            hideProgressBar();
        }
        homeScreenAdapter = new HomeScreenAdapter(meals, getContext(), this);
        homeRecyclerView.setAdapter(homeScreenAdapter);
        homeScreenAdapter.setList(meals);
        homeScreenAdapter.notifyDataSetChanged();

    }

    @Override
    public void onMealClick(Meal meal) {
        Toast.makeText(requireContext(), meal.getStrMeal(), Toast.LENGTH_SHORT).show();

        Log.d("home", "Clicked meal: " + meal.getIdMeal() + " - " + meal.getStrMeal() + " - " + meal.getStrMealThumb());
        Log.d("home", "Clicked meal: " + meal.getStrInstructions() + " - " + meal.getStrCategory());


        HomeScreenFragmentDirections.ActionHomeScreenFragmentToDetailedMealFragment action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToDetailedMealFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }
}
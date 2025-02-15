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

import com.bumptech.glide.Glide;
import com.example.foodplanner.MainActivity;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.MealRepository;
import com.example.foodplanner.Models.meals.MealRepositoryImpl;
import com.example.foodplanner.R;
import com.example.foodplanner.homescreen.presenter.HomeScreenPresenter;
import com.example.foodplanner.homescreen.presenter.HomeScreenPresenterImpl;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;
import com.example.foodplanner.utils.AppFunctions;
import com.example.foodplanner.utils.BottomSheetFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenFragment extends Fragment implements HomeScreenView, OnMealClickListener {

    ImageView mealOfTheDayImage;
    TextView mealOfTheDayTitle, mealOfTheDayInstructions, cozyMeals;
    RecyclerView homeRecyclerView;
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

        MealRepository mealRepo = MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance());

        homeScreenPresenter = new HomeScreenPresenterImpl(this, mealRepo, requireContext());
        homeScreenAdapter = new HomeScreenAdapter(meals, getContext(), this);
        Log.i("TAG", "onCreateView: here" + meals.size());
        homeRecyclerView.setAdapter(homeScreenAdapter);

        homeScreenPresenter.getMeals();
        homeScreenPresenter.getRandomMeal();
        homeScreenPresenter.checkInternetConnection();


        return view;
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

        HomeScreenFragmentDirections.ActionHomeScreenFragmentToDetailedMealFragment action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToDetailedMealFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }
}
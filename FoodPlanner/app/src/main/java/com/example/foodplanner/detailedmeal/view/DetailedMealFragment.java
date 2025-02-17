package com.example.foodplanner.detailedmeal.view;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.Repository.modelrepoitory.MealRepositoryImpl;
import com.example.foodplanner.Repository.modelrepoitory.PlanRepository;
import com.example.foodplanner.Repository.modelrepoitory.PlanRepositoryImpl;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSourceImpl;
import com.example.foodplanner.database.plannedmeal.PlannedMealLocalDataSourceImpl;
import com.example.foodplanner.detailedmeal.presenter.DetailedMealPresenter;
import com.example.foodplanner.detailedmeal.presenter.DetailedMealPresenterImpl;
import com.example.foodplanner.network.FilterRemoteDataSourceImpl;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;
import com.example.foodplanner.network.NetworkCallback;
import com.example.foodplanner.searchscreen.presenter.SearchScreenPresenter;
import com.example.foodplanner.utils.AppFunctions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailedMealFragment extends Fragment implements DetailedMealView, NetworkCallback {

    DetailedMealPresenter detailedMealPresenter;
    ImageView mealImageDetailed, countryFlagDetailed;
    TextView mealNameDetailed, instructionsTextDetailed, detailedMealHeader, countryFlagName;
    RecyclerView ingredientsRecyclerView;
    WebView mealVideo;
    SearchScreenPresenter searchPresenter;
    IngredientAdapter ingredientsAdapter;
    Button btnAddToFavoritesDetailed, btnAddToPlanMealDetailed;
    List<String> ingredientList = new ArrayList<>();

    void setupUI(View view) {
        ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        mealImageDetailed = view.findViewById(R.id.mealImageDetailed);
        mealNameDetailed = view.findViewById(R.id.mealNameDetailed);
        instructionsTextDetailed = view.findViewById(R.id.instructionsTextDetailed);
        btnAddToFavoritesDetailed = view.findViewById(R.id.btnAddToFavoritesDetailed);
        countryFlagDetailed = view.findViewById(R.id.countryFlagDetailed);
        detailedMealHeader = view.findViewById(R.id.detailedMealHeader);
        btnAddToPlanMealDetailed = view.findViewById(R.id.btnAddToPlanMealDetailed);
        countryFlagName = view.findViewById(R.id.countryFlagName);
        mealVideo = view.findViewById(R.id.mealVideo);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meal_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUI(view);
        MealRepository repo = MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), FavouriteMealLocalDataSourceImpl.getInstance(getContext()), FilterRemoteDataSourceImpl.getInstance());
        PlanRepository planRepository = PlanRepositoryImpl.getInstance(PlannedMealLocalDataSourceImpl.getInstance(requireContext()));

        detailedMealPresenter = new DetailedMealPresenterImpl(repo, this, planRepository);

        ingredientsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ingredientsRecyclerView.setLayoutManager(layoutManager);

        Meal meal = DetailedMealFragmentArgs.fromBundle(getArguments()).getDetailedMeal();


        if (meal.getStrInstructions() == null) { // not complete mal object
            detailedMealPresenter.fetchMealDetails(meal.getIdMeal());
        } else {
            showMealDetails(meal); // complete meal object
        }


        btnAddToFavoritesDetailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("fav", "onClick: on detaled fragment" + meal.getIdMeal());
                String userId = AppFunctions.getCurrentUserId();
                if (userId != null) {
                    meal.setUserId(userId);
                    detailedMealPresenter.onAddToFavourite(meal);
                    Log.d("MealDetailFragment", "Meal received: " + meal.getStrMeal());
                    showAddedSnackBar(meal, "Meal added to favorites");

                } else {
                    Log.e("fav", " User not authenticated. Cannot add meal to favorites.");
                }
            }
        });

        btnAddToPlanMealDetailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickers(meal);
            }
        });
    }

//    void setIngredineUi(Meal meal) {
//        detailedMealHeader.setText(meal.getStrMeal());
//        mealNameDetailed.setText(meal.getStrMeal());
//        countryFlagName.setText(meal.getStrArea());
//        String instructions = meal.getStrInstructions();
//        instructions = instructions.replaceAll("\r\n|\r|\n", "\n\n");
//        instructionsTextDetailed.setText(instructions);
//
//        String countryCode = AppFunctions.getCountryCode(meal.getStrArea()).toLowerCase();
//
//        Glide.with(requireContext()).load("https://flagcdn.com/w320/" + countryCode + ".png")
//                .error(R.drawable.ic_launcher_background)
//                .into(countryFlagDetailed);
//
//        Glide.with(requireContext()).load(meal.getStrMealThumb())
//                .error(R.drawable.ic_launcher_background)
//                .into(mealImageDetailed);
//    }


    @Override
    public void showAddedSnackBar(Meal meal, String message) {
        View rootView = requireActivity().findViewById(R.id.bottomNavigationView);
        Snackbar snackbar = Snackbar.make(ingredientsRecyclerView, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.parseColor("#3E5879"));
        snackbar.setTextColor(Color.WHITE);
        snackbar.setAnchorView(rootView);
        snackbar.show();
    }

    @Override
    public void showVideoPlayer(Meal meal) {
        mealVideo.getSettings().setJavaScriptEnabled(true);
        String videoId = detailedMealPresenter.extractYoutubeVideoId(meal.getStrYoutube());
        String embedHtml = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

        mealVideo.loadData(embedHtml, "text/html", "utf-8");

    }

    @Override
    public void showDatePickers(Meal meal) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.CustomDatePickerDialog,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "-" + String.format("%02d", (selectedMonth + 1)) + "-" + String.format("%02d", selectedDay);
                    String userId = AppFunctions.getCurrentUserId();

                    Log.d("date", "onClick: " + selectedDate);
                    PlannedMeal plannedMeal = new PlannedMeal(userId, meal.getIdMeal(), meal.getStrMeal(), meal.getStrMealThumb(), selectedDate);
                    if (detailedMealPresenter.isFutureDate(selectedDate)) {

                        detailedMealPresenter.onAddToPlan(plannedMeal);
                        //       showAddedSnackBar(meal, "Meal Planned Successfully!");
                    } else {
                        Toast.makeText(requireContext(), "Cannot add meal to a past date", Toast.LENGTH_SHORT).show();
                    }

                }, year, month, day);


        datePickerDialog.show();
    }

    @Override
    public void showMealList(List<Meal> meals) {


    }

    @Override
    public void showMealDetails(Meal meal) {

        detailedMealHeader.setText(meal.getStrMeal());
        mealNameDetailed.setText(meal.getStrMeal());
        countryFlagName.setText(meal.getStrArea());
        Log.d("DetailedMealFragment", "Clicked meal: " + meal.getIdMeal() + " - " + meal.getStrMeal() + " - " + meal.getStrMealThumb());
        Log.d("DetailedMealFragment", "Clicked meal: " + meal.getStrInstructions() + " - " + meal.getStrCategory());

        instructionsTextDetailed.setText(meal.getStrInstructions().replaceAll("\r\n|\r|\n", "\n\n"));


        String countryCode = AppFunctions.getCountryCode(meal.getStrArea()).toLowerCase();
        Glide.with(requireContext()).load("https://flagcdn.com/w320/" + countryCode + ".png")
                .error(R.drawable.ic_launcher_background)
                .into(countryFlagDetailed);


        Glide.with(requireContext()).load(meal.getStrMealThumb())
                .error(R.drawable.ic_launcher_background)
                .into(mealImageDetailed);


        ingredientList = detailedMealPresenter.getIngredient(meal);
        ingredientsAdapter = new IngredientAdapter(ingredientList, requireContext());
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        ingredientsAdapter.notifyDataSetChanged();

        showVideoPlayer(meal);
    }


    @Override
    public void onSuccess(List<Meal> meals) {

    }


    @Override
    public void onSuccessArea(List<Meal> meals) {

    }

    @Override
    public void onFailure(String errorMessage) {

    }
}



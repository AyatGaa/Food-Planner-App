package com.example.foodplanner.detailedmeal.view;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.example.foodplanner.Models.ingredient.Ingredient;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.Repository.modelrepoitory.MealRepositoryImpl;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSourceImpl;
import com.example.foodplanner.detailedmeal.presenter.DetailedMealPresenter;
import com.example.foodplanner.detailedmeal.presenter.DetailedMealPresenterImpl;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;
import com.example.foodplanner.utils.AppFunctions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailedMealFragment extends Fragment implements DetailedMealView {

    DetailedMealPresenter detailedMealPresenter;
    ImageView mealImageDetailed, countryFlagDetailed;
    TextView mealNameDetailed, instructionsTextDetailed, detailedMealHeader, countryFlagName;
    RecyclerView ingredientsRecyclerView;
    WebView mealVideo;
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
        MealRepository repo = MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), FavouriteMealLocalDataSourceImpl.getInstance(getContext()));
        detailedMealPresenter = new DetailedMealPresenterImpl(repo, this);

        ingredientsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ingredientsRecyclerView.setLayoutManager(layoutManager);

        Meal meal = DetailedMealFragmentArgs.fromBundle(getArguments()).getDetailedMeal();
        ingredientList = getIngredient(meal);
        setIngredineUi(meal);
        showVideoPlayer(meal);
        ingredientsAdapter = new IngredientAdapter(ingredientList, requireContext());
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        ingredientsAdapter.notifyDataSetChanged();

        btnAddToFavoritesDetailed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("fav", "onClick: on detaled fragment" + meal.getIdMeal());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = AppFunctions.getCurrentUserId();
                if (userId != null) {
                    meal.setUserId(userId);
                    detailedMealPresenter.onAddToFavourite(meal);
                    // Toast.makeText(requireContext(), "meal added to favorites" + meal.getStrMeal(), Toast.LENGTH_SHORT).show();
                    showAddedSnackBar(meal);

                } else {
                    Log.e("fav", " User not authenticated. Cannot add meal to favorites.");
                }


            }
        });
    }

    void setIngredineUi(Meal meal) {
        detailedMealHeader.setText(meal.getStrMeal());
        mealNameDetailed.setText(meal.getStrMeal());
        countryFlagName.setText(meal.getStrArea());
        String instructions = meal.getStrInstructions();
        instructions = instructions.replaceAll("\r\n|\r|\n", "\n\n");
        instructionsTextDetailed.setText(instructions);

        String countryCode = AppFunctions.getCountryCode(meal.getStrArea()).toLowerCase();

        Glide.with(requireContext()).load("https://flagcdn.com/w320/" + countryCode + ".png")
                .error(R.drawable.ic_launcher_background)
                .into(countryFlagDetailed);

        Glide.with(requireContext()).load(meal.getStrMealThumb())
                .error(R.drawable.ic_launcher_background)
                .into(mealImageDetailed);


    }

    List<String> getIngredient(Meal meal) {
        List<String> ingredientsList = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            try {
                String ingredient = (String) meal.getClass().getDeclaredField("strIngredient" + i).get(meal);
                if (ingredient != null && !ingredient.trim().isEmpty()) {
                    ingredientsList.add(ingredient);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i("TAG", "Extracted Ingredients: " + ingredientsList);
        return ingredientsList;
    }


    @Override
    public void showAddedSnackBar(Meal meal) {
        View rootView = getActivity().findViewById(R.id.bottomNavigationView);
        Snackbar snackbar = Snackbar.make(ingredientsRecyclerView, "Meal Added", Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.parseColor("#3E5879")); // Change background color
        snackbar.setTextColor(Color.WHITE);
        snackbar.setAnchorView(rootView);
        snackbar.show();
    }

    @Override
    public void showVideoPlayer(Meal meal) {
        mealVideo.getSettings().setJavaScriptEnabled(true);
        String videoId = extractYoutubeVideoId(meal.getStrYoutube());
        String embedHtml = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

        mealVideo.loadData(embedHtml, "text/html", "utf-8");

    }

    public String extractYoutubeVideoId(String url) {

        String pattern = "^(?:https?:\\/\\/)?(?:www\\.)?(?:youtube\\.com\\/(?:[^\\/]+\\/.*|(?:v|e(?:mbed)?)\\/|.*[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1); // Extract video ID
        }
        return null; // No valid video ID found
    }

}



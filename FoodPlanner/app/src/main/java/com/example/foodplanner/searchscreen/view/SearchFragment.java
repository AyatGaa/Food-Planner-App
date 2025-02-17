package com.example.foodplanner.searchscreen.view;

import static io.reactivex.rxjava3.internal.operators.observable.ObservableBlockingSubscribe.subscribe;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Models.area.Area;
import com.example.foodplanner.Models.category.Category;
import com.example.foodplanner.Models.ingredient.Ingredient;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.modelrepoitory.MealRepositoryImpl;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSourceImpl;
import com.example.foodplanner.homescreen.view.OnMealClickListener;
import com.example.foodplanner.network.FilterRemoteDataSourceImpl;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;
import com.example.foodplanner.searchscreen.presenter.SearchScreenPresenter;
import com.example.foodplanner.searchscreen.presenter.SearchScreenPresenterImpl;
import com.example.foodplanner.utils.AppFunctions;
import com.example.foodplanner.utils.BottomSheetFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchFragment extends Fragment implements SearchScreenView, OnSearchMealClickListener {

    SearchScreenPresenter searchPresenter;
    RecyclerView searchRecyclerView;
    ChipGroup searchChipGroup;
    TextView searchTitle;
    FilterAdapter filterAdapter;
    RecyclerView mealsRecyclerView;
    ConstraintLayout searchScreenConstraintLayout;
    MealsAdapter mealsAdapter;
    SearchView searchView;
    String selectedChip = "";

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SearchFragment() {
    }

    void updateMealList(List<Meal> meals) {
        searchRecyclerView.setVisibility(View.GONE);
        mealsRecyclerView.setVisibility(View.VISIBLE);

        mealsAdapter.setList(meals);
    }

    void setUi(View v) {
        searchRecyclerView = v.findViewById(R.id.searchRecyclerView);
        mealsRecyclerView = v.findViewById(R.id.mealsRecyclerView);
        searchChipGroup = v.findViewById(R.id.searchChipGroup);
        searchView = v.findViewById(R.id.searchView);
        searchTitle = v.findViewById(R.id.searchTitle);
        searchScreenConstraintLayout = v.findViewById(R.id.searchScreenConstraintLayout);

        searchRecyclerView.setVisibility(View.VISIBLE);
        mealsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        setUi(view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        GridLayoutManager mealGridLayoutManager = new GridLayoutManager(getContext(), 2);

        searchRecyclerView.setLayoutManager(gridLayoutManager);
        mealsRecyclerView.setLayoutManager(mealGridLayoutManager);

        filterAdapter = new FilterAdapter(new ArrayList(), requireContext());
        mealsAdapter = new MealsAdapter(new ArrayList(), requireContext(), this);

        searchRecyclerView.setAdapter(filterAdapter);
        mealsRecyclerView.setAdapter(mealsAdapter);

        searchPresenter = new SearchScreenPresenterImpl(new MealRepositoryImpl(
                MealRemoteDataSourceImpl.getInstance(),
                FavouriteMealLocalDataSourceImpl.getInstance(requireContext()),
                FilterRemoteDataSourceImpl.getInstance()
        ), this, requireContext());

        handleChipSelection(view);
        setupSearchObservable();
        searchPresenter.checkInternetConnection();

        return view;
    }


    private void handleChipSelection(View view) {
        searchChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                selectedChip = "";
                searchTitle.setText(getString(R.string.meal));
                performSearch("");
                return;
            }

            for (int id : checkedIds) {
                Chip chip = view.findViewById(id);
                if (chip != null) {
                    if (chip.getId() == R.id.categoryChip) {

                        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    chip.setChecked(true);
                                    selectedChip = getString(R.string.category);
                                    chip.setChipBackgroundColorResource(R.color.gray_light);
                                    searchTitle.setText(selectedChip);
                                    searchPresenter.getAllCategories();
                                } else {
                                    chip.setChecked(false);
                                    chip.setChipBackgroundColorResource(R.color.white);
                                    filterAdapter.updateList(new ArrayList<>());
                                    mealsAdapter.setList(new ArrayList<>());
                                    searchView.setQuery("",false);
                                }
                            }
                        });
                    } else if (chip.getId() == R.id.countryChip) {

                        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    chip.setChecked(true);
                                    chip.setChipBackgroundColorResource(R.color.gray_light);

                                    selectedChip = getString(R.string.country);

                                    searchTitle.setText(selectedChip);
                                    searchPresenter.getAllAreas();
                                } else {
                                    chip.setChecked(false);
                                    chip.setChipBackgroundColorResource(R.color.white);
                                    filterAdapter.updateList(new ArrayList<>());
                                    mealsAdapter.setList(new ArrayList<>());
                                    searchView.setQuery("",false);
                                }
                            }
                        });


                    } else if (chip.getId() == R.id.ingredientChip) {

                        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    chip.setChecked(true);
                                    selectedChip = getString(R.string.ingredient);
                                    chip.setChipBackgroundColorResource(R.color.gray_light);
                                    searchTitle.setText(selectedChip);
                                    searchPresenter.getAllIngredients();
                                } else {
                                    chip.setChecked(false);
                                    chip.setChipBackgroundColorResource(R.color.white);
                                    filterAdapter.updateList(new ArrayList<>());
                                    mealsAdapter.setList(new ArrayList<>());
                                    searchView.setQuery("",false);
                                }
                            }
                        });


                    } else {

                        selectedChip = "";
                        searchTitle.setText(getString(R.string.meal));
                        searchPresenter.getAllMeals("");
                        break;
                    }
                }
            }
        });
searchView.setQueryRefinementEnabled(true);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mealsAdapter.setList(new ArrayList<>());
                filterAdapter.updateList(new ArrayList<>());
                return false;
            }
        });
    }

    public void performSearch(String query) {
        if (query.isEmpty()) return;

        switch (selectedChip) {
            case "Category":

                if (!selectedChip.isEmpty()) {
                    searchPresenter.getMealsByCategory(query);  // Your existing method call
                    Log.d("SearchFragment", "Searching by Category: " + query);
                }
                break;
            case "Country":
                if (!selectedChip.isEmpty()) {
                    searchPresenter.getMealsByArea(query);// Your existing method call
                    Log.d("SearchFragment", "Searching by Category: " + query);
                }

                Log.d("SearchFragment", "Searching by Country: " + query);
                break;
            case "Ingredient":
                if (!selectedChip.isEmpty()) {
                    searchPresenter.getMealsByIngredient(query);

                    Log.d("SearchFragment", "Searching by Category: " + query);
                }

                Log.d("SearchFragment", "Searching by Ingredient: " + query);
                break;
            default:
                searchTitle.setText(getString(R.string.meal));
                searchPresenter.getAllMeals(query);
                Log.d("SearchFragment", "Performing general meal search: " + query);
                break;
        }
    }


    private void setupSearchObservable() {
        Observable<String> searchObservable = Observable.create(emitter -> {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    emitter.onNext(query);
                    performSearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    emitter.onNext(newText);
                    performSearch(newText);
                    return true;
                }
            });
        });

        compositeDisposable.add(
                searchObservable
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(query -> performSearch(query)));
    }


    @Override
    public void showOnNoConnectionSearch() {
        if (!AppFunctions.isConnected(requireContext())) {
            clearSearchUI();
            setNoConnectionSearchUI();
            BottomSheetFragment bottomSheet = new BottomSheetFragment();
            bottomSheet.show(getParentFragmentManager(), "NoConnectionBottomSheet");
        }
    }

    @Override
    public void clearSearchUI() {
        searchScreenConstraintLayout.removeAllViews();
    }

    @Override
    public void setNoConnectionSearchUI() {
        searchTitle.setText(getString(R.string.no_connection));
        searchTitle.setGravity(Gravity.CENTER);

        // Layout Params for TextView
        ConstraintLayout.LayoutParams textParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        searchTitle.setLayoutParams(textParams);

        // Create ImageView
        ImageView noConnectionImage = new ImageView(requireContext());
        noConnectionImage.setId(View.generateViewId());
        noConnectionImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        // Layout Params for ImageView
        ConstraintLayout.LayoutParams imageParams =
                new ConstraintLayout.LayoutParams(200, 400);

        imageParams.bottomToTop = searchTitle.getId();
        imageParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        imageParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        imageParams.topMargin = 60;
        noConnectionImage.setLayoutParams(imageParams);

        Glide.with(requireContext())
                .load(R.drawable.cutlery_primary_color)
                .error(R.drawable.ic_launcher_background)
                .into(noConnectionImage);

        searchScreenConstraintLayout.addView(noConnectionImage);
        searchScreenConstraintLayout.addView(searchTitle);

    }

    @Override
    public void showListOfMealByCategorty(List<Meal> meals) {
        Log.d("SearchFragment", "showListOfMealByCategory: =>" + meals.size());
        updateMealList(meals);
    }

    @Override
    public void showListOfMealByArea(List<Meal> meals) {
        Log.d("SearchFragment", "showListOfMealByArea: =>" + meals.size());
        updateMealList(meals);
    }

    @Override
    public void showListOfMealByIngredient(List<Meal> meals) {
        Log.d("SearchFragment", "showListOfMealByIngredient: => " + meals.size());
        updateMealList(meals);
    }

    @Override
    public void showAllAreas(List<Area> areas) {
        if (areas != null && !areas.isEmpty()) {
            filterAdapter.updateList(areas);
            searchRecyclerView.setAdapter(filterAdapter);
            filterAdapter.notifyDataSetChanged();

            searchRecyclerView.setVisibility(View.VISIBLE);
            mealsRecyclerView.setVisibility(View.GONE);
        } else {

            Toast.makeText(requireContext(), "No categories found.", Toast.LENGTH_SHORT).show();
        }

        Log.d("area", "showAllAreas:  = > " + areas.size());


    }

    @Override
    public void showAllCategories(List<Category> categories) {
        // Check if the categories list is null or empty before updating the UI
        if (categories != null && !categories.isEmpty()) {
            filterAdapter.updateList(categories);
            searchRecyclerView.setAdapter(filterAdapter);
            filterAdapter.notifyDataSetChanged();


            searchRecyclerView.setVisibility(View.VISIBLE);
            mealsRecyclerView.setVisibility(View.GONE);
        } else {

            Toast.makeText(requireContext(), "No categories found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showAllIngredients(List<Ingredient> ingredients) {

        if (ingredients != null && !ingredients.isEmpty()) {
            filterAdapter.updateList(ingredients);
            searchRecyclerView.setAdapter(filterAdapter);
            filterAdapter.notifyDataSetChanged();

            searchRecyclerView.setVisibility(View.VISIBLE);
            mealsRecyclerView.setVisibility(View.GONE);
        } else {

            Toast.makeText(requireContext(), "No categories found.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void showMealList(List<Meal> meals) {
        if (meals == null || meals.isEmpty()) {
            Log.d("SearchFragment", "showMealList: No meals found.");
            return;
        }
        mealsAdapter.setList(meals);
        mealsAdapter.notifyDataSetChanged();

        mealsRecyclerView.setVisibility(View.VISIBLE);
        searchRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showMealDetailsSearch(Meal meal) {
        searchPresenter.getMealsByArea(meal.getIdMeal());
    }


    @Override
    public void onSearchMealClick(Meal meal) {

        Toast.makeText(requireContext(), meal.getStrMeal(), Toast.LENGTH_SHORT).show();

        Log.d("SearchFragment", "Clicked meal: " + meal.getIdMeal() + " - " + meal.getStrMeal() + " - " + meal.getStrMealThumb());
        Log.d("SearchFragment", "Clicked meal: " + meal.getStrInstructions() + " - " + meal.getStrCategory());
        SearchFragmentDirections.ActionSearchFragmentToDetailedMealFragment action =
                SearchFragmentDirections.actionSearchFragmentToDetailedMealFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }


}
package com.example.foodplanner.searchscreen.view;

import static io.reactivex.rxjava3.internal.operators.observable.ObservableBlockingSubscribe.subscribe;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplanner.Models.area.Area;
import com.example.foodplanner.Models.category.Category;
import com.example.foodplanner.Models.ingredient.Ingredient;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.modelrepoitory.MealRepositoryImpl;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSourceImpl;
import com.example.foodplanner.network.FilterRemoteDataSourceImpl;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;
import com.example.foodplanner.searchscreen.presenter.SearchScreenPresenter;
import com.example.foodplanner.searchscreen.presenter.SearchScreenPresenterImpl;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchFragment extends Fragment implements SearchScreenView , OnMealClickListener {

    SearchScreenPresenter searchPresenter;
    RecyclerView searchRecyclerView;
    ChipGroup searchChipGroup;
    TextView searchTitle;
    FilterAdapter filterAdapter;
    RecyclerView mealsRecyclerView;

    MealsAdapter mealsAdapter;
    SearchView searchView;
    String selectedChip = "";

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SearchFragment() {
        // Required empty public constructor
    }
    void updateMealList(List<Meal> meals) {
        // Hide filters and show meal list
        searchRecyclerView.setVisibility(View.GONE);
        mealsRecyclerView.setVisibility(View.VISIBLE);

        // Update the meal recycler view
        mealsAdapter.setList(meals);
    }
    void setUi(View v) {
        searchRecyclerView = v.findViewById(R.id.searchRecyclerView);
        mealsRecyclerView = v.findViewById(R.id.mealsRecyclerView);
        searchChipGroup = v.findViewById(R.id.searchChipGroup);
        searchView = v.findViewById(R.id.searchView);
        searchTitle = v.findViewById(R.id.searchTitle);

        // Initially only show filters
        searchRecyclerView.setVisibility(View.VISIBLE);
        mealsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setUi(view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        GridLayoutManager mealGridLayoutManager = new GridLayoutManager(getContext(), 1);

        searchRecyclerView.setLayoutManager(gridLayoutManager);
        mealsRecyclerView.setLayoutManager(mealGridLayoutManager);

        filterAdapter = new FilterAdapter(new ArrayList(), requireContext());
        mealsAdapter = new MealsAdapter(new ArrayList(), requireContext(), this::onMealClick);

        searchRecyclerView.setAdapter(filterAdapter);
        mealsRecyclerView.setAdapter(mealsAdapter);

        searchPresenter = new SearchScreenPresenterImpl(new MealRepositoryImpl(
                MealRemoteDataSourceImpl.getInstance(),
                FavouriteMealLocalDataSourceImpl.getInstance(requireContext()),
                FilterRemoteDataSourceImpl.getInstance()
        ), this);
        handleChipSelection(view);
        setupSearchObservable();
//        searchChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
//            if (checkedIds.isEmpty()) {
//                selectedChip = "";
//            } else {
//                for (int id : checkedIds) {
//
//                    Chip chip = view.findViewById(id);
//
//                    if (chip != null) {
//
//                        if (chip == view.findViewById(R.id.categoryChip)) {
//                            selectedChip = getString(R.string.category);
//                            Log.i("chip", "onCreateView: chip cat clicked" + selectedChip);
//                            searchPresenter.getAllCategories();
//                        } else if (chip == view.findViewById(R.id.countryChip)) {
//                            selectedChip = getString(R.string.country);
//                            Log.i("chip", "onCreateView: chip cat clicked" + selectedChip);
//                            searchPresenter.getAllAreas();
//                        } else if (chip == view.findViewById(R.id.ingredientChip)) {
//
//                            selectedChip = getString(R.string.ingredient);
//                            Log.i("chip", "onCreateView: chip cat clicked" + selectedChip);
//                            searchPresenter.getAllIngredients();
//                        }
//                    }
//                }
//            }
//
//        });
//

        return view;
    }

    private void handleChipSelection(View view) {
        searchChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                selectedChip = ""; // Clear selection if no chip is selected
                return;
            }

            for (int id : checkedIds) {
                Chip chip = view.findViewById(id);
                if (chip != null) {
                    if (chip.getId() == R.id.categoryChip) {
                        selectedChip = getString(R.string.category);
                        Log.d("SearchFragment", "Category Chip Selected: " + selectedChip);
                        searchTitle.setText(selectedChip);
                        searchPresenter.getAllCategories();
                    } else if (chip.getId() == R.id.countryChip) {
                        selectedChip = getString(R.string.country);
                        Log.d("SearchFragment", "Country Chip Selected: " + selectedChip);
                        searchTitle.setText(selectedChip);
                        searchPresenter.getAllAreas();
                    } else if (chip.getId() == R.id.ingredientChip) {
                        selectedChip = getString(R.string.ingredient);
                        Log.d("SearchFragment", "Ingredient Chip Selected: " + selectedChip);
                        searchTitle.setText(selectedChip);
                        searchPresenter.getAllIngredients();
                    }
                }
            }
        });
    }

    public void performSearch(String query) {
        if (query.isEmpty()) return; // No query entered, do nothing

        // Perform search based on selected chip
        switch (selectedChip) {
            case "Category":
                searchPresenter.getMealsByCategory(query);
                Log.d("SearchFragment", "Searching by Category: " + query);
                break;
            case "Country":
                searchPresenter.getMealsByArea(query); // Ensure this matches the API method
                Log.d("SearchFragment", "Searching by Country: " + query);
                break;
            case "Ingredient":
                searchPresenter.getMealsByIngredient(query);
                Log.d("SearchFragment", "Searching by Ingredient: " + query);
                break;
            default:
                searchTitle.setText("Meals");
                searchPresenter.getAllMeals(query); // Regular search if no filter is selected
                Log.d("SearchFragment", "Performing general meal search: " + query);
                break;
        }
    }

//    private void handleChipSelection(View view) {
//        searchChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
//            if (checkedIds.isEmpty()) {
//                selectedChip = "";
//
//            } else {
//                for (int id : checkedIds) {
//                    Chip chip = view.findViewById(id);
//                    if (chip != null) {
//                        if (chip.getId() == R.id.categoryChip) {
//                            selectedChip = getString(R.string.category);
//                            searchTitle.setText(selectedChip);
//                            searchPresenter.getAllCategories();
//
//                        } else if (chip.getId() == R.id.countryChip) {
//                            selectedChip = getString(R.string.country);
//                            searchTitle.setText(selectedChip);
//                            searchPresenter.getAllAreas();
//
//                        } else if (chip.getId() == R.id.ingredientChip) {
//                            selectedChip = getString(R.string.ingredient);
//                            searchTitle.setText(selectedChip);
//                            searchPresenter.getAllIngredients();
//                        }
//                    }
//                }
//            }
//        });
//    }

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

//    public void performSearch(String query) {
//        if (query.isEmpty()) return;
//
//        switch (selectedChip) {
//            case "Category":
//                searchPresenter.getMealsByCategory(query);
//                Log.d("ss", "performSearch: " + query);
//                break;
//            case "Area":
//                searchPresenter.getMealsByArea(query);
//                break;
//            case "Ingredient":
//                searchPresenter.getMealsByIngredient(query);
//                break;
//            default:
//                searchPresenter.getAllMeals(query);
//                searchTitle.setText("Meals");
//
//                Log.d("TAG", "performSearch: No chip selected, normal search");
//                break;
//        }
//    }

    @Override
    public void showListOfMealByCategorty(List<Meal> meals) {
            updateMealList(meals);
        Log.d("cat", "showListOfMealByCategory: =>" + meals.size());

    }

    @Override
    public void showListOfMealByArea(List<Meal> meals) {
        updateMealList(meals);

        Log.d("cat", "showListOfMealByArea: =>" + meals.size());
    }

    @Override
    public void showListOfMealByIngredient(List<Meal> meals) {
        updateMealList(meals);

        Log.d("cat", "showListOfMealByIngredient: => " + meals.size());
    }

    @Override
    public void showAllAreas(List<Area> areas) {
        if (areas != null && !areas.isEmpty()) {
            filterAdapter.updateList(areas);
            searchRecyclerView.setAdapter(filterAdapter);
            filterAdapter.notifyDataSetChanged();

            // Switch to the meal recycler view
            searchRecyclerView.setVisibility(View.VISIBLE);
            mealsRecyclerView.setVisibility(View.GONE);
        } else {
            // Show a message if no categories are found
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

            // Switch to the meal recycler view
            searchRecyclerView.setVisibility(View.VISIBLE);
            mealsRecyclerView.setVisibility(View.GONE);
        } else {
            // Show a message if no categories are found
            Toast.makeText(requireContext(), "No categories found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showAllIngredients(List<Ingredient> ingredients) {

        if (ingredients != null && !ingredients.isEmpty()) {
            filterAdapter.updateList(ingredients);
            searchRecyclerView.setAdapter(filterAdapter);
            filterAdapter.notifyDataSetChanged();

            // Switch to the meal recycler view
            searchRecyclerView.setVisibility(View.VISIBLE);
            mealsRecyclerView.setVisibility(View.GONE);
        } else {
            // Show a message if no categories are found
            Toast.makeText(requireContext(), "No categories found.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void showMealList(List<Meal> meals) {
        // Ensure meals list is initialized
        if (meals == null) {
            meals = new ArrayList<>(); // Initialize to empty list if null
        }

        mealsAdapter.setList(meals);
        mealsRecyclerView.setAdapter(mealsAdapter);
        mealsAdapter.notifyDataSetChanged();

        // Show the meal list view
        mealsRecyclerView.setVisibility(View.VISIBLE);
        searchRecyclerView.setVisibility(View.GONE);
    }


    @Override
    public void onMealClick(Meal meal) {
        Toast.makeText(requireContext(), meal.getStrMeal(), Toast.LENGTH_SHORT).show();
    }
}
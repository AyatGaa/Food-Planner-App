package com.example.foodplanner.searchscreen.view;

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

public class SearchFragment extends Fragment implements SearchScreenView {

    SearchScreenPresenter searchPresenter;
    RecyclerView searchRecyclerView;
    ChipGroup searchChipGroup;
    FilterAdapter filterAdapter;
    SearchView searchView;

    public SearchFragment() {
        // Required empty public constructor
    }

    void setUi(View v) {
        searchRecyclerView = v.findViewById(R.id.searchRecyclerView);
        searchChipGroup = v.findViewById(R.id.searchChipGroup);
        searchView = v.findViewById(R.id.searchView);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setUi(view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        searchRecyclerView.setLayoutManager(gridLayoutManager);

        searchRecyclerView.setLayoutManager(gridLayoutManager);

        filterAdapter = new FilterAdapter(new ArrayList(), requireContext());
        searchRecyclerView.setAdapter(filterAdapter);

        searchPresenter = new SearchScreenPresenterImpl(new MealRepositoryImpl(
                MealRemoteDataSourceImpl.getInstance(),
                FavouriteMealLocalDataSourceImpl.getInstance(requireContext()),
                FilterRemoteDataSourceImpl.getInstance()
        ), this);

        searchChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {

            for (int id : checkedIds) {

                Chip chip = view.findViewById(id);

                if (chip != null) {

                    if (chip == view.findViewById(R.id.categoryChip)) {

                        Log.i("chip", "onCreateView: chip cat clicked" + chip.getText().toString());
                        searchPresenter.getAllCategories();
                    } else if (chip == view.findViewById(R.id.countryChip)) {
                        chip.setChipIconVisible(true);
                        searchPresenter.getAllAreas();
                    }else if (chip == view.findViewById(R.id.ingredientChip)) {

                        searchPresenter.getAllIngredients();
                    }
                }
            }


        });

        return view;
    }


    @Override
    public void showListOfMealByCategorty(List<Meal> meals) {

        Log.d("cat", "showListOfMealByCategory: =>" + meals.size());

    }

    @Override
    public void showListOfMealByArea(List<Meal> meals) {
        Log.d("cat", "showListOfMealByArea: =>" + meals.size());
    }

    @Override
    public void showListOfMealByIngredient(List<Meal> meals) {
        Log.d("cat", "showListOfMealByIngredient: => " + meals.size());
    }

    @Override
    public void showAllAreas(List<Area> areas) {

        filterAdapter.updateList(areas);
        searchRecyclerView.setAdapter(filterAdapter);
        filterAdapter.notifyDataSetChanged();
        Log.d("area", "showAllAreas:  = > " + areas.size());


    }

    @Override
    public void showAllCategories(List<Category> categories) {
        filterAdapter.updateList(categories);
        searchRecyclerView.setAdapter(filterAdapter);
        filterAdapter.notifyDataSetChanged();

    }

    @Override
    public void showAllIngredients(List<Ingredient> ingredients) {

        filterAdapter.updateList(ingredients);
        searchRecyclerView.setAdapter(filterAdapter);
        filterAdapter.notifyDataSetChanged();

    }

}
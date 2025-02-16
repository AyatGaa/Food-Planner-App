package com.example.foodplanner.searchscreen.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.modelrepoitory.MealRepositoryImpl;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSourceImpl;
import com.example.foodplanner.network.FilterRemoteDataSourceImpl;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;
import com.example.foodplanner.searchscreen.presenter.SearchScreenPresenter;
import com.example.foodplanner.searchscreen.presenter.SearchScreenPresenterImpl;

import java.util.List;

public class SearchFragment extends Fragment implements SearchScreenView{

    SearchScreenPresenter searchPresenter;
TextView test;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        test = view.findViewById(R.id.test);
        searchPresenter = new SearchScreenPresenterImpl(new MealRepositoryImpl(
                MealRemoteDataSourceImpl.getInstance(),
                FavouriteMealLocalDataSourceImpl.getInstance(requireContext()),
                FilterRemoteDataSourceImpl.getInstance()
        ), this);

        searchPresenter.filterByCategory("Seafood");
        searchPresenter.filterByArea("Canadian");
        searchPresenter.filterByIngredient("chicken_breast");

        return view;
    }


    @Override
    public void showListOfMealByCategorty(List<Meal> meals) {
            test.setText(meals.get(0).getStrMeal());
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
}
package com.example.foodplanner.favortitescreen.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.Repository.modelrepoitory.MealRepositoryImpl;
import com.example.foodplanner.backup.BackupMealFirebaseImpl;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSourceImpl;
import com.example.foodplanner.favortitescreen.presenter.FavoriteScreenPresenter;
import com.example.foodplanner.favortitescreen.presenter.FavoriteScreenPresenterImpl;
import com.example.foodplanner.network.datasources.FilterRemoteDataSourceImpl;
import com.example.foodplanner.network.datasources.MealRemoteDataSourceImpl;
import com.example.foodplanner.utils.AppFunctions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment implements FavoriteScreenView, OnDeleteMealClickListener {
    RecyclerView favoriteRecyclerView;
    FavoriteAdapter favAdapter;
    LinearLayout signInLayout;
    Button signInButton;
    FavoriteScreenPresenter favoriteScreenPresenter;

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MealRepository repo = MealRepositoryImpl.getInstance(
                MealRemoteDataSourceImpl.getInstance(),
                FavouriteMealLocalDataSourceImpl.getInstance(getContext()),
                FilterRemoteDataSourceImpl.getInstance(),
                BackupMealFirebaseImpl.getInstance()
        );
        favoriteScreenPresenter = new FavoriteScreenPresenterImpl(this, repo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        signInLayout = view.findViewById(R.id.signInLayout);
        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView);
        signInButton = view.findViewById(R.id.signInButton);

        boolean userStatus = AppFunctions.isAuthenticated();
        if (!userStatus) {

            signInLayout.setVisibility(View.VISIBLE);
            favoriteRecyclerView.setVisibility(View.GONE);
        } else {
            signInLayout.setVisibility(View.GONE);

            favoriteRecyclerView.setVisibility(View.VISIBLE);
            favoriteRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            favoriteRecyclerView.setLayoutManager(layoutManager);

            favAdapter = new FavoriteAdapter(getContext(), new ArrayList<>(), this);
            favoriteRecyclerView.setAdapter(favAdapter);

            favoriteScreenPresenter.checkInternetConnection(requireContext());
            favoriteScreenPresenter.getFavoriteMeals();
            favoriteScreenPresenter.getFavouriteMealsFromFirebase(AppFunctions.getCurrentUserId());
        }

        signInButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_favouriteFragment_to_signInFragment);
        });

        return view;
    }

    @Override
    public void showFavoriteMeals(List<Meal> meals) {
        if (favAdapter != null) {
            favAdapter.updateData(meals);
        } else {
            favAdapter = new FavoriteAdapter(getContext(), meals, this);
            favoriteRecyclerView.setAdapter(favAdapter);
        }

    }

    @Override
    public void showSnackBar(Meal meal, String message) {
        View rootView = getActivity().findViewById(R.id.bottomNavigationView);
        Snackbar snackbar = Snackbar.make(favoriteRecyclerView, message, Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(rootView).setAction("Undo", v -> {
            favoriteScreenPresenter.addMealToFavorite(meal); // undo
            favAdapter.notifyDataSetChanged();
        });
        snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"))
                .setActionTextColor(Color.parseColor("#C21010"))
                .setTextColor(Color.parseColor("#C21010"));
         snackbar.show();

    }

    @Override
    public void onDeleteMealClick(Meal meal) {
        favoriteScreenPresenter.deleteMealFromFavorite(meal);
    }
}
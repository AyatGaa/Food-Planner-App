package com.example.foodplanner.favortitescreen.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.Repository.modelrepoitory.MealRepositoryImpl;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSourceImpl;
import com.example.foodplanner.favortitescreen.presenter.FavoriteScreenPresenter;
import com.example.foodplanner.favortitescreen.presenter.FavoriteScreenPresenterImpl;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;

import java.util.List;

public class FavouriteFragment extends Fragment implements FavoriteScreenView{
TextView textView;

    FavoriteScreenPresenter favoriteScreenPresenter;

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MealRepository repo = MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), FavouriteMealLocalDataSourceImpl.getInstance(getContext()));
        favoriteScreenPresenter = new FavoriteScreenPresenterImpl(this,repo);
        favoriteScreenPresenter.addMealToFavorite(new Meal("id","name","image","qq","55","66"));
        favoriteScreenPresenter.addMealToFavorite(new Meal("id1","name2","image2","ww","22","kk"));
        favoriteScreenPresenter.addMealToFavorite(new Meal("id2","name3","image3","sss","21","33"));

        favoriteScreenPresenter.getFavoriteMeals();
        favoriteScreenPresenter.deleteMealFromFavorite(new Meal("id2","name3","image3","sss","21","33"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void showFavoriteMeals(List<Meal> meals) {
        textView = getView().findViewById(R.id.textView);
        textView.setText(meals.get(2).getStrMeal());
        Log.d("TAG", "showFavoriteMeals: form fav frag" + meals.size() + meals.get(0).getStrMeal());
    }
}
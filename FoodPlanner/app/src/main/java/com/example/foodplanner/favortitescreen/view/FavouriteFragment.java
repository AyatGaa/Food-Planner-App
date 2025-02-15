package com.example.foodplanner.favortitescreen.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.modelrepoitory.MealRepository;
import com.example.foodplanner.Repository.modelrepoitory.MealRepositoryImpl;
import com.example.foodplanner.database.favouritemeal.FavouriteMealLocalDataSourceImpl;
import com.example.foodplanner.favortitescreen.presenter.FavoriteScreenPresenter;
import com.example.foodplanner.favortitescreen.presenter.FavoriteScreenPresenterImpl;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;
import com.example.foodplanner.utils.AppFunctions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment implements FavoriteScreenView ,OnDeleteMealClickListener {
    RecyclerView favoriteRecyclerView;;
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

        MealRepository repo = MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), FavouriteMealLocalDataSourceImpl.getInstance(getContext()));
        favoriteScreenPresenter = new FavoriteScreenPresenterImpl(this, repo);
//        favoriteScreenPresenter.addMealToFavorite(new Meal("id","name","image","qq","55","66"));
//        favoriteScreenPresenter.addMealToFavorite(new Meal("id1","name2","image2","ww","22","kk"));
//        favoriteScreenPresenter.addMealToFavorite(new Meal("id2","name3","image3","sss","21","33"));
//

//        favoriteScreenPresenter.deleteMealFromFavorite(new Meal("id2","name3","image3","sss","21","33"));
//


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        signInLayout = view.findViewById(R.id.signInLayout);
        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView);
        signInButton = view.findViewById(R.id.signInButton);
        boolean userStatus = AppFunctions.isAuthenticated();
        if(!userStatus){//

            signInLayout.setVisibility(View.VISIBLE);
            favoriteRecyclerView.setVisibility(View.GONE);
        }else {

            signInLayout.setVisibility(View.GONE);
            favoriteRecyclerView.setVisibility(View.VISIBLE);


            favoriteRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            favoriteRecyclerView.setLayoutManager(layoutManager);
            favAdapter = new FavoriteAdapter(getContext(), new ArrayList<>(),this);
            favoriteRecyclerView.setAdapter(favAdapter);

            favoriteScreenPresenter.getFavoriteMeals();
        }

        signInButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_favouriteFragment_to_signInFragment);
        });

        return view;
    }

    @Override
    public void showFavoriteMeals(List<Meal> meals) {
        Log.d("fav", "Updating RecyclerView with " + meals.size() + " meals"); // Debugging
        if (favAdapter != null) {
            favAdapter.updateData(meals);
        } else {
            Log.d("fav", "Adapter is null! Initializing again...");
            favAdapter = new FavoriteAdapter(getContext(), meals,this);
            favoriteRecyclerView.setAdapter(favAdapter);
        }

//        Log.d("FavoriteDebug", "Updating RecyclerView with " + meals.size() + " meals"); // Debug
//        favAdapter.updateData(meals);
//
//       favAdapter = new FavoriteAdapter(getContext(), meals ,this);
//        favoriteRecyclerView.setAdapter(favAdapter);
//        favAdapter.notifyDataSetChanged();
//        Log.d("TAG", "Favorite meals count: " + meals.size());
    }

    @Override
    public void showSnackBar(Meal meal) {
        View rootView = getActivity().findViewById(R.id.bottomNavigationView);
        Snackbar snackbar = Snackbar.make(favoriteRecyclerView, "Meal deleted", Snackbar.LENGTH_LONG);
            snackbar.setAnchorView(rootView).setAction("UNDO", v -> {
                favoriteScreenPresenter.addMealToFavorite(meal); // Restore meal
            });
            snackbar.show();

    }

    @Override
    public void onDeleteMealClick(Meal meal) {
        favoriteScreenPresenter.deleteMealFromFavorite(meal);
    }
}
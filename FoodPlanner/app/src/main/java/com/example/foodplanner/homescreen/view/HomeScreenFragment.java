package com.example.foodplanner.homescreen.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.meals.MealRepository;
import com.example.foodplanner.Models.meals.MealRepositoryImpl;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.AuthRepositoryImpl;
import com.example.foodplanner.homescreen.presenter.HomeScreenPresenter;
import com.example.foodplanner.homescreen.presenter.HomeScreenPresenterImpl;
import com.example.foodplanner.network.MealRemoteDataSourceImpl;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenFragment extends Fragment implements HomeScreenView {

    ImageView mealOfTheDayImage;
    TextView mealOfTheDayTitle, mealOfTheDayInstructions;
    RecyclerView homeRecyclerView;
    HomeScreenPresenter homeScreenPresenter;

    HomeScreenAdapter homeScreenAdapter;

    List<Meal> meals = new ArrayList<>();


    void setupUI(View view){
        mealOfTheDayImage = view.findViewById(R.id.mealOfTheDayImage);
        mealOfTheDayTitle = view.findViewById(R.id.mealOfTheDayTitle);
        mealOfTheDayInstructions = view.findViewById(R.id.mealOfTheDayInstructions);
        homeRecyclerView = view.findViewById(R.id.cozyMealRecyclerView);
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

        homeRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        homeRecyclerView.setLayoutManager(layoutManager);

        MealRepository mealRepo = MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance());

        homeScreenPresenter = new HomeScreenPresenterImpl(this, mealRepo);
        homeScreenAdapter = new HomeScreenAdapter(meals, getContext());
        Log.i("TAG", "onCreateView: here"+ meals.size());
        homeRecyclerView.setAdapter(homeScreenAdapter);

         homeScreenPresenter.getMeals();
        homeScreenPresenter.getRandomMeal();

//        btnLogOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("TAG", "onClick: logiut");
//                authRepository.signOut();
//            }
//        });

        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void setRandmoMealCard(Meal meal){
        Glide.with(getContext()).load(meal.getStrMealThumb())
                .error(R.drawable.ic_launcher_background)
                .into(mealOfTheDayImage);
        mealOfTheDayTitle.setText(meal.getStrMeal());
        mealOfTheDayInstructions.setText(meal.getStrInstructions());
    }
    @Override
    public void showMeals(List<Meal> meals) {
        homeScreenAdapter = new HomeScreenAdapter(meals, getContext());
        homeRecyclerView.setAdapter(homeScreenAdapter);
        homeScreenAdapter.setList(meals);
        homeScreenAdapter.notifyDataSetChanged();


         //   homeScreenPresenter.getMeals();
    }
}
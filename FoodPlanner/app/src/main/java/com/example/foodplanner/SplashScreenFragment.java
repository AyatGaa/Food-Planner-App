package com.example.foodplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashScreenFragment extends Fragment {

    ImageView logo;
    TextView planYourMeals;
    Animation logoAnimation;
    Animation texAnimation;

    public SplashScreenFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        logo = view.findViewById(R.id.logo_intro);
        planYourMeals = view.findViewById(R.id.text_intro);
        logoAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.logo_intro);
        texAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.text_intro);
        logo.startAnimation(logoAnimation);
        planYourMeals.startAnimation(texAnimation);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 3000);
    }
}
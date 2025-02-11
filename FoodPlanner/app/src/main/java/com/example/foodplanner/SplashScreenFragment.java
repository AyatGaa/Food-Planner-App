package com.example.foodplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplanner.utils.AppFunctions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashScreenFragment extends Fragment {

    ImageView logo;
    TextView planYourMeals;
    Animation logoAnimation, texAnimation;
    private FirebaseAuth mAuth;

    public SplashScreenFragment() {

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
        mAuth = FirebaseAuth.getInstance();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("TAG", "run: herere");
                Toast.makeText(getContext(), "end of 3 sec", Toast.LENGTH_SHORT).show();
              //   Navigation.findNavController(view).navigate(R.id.action_splashScreenFragment_to_introScreenFragment);
                checkFirstTime();
            }
        }, 3000);

    }
    private void checkFirstTime(){

        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean("isFirstTime", true);
            if(isFirstTime){
                AppFunctions.navigateTo(getView(), R.id.action_splashScreenFragment_to_introScreenFragment);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isFirstTime", false);
                editor.apply();
            }else{
                checkUserStatus();

            }
    }
    private void checkUserStatus(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Navigation.findNavController(getView()).navigate(R.id.action_splashScreenFragment_to_homeScreenFragment);

        }else{
            Navigation.findNavController(getView()).navigate(R.id.action_splashScreenFragment_to_signInFragment);
        }
    }
}
package com.example.foodplanner.splashscreen.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.example.foodplanner.utils.AppFunctions;
import com.example.foodplanner.utils.BottomSheetFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;


public class SplashScreenFragment extends Fragment implements SplashScreenView {

    ImageView logo;
    TextView planYourMeals;
    Animation logoAnimation, texAnimation;
    private FirebaseAuth mAuth;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        compositeDisposable.add(
                Completable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    checkFirstTime();
                                    Log.i("TAG", "run: herere");
                                    Toast.makeText(getContext(), "end of 3 sec from obs", Toast.LENGTH_SHORT).show();
                                })
        );


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void checkFirstTime() {

        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean("isFirstTime", true);
        if (isFirstTime) {
            Navigation.findNavController(requireView()).navigate(R.id.action_splashScreenFragment_to_introScreenFragment ,null,
                    new NavOptions.Builder()
                            .setPopUpTo(R.id.splashScreenFragment, true)
                            .build());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
        } else {
            checkUserStatus();
        }
    }

    private void checkUserStatus() {
        if (getView() == null) return;

        if (!AppFunctions.isConnected(requireContext())) {

            Navigation.findNavController(requireView()).navigate(R.id.action_splashScreenFragment_to_homeScreenFragment);
            //   showOnNoConnection();
            //  Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Navigation.findNavController(requireView()).navigate(R.id.action_splashScreenFragment_to_homeScreenFragment , null,
                    new NavOptions.Builder()
                            .setPopUpTo(R.id.splashScreenFragment, true) //handle back btn
                            .build());

        } else {
            Navigation.findNavController(requireView()).navigate(R.id.action_splashScreenFragment_to_signInFragment ,null,
                    new NavOptions.Builder()
                            .setPopUpTo(R.id.splashScreenFragment, true)
                            .build());
        }
    }


    @Override
    public void showOnNoConnection() {
        if (!AppFunctions.isConnected(requireContext())) {
            BottomSheetFragment bottomSheet = new BottomSheetFragment();
            bottomSheet.show(getParentFragmentManager(), "NoConnectionBottomSheet");
        }
    }
}

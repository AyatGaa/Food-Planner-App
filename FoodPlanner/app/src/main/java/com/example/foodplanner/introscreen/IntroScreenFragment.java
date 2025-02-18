package com.example.foodplanner.introscreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodplanner.R;

public class IntroScreenFragment extends Fragment {

    Button btnGetStarted;
    Button btnSignup;
    TextView txtSignIn;

    public IntroScreenFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_screen, container, false);
        btnSignup = view.findViewById(R.id.btnSignUpIntro);
        btnGetStarted = view.findViewById(R.id.btnGetStarted);
        txtSignIn = view.findViewById(R.id.txtLoginIntro);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_introScreenFragment_to_signUpFragment);

            }
        });
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_introScreenFragment_to_homeScreenFragment);

            }
        });
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_introScreenFragment_to_signInFragment);

            }
        });
        return view;
    }
}
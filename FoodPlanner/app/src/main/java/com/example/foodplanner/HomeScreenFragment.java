package com.example.foodplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.foodplanner.Repository.AuthRepository;
import com.example.foodplanner.Repository.AuthRepositoryImpl;

public class HomeScreenFragment extends Fragment {

    Button btnLogOut;

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
    View view  = inflater.inflate(R.layout.fragment_home_screen, container, false);
    AuthRepositoryImpl authRepository = AuthRepositoryImpl.getInstance();
        btnLogOut = view.findViewById(R.id.btnLogOut);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onClick: logiut");
                authRepository.signOut();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
package com.example.foodplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodplanner.Repository.AuthRepository;
import com.example.foodplanner.Repository.AuthRepositoryImpl;


public class UserFragment extends Fragment {

    TextView userName;
    Button signOut;
    AuthRepository authRepository;
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
    signOut = view.findViewById(R.id.btnSignOut);
   userName = view.findViewById(R.id.txtUserNameUserScreen);

   authRepository = AuthRepositoryImpl.getInstance();
    userName.setText(" " + authRepository.getCurrentUserName());
    signOut.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("TAG", "signOut: from auth repo");
            authRepository.signOut();
            Navigation.findNavController(view).navigate(R.id.action_userFragment_to_homeScreenFragment);
        }
    });
        return view;
    }
}
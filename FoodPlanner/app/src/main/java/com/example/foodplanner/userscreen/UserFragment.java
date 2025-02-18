package com.example.foodplanner.userscreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodplanner.R;
import com.example.foodplanner.Repository.authrepository.AuthRepository;
import com.example.foodplanner.Repository.authrepository.AuthRepositoryImpl;
import com.example.foodplanner.utils.AppFunctions;


public class UserFragment extends Fragment {

    TextView userName, hi;
    Button signOut, signIn;
    AuthRepository authRepository;
    LinearLayout signInLayoutPlan;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void setUI(View view) {
        signOut = view.findViewById(R.id.btnSignOut);
        signIn = view.findViewById(R.id.signInButtonUser);
        hi = view.findViewById(R.id.txtHi);
        userName = view.findViewById(R.id.txtUserNameUserScreen);
        signInLayoutPlan = view.findViewById(R.id.signInLayoutUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        setUI(view);
        boolean userStatus = AppFunctions.isAuthenticated();
        if (!userStatus) {// is not auth=> need to sign in
            signInLayoutPlan.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.GONE);
            userName.setVisibility(View.GONE);
            hi.setVisibility(View.GONE);

            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(view).navigate(R.id.action_userFragment_to_signInFragment);
                }
            });

        } else { // is auth then go through app ..

            signInLayoutPlan.setVisibility(View.GONE);
            signOut.setVisibility(View.VISIBLE);
            userName.setVisibility(View.VISIBLE);
            hi.setVisibility(View.VISIBLE);


            authRepository = AuthRepositoryImpl.getInstance();
            userName.setText(("    " + getString(R.string.foodi_user)));
            signOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    authRepository.signOut();
                    Navigation.findNavController(view).navigate(R.id.action_userFragment_to_introScreenFragment); //go to intro screen
                }

            });
        }
        return view;
    }
}
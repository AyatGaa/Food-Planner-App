package com.example.foodplanner.signin.view;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.example.foodplanner.signin.presenter.SignInPresenter;
import com.example.foodplanner.signin.presenter.SignInPresenterImpl;
import com.example.foodplanner.utils.AppFunctions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInFragment extends Fragment implements SignInView {

    private static final String TAG = "signin";
    private static final int RC_SIGN_IN = 9001;
    EditText edtEmailSignIn;
    EditText editPasswordSignIn;
    TextView txtSignUpIn;
    Button btnSignIn;
    Button btnGoogleSignIn;

    SignInPresenter signInPresenter;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    public SignInFragment() {
        Log.d(TAG, "SignInFragment: dasdas");
    }

    void setupUI(View view) {
        editPasswordSignIn = view.findViewById(R.id.edtPasswordSignIn);
        edtEmailSignIn = view.findViewById(R.id.edtEmailSignIn);
        btnSignIn = view.findViewById(R.id.btnSignIn);
        txtSignUpIn = view.findViewById(R.id.txtSignUpIn);
        btnGoogleSignIn = view.findViewById(R.id.btnSignInWithGoogle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        setupUI(view);
        signInPresenter = new SignInPresenterImpl(this,getContext());

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInPresenter.onSignInClicked(
                        edtEmailSignIn.getText().toString(),
                        editPasswordSignIn.getText().toString());

            }
        });
        txtSignUpIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment2);
            }
        });
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             signInPresenter.signInWithGoogle();
            }
        });
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        signInPresenter.handleGoogleSignInResult(data);
                    } else {
                        showErrorSignIn("Google Sign-In failed: Result Code " + result.getResultCode());
                    }
                }
        );

        return view;
    }

    @Override
    public void showSuccessSignIn() {
        Toast.makeText(getActivity(), "Login Successful!", Toast.LENGTH_SHORT).show();
        //Navigation.findNavController().navigate(R.id.action_signInFragment_to_homeScreenFragment);
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isFirstTime", false);
        editor.apply();
        AppFunctions.navigateTo(getView(), R.id.action_signInFragment_to_homeScreenFragment);

    }

    @Override
    public void showErrorSignIn(String errMessage) {

        Toast.makeText(getActivity(), errMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void startGoogleSignInIntent(Intent signInIntent) {
        if (signInIntent != null) {
            Log.d("GoogleSignIn", "Launching Google Sign-In Intent");
            googleSignInLauncher.launch(signInIntent);
        } else {
            Log.e("GoogleSignIn", "Google Sign-In Intent is null");
        }
    }



}
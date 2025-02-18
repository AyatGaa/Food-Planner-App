package com.example.foodplanner.signin.view;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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

    private static final String TAG = "TAG";
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    EditText edtEmailSignIn, editPasswordSignIn;
    TextView txtSignUpIn;
    Button btnSignIn, btnGoogleSignIn,btnSkipSignIn;
    private ProgressBar progressBarSignIn;
    LottieAnimationView loadingViewSignIn;

    SignInPresenter signInPresenter;


    public SignInFragment() {
        Log.d(TAG, "SignInFragment: dasdas");
    }

    void setupUI(View view) {
        editPasswordSignIn = view.findViewById(R.id.edtPasswordSignIn);
        edtEmailSignIn = view.findViewById(R.id.edtEmailSignIn);
        btnSignIn = view.findViewById(R.id.btnSignIn);
        txtSignUpIn = view.findViewById(R.id.txtSignUpIn);
        btnGoogleSignIn = view.findViewById(R.id.btnSignInWithGoogle);
        loadingViewSignIn = view.findViewById(R.id.loadingViewSignIn);
        btnSkipSignIn = view.findViewById(R.id.btnSkipSignIn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        setupUI(view);
        signInPresenter = new SignInPresenterImpl(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);


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
                signInWithGoogle();
            }
        });

        btnSkipSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_homeScreenFragment);
            }
        });
        return view;
    }

    @Override
    public void showSuccessSignIn() {
        Toast.makeText(getActivity(), "Login Successful!", Toast.LENGTH_SHORT).show();

        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isFirstTime", false);
        editor.apply();
        AppFunctions.navigateTo(getView(), R.id.action_signInFragment_to_homeScreenFragment);

    }

    @Override
    public void showErrorSignIn(String errMessage) {
        Log.i(TAG, "showErrorSignIn:GOOGLE in Fragment ");
        Toast.makeText(getActivity(), errMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        loadingViewSignIn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingViewSignIn.setVisibility(View.VISIBLE);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                signInPresenter.signInWithGoogle(account);
            } catch (ApiException e) {
                showErrorSignIn("Google sign-in failed: " + e.getMessage());
            }
        }
    }


}

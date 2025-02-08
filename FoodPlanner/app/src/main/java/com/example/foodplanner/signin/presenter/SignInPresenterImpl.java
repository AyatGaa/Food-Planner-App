package com.example.foodplanner.signin.presenter;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.foodplanner.R;
import com.example.foodplanner.Repository.AuthCallback;
import com.example.foodplanner.Repository.AuthRepository;
import com.example.foodplanner.Repository.AuthRepositoryImpl;
import com.example.foodplanner.signin.view.SignInView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class SignInPresenterImpl implements SignInPresenter {

    AuthRepository authRepository;
    SignInView signInView;
    private GoogleSignInClient mGoogleSignInClient;
    private Context context;
    public SignInPresenterImpl(SignInView signInView, Context context) {
        this.authRepository = AuthRepositoryImpl.getInstance();
        this.signInView = signInView;
        this.context =context;

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions
                .DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client))
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(context,gso);
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, gso);
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            Log.d("GoogleSignOut", "Signed out before new login attempt");
        });
    }

    @Override
    public void onSignInClicked(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            signInView.showErrorSignIn("please fill all fields");
            return;
        }
        authRepository.signIn(email, password, new AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                signInView.showSuccessSignIn();
            }

            @Override
            public void onFailure(String errorMessage) {
                signInView.showErrorSignIn(errorMessage);
            }
        });
    }
    @Override
    public void handleGoogleSignInResult( Intent data) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    Log.d("GoogleSignIn", "Google sign-in successful, ID Token: " + account.getIdToken());

                    authRepository.signInWithGoogle(account.getIdToken(), new AuthCallback() {
                        @Override
                        public void onSuccess(FirebaseUser user) {
                            Log.d("GoogleSignIn", "Firebase Authentication Successful");
                            signInView.showSuccessSignIn();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.e("GoogleSignIn", "Firebase Authentication Failed: " + errorMessage);
                            signInView.showErrorSignIn(errorMessage);
                        }
                    });
                } else {
                    Log.e("GoogleSignIn", "GoogleSignInAccount is null");
                }
            } catch (ApiException e) {
                int statusCode = e.getStatusCode();
                Log.e("GoogleSignIn", "Google sign-in failed: " + statusCode, e);
                signInView.showErrorSignIn("Google sign in failed: " + statusCode);
            }
    }
    public void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInView.startGoogleSignInIntent(signInIntent);
    }
}

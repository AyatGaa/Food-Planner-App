package com.example.foodplanner.Repository;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.foodplanner.R;
import com.example.foodplanner.signin.view.SignInView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthRepositoryImpl implements AuthRepository {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "signin";
    private FirebaseAuth mAuth;
    private static AuthRepositoryImpl authRepository = null;
    private GoogleSignInClient mGoogleSignInClient;
    Context context;


    public static AuthRepositoryImpl getInstance() {
        if (authRepository == null) {
            authRepository = new AuthRepositoryImpl();
        }
        return authRepository;
    }
    private AuthRepositoryImpl() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signIn(String email, String password, AuthCallback authCallback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "onComplete: signed in  successfully");
                            authCallback.onSuccess(task.getResult().getUser()); // getcurrentuser
                        } else {
                            Log.i(TAG, "onComplete: signed in NOT DONE");
                            authCallback.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }

    @Override
    public void signUp(String email, String password, AuthCallback callback) {

    }

    @Override
    public void signInWithGoogle(String idToken, AuthCallback authCallback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithCredential:success");
                    authCallback.onSuccess(mAuth.getCurrentUser());
                } else {
                    authCallback.onFailure(task.getException().getMessage());
                    Log.w(TAG, "signInWithCredential:failure", task.getException());

                }
            }
        });

    }

    public void signOut() {
        mAuth.signOut();
    }


}

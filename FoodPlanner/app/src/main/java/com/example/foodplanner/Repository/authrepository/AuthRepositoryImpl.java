package com.example.foodplanner.Repository.authrepository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthRepositoryImpl implements AuthRepository {

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
    public String getCurrentUserName() {
        String userName = mAuth.getCurrentUser().getDisplayName();
        return userName;
    }

    @Override
    public void signUp(String email, String password, AuthCallback authCallback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("TAG", "onComplete: Done sign up in auth ");
                            authCallback.onSuccess(task.getResult().getUser());
                        } else {
                            Log.i("TAG", "onComplete:WRONG  in auth imple class");
                            authCallback.onFailure(task.getException().getMessage());

                        }
                    }
                });
    }

    @Override
    public void signInWithGoogle(GoogleSignInAccount account, AuthCallback authCallback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithCredential:success");
                    authCallback.onSuccess(task.getResult().getUser());
                } else {
                    authCallback.onFailure(task.getException().getMessage());
                    Log.w(TAG, "signInWithCredential:failure", task.getException());

                }
            }
        });

    }

    public void signOut() {
        Log.d(TAG, "signOut: from auth repo");
        mAuth.signOut();
    }


}

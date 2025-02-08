package com.example.foodplanner.signin.view;

import android.content.Intent;

public interface SignInView { // what i need to use to update UI
    void showSuccessSignIn();
    void showErrorSignIn(String errMessage);
    void startGoogleSignInIntent(Intent signInIntent);
}

package com.example.foodplanner.signin.presenter;

import android.content.Intent;

public interface SignInPresenter {

   void onSignInClicked(String email, String password);
   void handleGoogleSignInResult(Intent data);
   void signInWithGoogle();

}

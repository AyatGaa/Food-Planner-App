package com.example.foodplanner.signin.presenter;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface SignInPresenter {

   void onSignInClicked(String email, String password);
   void signInWithGoogle(GoogleSignInAccount account);

}

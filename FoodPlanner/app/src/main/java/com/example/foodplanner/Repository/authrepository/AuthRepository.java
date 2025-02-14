package com.example.foodplanner.Repository.authrepository;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface AuthRepository {

    void signIn(String email, String password, AuthCallback callback);
    void signUp(String email, String password, AuthCallback callback);
    void signInWithGoogle(GoogleSignInAccount account, AuthCallback callback);
    void signOut();
    String getCurrentUserName();

}

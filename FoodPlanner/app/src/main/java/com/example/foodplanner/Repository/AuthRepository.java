package com.example.foodplanner.Repository;

import com.google.firebase.auth.FirebaseUser;

public interface AuthRepository {

    void signIn(String email, String password, AuthCallback callback);
    void signUp(String email, String password, AuthCallback callback);
    void signInWithGoogle(String idToken, AuthCallback callback);
    void signOut();

}

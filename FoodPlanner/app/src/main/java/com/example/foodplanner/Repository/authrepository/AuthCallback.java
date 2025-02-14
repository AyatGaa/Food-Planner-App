package com.example.foodplanner.Repository.authrepository;

import com.google.firebase.auth.FirebaseUser;

public interface AuthCallback {
    void onSuccess(FirebaseUser user);
    void onFailure(String errorMessage);
}

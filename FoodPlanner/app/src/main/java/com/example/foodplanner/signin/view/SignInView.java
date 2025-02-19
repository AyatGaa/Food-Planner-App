package com.example.foodplanner.signin.view;


public interface SignInView { // what i need to use to update UI
    void showSuccessSignIn();

    void showErrorSignIn(String errMessage);

    void showLoading();

    void hideLoading();
}

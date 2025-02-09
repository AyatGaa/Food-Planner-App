package com.example.foodplanner.signin.presenter;

import com.example.foodplanner.Repository.AuthCallback;
import com.example.foodplanner.Repository.AuthRepository;
import com.example.foodplanner.Repository.AuthRepositoryImpl;
import com.example.foodplanner.signin.view.SignInView;
import com.google.firebase.auth.FirebaseUser;

public class SignInPresenterImpl implements SignInPresenter {

    AuthRepository authRepository;
    SignInView signInView;

    public SignInPresenterImpl(SignInView signInView) {
        this.authRepository = AuthRepositoryImpl.getInstance();
        this.signInView = signInView;
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


}

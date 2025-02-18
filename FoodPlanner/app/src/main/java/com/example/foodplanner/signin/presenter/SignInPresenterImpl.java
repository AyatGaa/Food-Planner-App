package com.example.foodplanner.signin.presenter;

import com.example.foodplanner.Repository.authrepository.AuthCallback;
import com.example.foodplanner.Repository.authrepository.AuthRepository;
import com.example.foodplanner.Repository.authrepository.AuthRepositoryImpl;
import com.example.foodplanner.signin.view.SignInView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

    @Override
    public void signInWithGoogle(GoogleSignInAccount account) {
        signInView.showLoading();
        authRepository.signInWithGoogle(account, new AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                signInView.hideLoading();
                signInView.showSuccessSignIn();
            }

            @Override
            public void onFailure(String message) {
                signInView.hideLoading();
                signInView.showErrorSignIn(message);
            }
        });
    }

}

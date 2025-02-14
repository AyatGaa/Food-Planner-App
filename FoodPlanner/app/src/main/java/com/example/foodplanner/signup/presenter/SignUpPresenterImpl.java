package com.example.foodplanner.signup.presenter;

import android.util.Log;

import com.example.foodplanner.Repository.authrepository.AuthCallback;
import com.example.foodplanner.Repository.authrepository.AuthRepository;
import com.example.foodplanner.Repository.authrepository.AuthRepositoryImpl;
import com.example.foodplanner.signup.view.SignUpView;
import com.google.firebase.auth.FirebaseUser;

public class SignUpPresenterImpl implements SignUpPresenter {

    AuthRepository authRepository;
    SignUpView signUpView;

    public SignUpPresenterImpl( SignUpView signUpView) {
        this.authRepository = AuthRepositoryImpl.getInstance();
        this.signUpView = signUpView;
    }

    @Override
    public void signUpClicked(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            signUpView.showErrorSignUp("please fill all fields");
            return;
        }
        authRepository.signUp(email, password, new AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                signUpView.showSuccessSignUp();
                Log.i("TAG", "onSuccess: in signup clicked");
            }

            @Override
            public void onFailure(String errorMessage) {
                signUpView.showErrorSignUp(errorMessage);
                Log.i("TAG", "onFailure: in signup clicked");
            }
        });

    }
}

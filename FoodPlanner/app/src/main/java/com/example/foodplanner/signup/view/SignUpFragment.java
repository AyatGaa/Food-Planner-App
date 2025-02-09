package com.example.foodplanner.signup.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.example.foodplanner.signup.presenter.SignUpPresenter;
import com.example.foodplanner.signup.presenter.SignUpPresenterImpl;

public class SignUpFragment extends Fragment implements SignUpView {

    EditText edtEmailSignUp, edtPasswordSignUp, edtUsernameSignUp;
    Button btnSignUp;
    SignUpPresenter signUpPresenter;
    TextView txtSignin;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        setSignUpUi(view);
        signUpPresenter = new SignUpPresenterImpl(this);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onClick: in fragment");
                signUpPresenter.signUpClicked(
                        edtEmailSignUp.getText().toString(),
                        edtPasswordSignUp.getText().toString());

            }
        });

        txtSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_signInFragment);
            }
        });
        return view;
    }


    void setSignUpUi(View view) {
        edtUsernameSignUp = view.findViewById(R.id.edtUsernameSignUp);
        edtEmailSignUp = view.findViewById(R.id.edtEmailSignUp);
        edtPasswordSignUp = view.findViewById(R.id.edtPasswordSignUp);
        txtSignin = view.findViewById(R.id.txtSignIn);
        btnSignUp = view.findViewById(R.id.btnSignUp);
    }

    @Override
    public void showSuccessSignUp() {
        Toast.makeText(this.getContext(), "signed up successfully ", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(this.getView()).navigate(R.id.action_signUpFragment_to_homeScreenFragment);
    }

    @Override
    public void showErrorSignUp(String errMessage) {

        Toast.makeText(this.getContext(), "signed up Went Wrong ", Toast.LENGTH_SHORT).show();
    }
}
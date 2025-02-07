package com.example.foodplanner;

import static android.widget.Toast.makeText;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpFragment extends Fragment {

    EditText edtEmailSignUp;
    EditText edtPasswordSignUp;
    EditText edtUsernameSignUp;
    Button btnSignUp;
    FirebaseAuth mAuth;
    TextView txtSignin;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        edtUsernameSignUp = view.findViewById(R.id.edtUsernameSignUp);
        edtEmailSignUp = view.findViewById(R.id.edtEmailSignUp);
        edtPasswordSignUp = view.findViewById(R.id.edtPasswordSignUp);
            txtSignin = view.findViewById(R.id.txtSignIn);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("TAG", "onClick: llll");
                Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_homeScreenFragment);

//                String email = edtEmailSignUp.getText().toString().trim();
//                String password = edtPasswordSignUp.getText().toString().trim();
//                signUp(email, password);
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

    private void signUp(String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                     Log.i("TAG", "onComplete: Done llog up ");
                }else{

                     Log.i("TAG", "onComplete:WRONG ");

                }
            }
        });
    }

}
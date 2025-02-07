package com.example.foodplanner;


import android.content.Intent;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInFragment extends Fragment {

    private static final String TAG = "signin";
    private static final int RC_SIGN_IN = 9001;
    EditText edtEmailSignIn;
    EditText editPasswordSignIn;
    TextView txtSignUpIn;
    Button btnSignIn;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    public SignInFragment() {
        Log.d(TAG, "SignInFragment: dasdas");
    }


    @Override
    public void onStart() { // if user already signed
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client))
                .requestEmail().build();

        //   mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        Log.i("TAG", "on createee");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        editPasswordSignIn = view.findViewById(R.id.edtPasswordSignIn);
        edtEmailSignIn = view.findViewById(R.id.edtEmailSignIn);
        btnSignIn = view.findViewById(R.id.btnSignIn);
        txtSignUpIn = view.findViewById(R.id.txtSignUpIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Navigation.findNavController(v).navigate(R.id.action_signInFragment_to_homeScreenFragment);

                Log.i(TAG, "onClick: llll");

//                String email = edtEmailSignIn.getText().toString();
//                String password = editPasswordSignIn.getText().toString();
//                signIn(email, password);
            }
        });
        txtSignUpIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment2);

            }
        });
        return view;
    }


    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "onComplete: signed in  successfully");
                } else {
                    Log.i(TAG, "onComplete: signed in NOT DONE");
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mAuth.signOut();
    }

    private void signInWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential).addOnCompleteListener(   new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                         Log.d(TAG, "signInWithCredential:success");
                     //   FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());

                    }
                }
            });
    }

}
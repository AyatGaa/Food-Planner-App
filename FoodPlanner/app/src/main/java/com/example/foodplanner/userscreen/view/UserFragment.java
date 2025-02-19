package com.example.foodplanner.userscreen.view;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.Repository.authrepository.AuthRepository;
import com.example.foodplanner.Repository.authrepository.AuthRepositoryImpl;
import com.example.foodplanner.userscreen.presenter.UserPresenter;
import com.example.foodplanner.userscreen.presenter.UserPresenterImpl;
import com.example.foodplanner.utils.AppFunctions;
import com.example.foodplanner.utils.BottomSheetFragment;


public class UserFragment extends Fragment implements UserView {

    TextView userName, hi;
    Button signOut, signIn;
    AuthRepository authRepository;
    LinearLayout signInLayoutPlan;
    UserPresenter userPresenter;
    ConstraintLayout userConstraintLayout;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void setUI(View view) {
        signOut = view.findViewById(R.id.btnSignOut);
        signIn = view.findViewById(R.id.signInButtonUser);
        hi = view.findViewById(R.id.txtHi);
        userName = view.findViewById(R.id.txtUserNameUserScreen);
        signInLayoutPlan = view.findViewById(R.id.signInLayoutUser);
        userConstraintLayout = view.findViewById(R.id.userConstraintLayout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        setUI(view);
        userPresenter = new UserPresenterImpl(requireContext(), this);

        boolean userStatus = AppFunctions.isAuthenticated();
        if (!userStatus) {// is not auth=> need to sign in
            signInLayoutPlan.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.GONE);
            userName.setVisibility(View.GONE);
            hi.setVisibility(View.GONE);

            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(view).navigate(R.id.action_userFragment_to_signInFragment);
                }
            });

        } else { // is auth then go through app ..

            signInLayoutPlan.setVisibility(View.GONE);
            signOut.setVisibility(View.VISIBLE);
            userName.setVisibility(View.VISIBLE);
            hi.setVisibility(View.VISIBLE);


            authRepository = AuthRepositoryImpl.getInstance();
            userName.setText(("    " + getString(R.string.foodi_user)));
            signOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    authRepository.signOut();
                    Navigation.findNavController(view).navigate(R.id.action_userFragment_to_homeScreenFragment); //go to intro screen
                }

            });
        }
        userPresenter.checkInternetConnection();
        return view;
    }

    @Override
    public void showOnNoConnectionSearch() {
        if (!AppFunctions.isConnected(requireContext())) {
            clearSearchUI();
            setNoConnectionUserUI();
            BottomSheetFragment bottomSheet = new BottomSheetFragment();
            bottomSheet.show(getParentFragmentManager(), "NoConnectionBottomSheet");
        }
    }

    @Override
    public void clearSearchUI() {
        userConstraintLayout.removeAllViews();
    }

    @Override
    public void setNoConnectionUserUI() {
        userName.setText(getString(R.string.no_connection));
        userName.setGravity(Gravity.CENTER);

        ConstraintLayout.LayoutParams textParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        textParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        textParams.topMargin = 50;
        userName.setLayoutParams(textParams);

        ImageView noConnectionImage = new ImageView(requireContext());
        noConnectionImage.setId(View.generateViewId());
        noConnectionImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        ConstraintLayout.LayoutParams imageParams =
                new ConstraintLayout.LayoutParams(200, 400);

        imageParams.topToBottom = userName.getId(); // Place below the username
        imageParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        imageParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        imageParams.topMargin = 30;
        noConnectionImage.setLayoutParams(imageParams);

        Glide.with(requireContext())
                .load(R.drawable.cutlery)
                .error(R.drawable.notfound)
                .into(noConnectionImage);

        userConstraintLayout.addView(noConnectionImage);
        userConstraintLayout.addView(userName);
    }

}
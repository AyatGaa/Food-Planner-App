package com.example.foodplanner.userscreen.presenter;

import android.content.Context;

import com.example.foodplanner.userscreen.view.UserView;
import com.example.foodplanner.utils.AppFunctions;

public class UserPresenterImpl implements UserPresenter {

    Context context;
    UserView view;

    public UserPresenterImpl(Context context, UserView view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void checkInternetConnection() {
        boolean isConnected = AppFunctions.isConnected(context);
        if (!isConnected) {
            view.showOnNoConnectionSearch();
        }
    }
}

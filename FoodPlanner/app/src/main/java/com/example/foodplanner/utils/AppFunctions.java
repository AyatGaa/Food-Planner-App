package com.example.foodplanner.utils;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import androidx.navigation.Navigation;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public abstract class AppFunctions {

    public static void navigateTo(View view, int layoutId) {
        Navigation.findNavController(view).navigate(layoutId);
    }

    private static final Map<String, String> countryCodes = new HashMap<>();

    static {

        countryCodes.put("American", "US");
        countryCodes.put("British", "GB");
        countryCodes.put("Canadian", "CA");
        countryCodes.put("Chinese", "CN");
        countryCodes.put("Croatian", "HR");
        countryCodes.put("Dutch", "NL");
        countryCodes.put("Egyptian", "EG");
        countryCodes.put("Filipino", "PH");
        countryCodes.put("French", "FR");
        countryCodes.put("Greek", "GR");
        countryCodes.put("Indian", "IN");
        countryCodes.put("Irish", "IE");
        countryCodes.put("Italian", "IT");
        countryCodes.put("Jamaican", "JM");
        countryCodes.put("Japanese", "JP");
        countryCodes.put("Kenyan", "KE");
        countryCodes.put("Malaysian", "MY");
        countryCodes.put("Mexican", "MX");
        countryCodes.put("Moroccan", "MA");
        countryCodes.put("Polish", "PL");
        countryCodes.put("Portuguese", "PT");
        countryCodes.put("Russian", "RU");
        countryCodes.put("Spanish", "ES");
        countryCodes.put("Thai", "TH");
        countryCodes.put("Tunisian", "TN");
        countryCodes.put("Turkish", "TR");
        countryCodes.put("Ukrainian", "UA");
        countryCodes.put("Uruguayan", "UY");
        countryCodes.put("Vietnamese", "VN");

    }

    public static String getCountryCode(String countryName) {
        return countryCodes.getOrDefault(countryName, "");
    }


    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    public static void goToMealDetails(Context context, Meal meal, View view, int layoutId) {
        navigateTo(view, layoutId);


    }

    public static boolean isAuthenticated() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            return false;  // Prevent further execution
        }
        return true;
    }

    public static String getCurrentUserId() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        return userId;

    }
}

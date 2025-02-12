package com.example.foodplanner.network;

import retrofit2.Call;

public interface MealRemoteDataSource {
    void mealNetworkCall(NetworkCallback callBack);

}

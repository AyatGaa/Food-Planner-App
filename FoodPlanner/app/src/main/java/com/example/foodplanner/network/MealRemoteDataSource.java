package com.example.foodplanner.network;

import retrofit2.Call;

public interface MealRemoteDataSource {
    void mealNetworkCall(NetworkCallback callBack);
    void randomMealNetworkCall(RandomMealCallback callBack);

    void  filterMealByArea(NetworkCallback callBack, String area);

    void filterMealByIngredient(NetworkCallback callBack, String ingredient);
    void searchMealByName(NetworkCallback callBack, String mealName);
    void getAllAreas(NetworkCallback callBack);

}

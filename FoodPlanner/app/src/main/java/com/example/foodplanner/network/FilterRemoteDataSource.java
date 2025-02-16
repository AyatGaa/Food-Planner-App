package com.example.foodplanner.network;

public interface FilterRemoteDataSource {

    void categoryNetworkCall(NetworkCallback callBack);

    void filterMealByCategory(NetworkCallback callBack, String category);
    void filterMealByArea(NetworkCallback callBack, String area);
    void filterMealByIngredient(NetworkCallback callBack, String ingredient);

}

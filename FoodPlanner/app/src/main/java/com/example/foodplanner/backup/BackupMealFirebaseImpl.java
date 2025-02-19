package com.example.foodplanner.backup;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.foodplanner.Models.meals.Meal;
import com.example.foodplanner.Models.plannedMeal.PlannedMeal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class BackupMealFirebaseImpl implements BackupMealFirebase {
    FirebaseDatabase database;
    DatabaseReference dbRef;
final static String USER_DATA = "user_data";
final static String PLAN_MEAL = "plan_meal";
final static String FAVORITE_MEAL = "favorite_meal";
    private static BackupMealFirebaseImpl instance = null;
    /*
     * favorite meal =>
     *                   user_id=>
     *                             favorite_meal"meal itself"=>
     *                                                           meal_id
     *                                                           meal_id
     *                                                           meal_id
     *                                                           meal_id
     * */

    public BackupMealFirebaseImpl() {

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference(USER_DATA);
    }

    public static BackupMealFirebaseImpl getInstance() {
        if (instance == null) {
            instance = new BackupMealFirebaseImpl();

        }
        return instance;
    }

    @Override
    public void addMealToFirebase(Meal meal, String userId) { //
        dbRef.child(userId).child(FAVORITE_MEAL).child(meal.getIdMeal()).setValue(meal)
                .addOnSuccessListener(success -> Log.e("fb", "onSuccess: added " + meal.getIdMeal()))
                .addOnFailureListener(e -> Log.e("fb", "onFailure: " + e.getMessage()));
    }


    @Override
    public Observable<List<Meal>> getFavouriteMealsFromFirebase(String userId) {
        return Observable.create(emitter -> {
            dbRef.child(userId).child(FAVORITE_MEAL)
                    .addListenerForSingleValueEvent(new ValueEventListener() { //userid-> fav_meals (list of meals)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Meal> favMealFb = new ArrayList<>();

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Meal meal = dataSnapshot.getValue(Meal.class);

                                if (meal != null) {
                                    favMealFb.add(meal);
                                }

                            }
                              emitter.onNext(favMealFb);
                               emitter.onComplete();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }


    @Override
    public void deleteMealFromFirebase(Meal meal, String userId) {
        dbRef.child(userId).child(FAVORITE_MEAL).child(meal.getIdMeal())  // Adjust path if needed
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("fb", "onSuccess: deleted from fb  " + meal.getStrCategory() + " " + meal.getIdMeal());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("fb", "onFailure: failed to delete from fb  " + e.getMessage());

                    }
                });
    }

    // planned meals
    @Override
    public void addPlannedMealToFirebase(PlannedMeal plannedMeal, String userId, String plannedDate) {
        //String userId, String idMeal, String mealName, String mealImage, String date
      //  PlannedMeal plannedMeal = new PlannedMeal(meal.getUserId(), meal.getIdMeal(), meal.getStrMeal(), meal.getStrMealThumb(), plannedDate);

        dbRef.child(userId).child(PLAN_MEAL).child(plannedDate).child(plannedMeal.getIdMeal()).setValue(plannedMeal)
                .addOnSuccessListener(success -> Log.e("fb", "onSuccess: PLAN added " + plannedMeal.getIdMeal()))
                .addOnFailureListener(e -> Log.e("fb", "onFailure: PLAN add" + e.getMessage()));
    }

    @Override
    public Observable<List<PlannedMeal>> getPlannedMealsFromFirebase(String userId, String plannedDate) {
        return Observable.create(emitter -> {
            dbRef.child(userId).child(PLAN_MEAL).child(plannedDate)
                    .addValueEventListener(new ValueEventListener() { //userid-> plan_meal ->date-> meal (list of meals)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<PlannedMeal> planMealFb = new ArrayList<>();

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                PlannedMeal plannedMeal = dataSnapshot.getValue(PlannedMeal.class);

                                if (plannedMeal != null) {
                                    planMealFb.add(plannedMeal);
                                    Log.w("fb", "onDataChange: get from plan meal " + planMealFb.size() );
                                }

                            }
                               emitter.onNext(planMealFb);
                               emitter.onComplete();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    @Override
    public void deletePlannedMealToFirebase(PlannedMeal plannedMeal, String userId, String plannedDate) {
        dbRef.child(userId).child(PLAN_MEAL).child(plannedDate).child(plannedMeal.getIdMeal())  // Adjust path if needed
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("fb", "onSuccess: PLAN deleted from fb  " + plannedMeal.getMealName() + " " + plannedMeal.getIdMeal());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("fb", "onFailure: failed to PLAN delete from fb  " + e.getMessage());

                    }
                });
    }

    @Override
    public Observable<List<PlannedMeal>> getPlannedMealsFromFirebaseByDate(String userId, String plannedDate) {
        return Observable.create(emitter -> {
            dbRef.child(userId).child(PLAN_MEAL).child(plannedDate)
                    .addListenerForSingleValueEvent(new ValueEventListener() { //userid-> plan_meal ->date-> meal (list of meals)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<PlannedMeal> planMealFb = new ArrayList<>();

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                PlannedMeal plannedMeal = dataSnapshot.getValue(PlannedMeal.class);

                                if (plannedMeal != null) {
                                    planMealFb.add(plannedMeal);
                                    Log.w("fb", "onDataChange: get from plan meal " + planMealFb.size() );
                                }

                            }
                            emitter.onNext(planMealFb);
                            emitter.onComplete();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }
}

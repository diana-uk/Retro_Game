package com.example.hw2updated.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class GameSP {

    private final String SHARED_PREF_KEY = "SHARED_KEY";

    private SharedPreferences sharedPreferences = null;
    //Design Pattern singleton
    private static GameSP instance;
    //TODO context ???
    private GameSP(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
    }

    public static void initInstance(Context context) {
        if (instance == null) {
            instance = new GameSP(context);
        }
    }

    public static GameSP getInstance() {
        return instance;
    }

    public String getString(String KEY, String defValue) {
        return sharedPreferences.getString(KEY, defValue);
    }

    public void putString(String KEY, String value) {
        sharedPreferences.edit().putString(KEY, value).apply();
    }
}

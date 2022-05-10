package com.example.hw2updated.activities;

import android.app.Application;

import com.example.hw2updated.utils.GameSP;

//ALL the app itself
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate ();
        //Here i do init to shared preferences
        GameSP.initInstance (this);
    }
}

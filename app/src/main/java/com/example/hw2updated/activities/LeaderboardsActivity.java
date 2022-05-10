package com.example.hw2updated.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.hw2updated.R;
import com.example.hw2updated.callbacks.CallBack_ListRecordClicked;
import com.example.hw2updated.fragments.Fragment_List;
import com.example.hw2updated.fragments.Fragment_Map;
import com.google.android.material.button.MaterialButton;

public class LeaderboardsActivity extends AppCompatActivity {
    private Fragment_List fragment_list;
    private Fragment_Map fragment_map;
    private MaterialButton leaderboards_BTN_playAgain;
    private ImageView leaderboards_IMG_exit;

    private CallBack_ListRecordClicked callBack_listRecordClicked = new CallBack_ListRecordClicked() {
        @Override
        public void recordClicked(double lat, double lng) {
            fragment_map.setMapLocation(lat, lng);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);
        findViews();
        setOnClickListeners();
        setFragments();

        if (!getIntent().getExtras().getBoolean(getString(R.string.BUNDLE_KEY_FROM_GAME)))
            leaderboards_BTN_playAgain.setVisibility(View.INVISIBLE);
    }

    private void setFragments() {
        fragment_list = new Fragment_List(callBack_listRecordClicked);
        getSupportFragmentManager().beginTransaction().add(R.id.leaderboards_LAY_list, fragment_list).commit();

        fragment_map = new Fragment_Map();
        getSupportFragmentManager().beginTransaction().add(R.id.leaderboards_LAY_map, fragment_map).commit();
    }

    private void findViews() {
        leaderboards_BTN_playAgain = findViewById(R.id.leaderboards_BTN_playAgain);
        leaderboards_IMG_exit = findViewById(R.id.leaderboards_IMG_exit);
    }

    private void setOnClickListeners() {
        leaderboards_BTN_playAgain.setOnClickListener(view -> playAgain());
        leaderboards_IMG_exit.setOnClickListener(view -> exitClicked());
    }

    private void exitClicked() {
        onBackPressed();
    }

    private void playAgain() {
        //Intent intent = new Intent(this, Activity_PlayerDetails.class);
        //startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
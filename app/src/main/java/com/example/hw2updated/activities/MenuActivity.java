package com.example.hw2updated.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hw2updated.data.Record;
import com.example.hw2updated.models.GameType;
import com.example.hw2updated.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

public class MenuActivity extends AppCompatActivity {
    private MaterialTextView menu_TXT_playerName;
    private MaterialButton menu_BTN_startGameButtons;
    private MaterialButton menu_BTN_startGameSensors;
    private MaterialButton menu_BTN_goToTopTen;
    Bundle bundle;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        getRecord();
        setClickListeners();

    }

        private void getRecord() {
            bundle = getIntent().getExtras();
            String json = bundle.getString(getString(R.string.BUNDLE_RECORD_KEY));
            Record record=new Gson ().fromJson(json, Record.class);
            name=record.getName ();
            menu_TXT_playerName.setText (name);

        }


    private void setClickListeners() {
        Intent intent = new Intent (this, GameActivity.class);

        menu_BTN_startGameSensors.setOnClickListener(view -> {
            bundle.putString ("GameType", GameType.SENSORS.toString ());
            intent.putExtras(bundle);
            startActivity(intent);
        });
        menu_BTN_startGameButtons.setOnClickListener (view ->  {
            bundle.putString ("GameType",GameType.BUTTONS.toString ());
            intent.putExtras(bundle);
            startActivity(intent);
        } );
        menu_BTN_goToTopTen.setOnClickListener (view -> openLeaderboardsActivity());

    }

    private void findViews() {
        menu_TXT_playerName=findViewById (R.id.menu_TXT_playerName);
        menu_BTN_startGameButtons=findViewById(R.id.menu_BTN_startGameButtons);
        menu_BTN_startGameSensors=findViewById(R.id.menu_BTN_startGameSensors);
        menu_BTN_goToTopTen=findViewById(R.id.menu_BTN_goToTopTen);
    }

    private void openLeaderboardsActivity() {
        Intent intent = new Intent (this, LeaderboardsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(getString(R.string.BUNDLE_KEY_FROM_GAME),false);
        intent.putExtras(bundle);
        startActivity(intent);
    }


        // Store the data in the SharedPreference
        // in the onPause() method
        // When the user closes the application
        // onPause() will be called
        // and data will be stored
        @Override
        protected void onPause() {
            super.onPause();

            // Creating a shared pref object
            // with a file name "MySharedPref"
            // in private mode




        }
}
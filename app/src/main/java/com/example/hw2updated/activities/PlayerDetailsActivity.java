package com.example.hw2updated.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hw2updated.R;
import com.example.hw2updated.data.Record;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;


public class PlayerDetailsActivity extends AppCompatActivity {

    private EditText details_EDT_name;
    private MaterialButton details_BTN_play;
    private final int REQUEST_CODE = 44;
    private boolean locationPermissionGranted;
    private FusedLocationProviderClient client;

    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);

        details_BTN_play = findViewById(R.id.details_BTN_play);
        details_EDT_name = findViewById(R.id.details_EDT_name);

        details_BTN_play.setOnClickListener((view) -> playClicked());
        //Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

    }

    /**
     * Play button clicked - check if location permission is granted and if entered valid name.
     */
    private void playClicked() {
        if (!locationPermissionGranted){
            Toast.makeText(this,getString(R.string.LOCATION_PERMISSION_MESSAGE),Toast.LENGTH_LONG).show();
            getCurrentLocation();
        }else if (details_EDT_name.getText().toString().isEmpty()){
            Toast.makeText(this,getString(R.string.INVALID_NAME_ENTERED_MESSAGE),Toast.LENGTH_LONG).show();
        }else{
            startGame();
            finish();
        }

    }

    /**
     * open the game activity and send the player name and location
     */
    private void startGame() {
        String name = details_EDT_name.getText().toString();
        String json = new Gson ().toJson( new Record (name,latLng));

        Intent intent = new Intent (this, MenuActivity.class);
        Bundle bundle =new Bundle ();
        bundle.putString(getString(R.string.BUNDLE_RECORD_KEY),json);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                getCurrentLocation();
            }
        }
    }


    /**
     * if the permission is granted  gets  the current location of the player , else it asks for permission
     */
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

        }else{
            locationPermissionGranted = true;
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location> () {
                @Override
                public void onSuccess(Location location) {
                    //When success
                    if(location != null){
                        latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    }
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
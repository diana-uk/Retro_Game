package com.example.hw2updated.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hw2updated.callbacks.CallBack_TimerUpdate;
import com.example.hw2updated.data.Record;
import com.example.hw2updated.models.CollisionType;
import com.example.hw2updated.logic.GameManager;
import com.example.hw2updated.models.GameType;
import com.example.hw2updated.models.MoveDirection;
import com.example.hw2updated.R;
import com.example.hw2updated.utils.TimerManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

public class GameActivity extends AppCompatActivity {
    private ImageView[] game_IMG_hearts;
    private ImageView[][] game_IMG_matrix;
    private ImageView game_IMG_btnUp;
    private ImageView game_IMG_btnDown;
    private ImageView game_IMG_btnRight;
    private ImageView game_IMG_btnLeft;

    private MaterialTextView game_TXT_score;
    private GameManager gameManager;

    private SensorManager sensorManager;
    private Sensor acc_sensor;
    private SensorEventListener accSensorEventListener;

    private MediaPlayer coinSound;
    private MediaPlayer collisionSound;

    private TimerManager timerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_game);
        gameManager = new GameManager ();
        gameManager.setCurrentGameType (GameType.valueOf (getIntent ().getExtras ().getString ("GameType")));

        findViews ();
        getRecord();
        initSounds();
        initSensors();
        setListeners ();
        initTimer();

        initGame ();
    }

    private void initTimer() {
        CallBack_TimerUpdate callBack_Timer_update = () -> runOnUiThread(this::update);
        timerManager = new TimerManager (callBack_Timer_update);
    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService (Context.SENSOR_SERVICE);
        acc_sensor = sensorManager.getDefaultSensor (Sensor.TYPE_ACCELEROMETER);
    }


    private void initSounds() {
        coinSound = MediaPlayer.create(this, R.raw.eating_sound);
        collisionSound = MediaPlayer.create(this, R.raw.collision_sound);
    }

    private void setListeners() {
        if (gameManager.getCurrentGameType ().equals (GameType.BUTTONS))
            setClickListeners ();
        else
            setMotionSensorsListener ();
    }

    private void setMotionSensorsListener() {
        accSensorEventListener = new SensorEventListener () {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                gameManager.findDirectionSensors (sensorEvent.values[0], sensorEvent.values[1]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //Method not used
            }
        };
    }
    private void getRecord() {
        Bundle bundle = getIntent().getExtras();
        String json = bundle.getString(getString(R.string.BUNDLE_RECORD_KEY));
        gameManager.setCurrentRecord(new Gson().fromJson(json,Record.class));
    }

    private void initGame() {
        if(gameManager.getCurrentGameType ().equals (GameType.SENSORS))
            cleanGameArrows ();
        gameManager.initPosition ();
        timerManager.startTimer ();
    }

    private void updateFugitivePlayerDirection(MoveDirection moveDirection) {
        gameManager.updateFugitivePlayerDirectionData (moveDirection);
    }

    private void setClickListeners() {
        game_IMG_btnUp.setOnClickListener (view -> updateFugitivePlayerDirection (MoveDirection.UP));
        game_IMG_btnDown.setOnClickListener (view -> updateFugitivePlayerDirection (MoveDirection.DOWN));
        game_IMG_btnRight.setOnClickListener (view -> updateFugitivePlayerDirection (MoveDirection.RIGHT));
        game_IMG_btnLeft.setOnClickListener (view -> updateFugitivePlayerDirection (MoveDirection.LEFT));
    }

    private void findViews() {
        game_IMG_btnUp = findViewById (R.id.game_IMG_btnUP);
        game_IMG_btnDown = findViewById (R.id.game_IMG_btnDOWN);
        game_IMG_btnRight = findViewById (R.id.game_IMG_btnRIGHT);
        game_IMG_btnLeft = findViewById (R.id.game_IMG_btnLEFT);
        game_TXT_score = findViewById (R.id.game_TXT_score);
        game_IMG_hearts = new ImageView[]{
                findViewById (R.id.game_IMG_heart1),
                findViewById (R.id.game_IMG_heart2),
                findViewById (R.id.game_IMG_heart3)
        };

        game_IMG_matrix = new ImageView[gameManager.getMatRows ()][gameManager.getMatCols ()];

        for (int i = 0; i < gameManager.getMatRows (); i++) {
            for (int j = 0; j < gameManager.getMatCols (); j++) {
                String imageViewName = "game_IMG_matrix_" + i + j;
                int imageId = this.getResources ().getIdentifier (imageViewName, "id", this.getPackageName ());
                game_IMG_matrix[i][j] = findViewById (imageId);
            }
        }
    }


    private void update() {
        gameManager.updateGameData ();

        if (gameManager.checkIfCollision ()) {
            collisionSound.start ();
            gameManager.setCurrentCollisionType (CollisionType.ENEMY_AND_FUGITIVE);
            timerManager.stopTimer ();
            vibrateOnce ();
            gameManager.updateCollisionEventGameData ();
            updateCollisionEventUI ();
            updateUI ();

            if (gameManager.isGameOver ()) {
                updateUI ();
                saveGameData();
                openLeaderboards();

                finish ();
            }
            else {
                updateUI ();
                showMessageOfLivesLeft ();
                initGame ();
            }
        } else if (gameManager.checkIfCoinCollision ()) {
            coinSound.start ();
            gameManager.setCurrentCollisionType (CollisionType.COIN);
            gameManager.updateCollisionCoinEventData ();
            updateCollisionCoinEventUI ();
            updateCoin ();
            updateUI ();
        }
        else if(gameManager.checkIfCoinReplacement()) {
            updateCoin ();
            updateUI ();
        }
        else
            updateUI ();

    }

    //Method opens the the leaderboards
    private void openLeaderboards() {
        Intent intent = new Intent (this, LeaderboardsActivity.class);
        Bundle bundle = this.getIntent().getExtras();
        bundle.putBoolean(getString(R.string.BUNDLE_KEY_FROM_GAME),true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void updateCollisionCoinEventUI() {
        updateScoreUI ();
    }

    private void showMessageOfLivesLeft() {
        if (gameManager.getLives () == gameManager.getMaxLives () - 1)
            Snackbar.make (findViewById (R.id.game_LAYOUT_relative), R.string.two_lives_left,
                    Snackbar.LENGTH_SHORT)
                    .show ();

        else if (gameManager.getLives () == gameManager.getMaxLives () - 2)
            Snackbar.make (findViewById (R.id.game_LAYOUT_relative), R.string.one_life_left,
                    Snackbar.LENGTH_SHORT)
                    .show ();
    }

    private void updateCollisionEventUI() {
        for (int i = 0; i < game_IMG_hearts.length; i++) {
            game_IMG_hearts[i].setVisibility (gameManager.getLives () > i ? View.VISIBLE : View.INVISIBLE);
        }
        cleanGameMatrix ();
    }

    private void updateUI() {
        cleanGameMatrixCase ();

        if (gameManager.getCurrentGameType ().equals (GameType.SENSORS))
            updateArrowImageUI ();

        updateFugitivePlayerInUI ();
        updateEnemyPlayerInUI ();
        updateScoreUI ();
    }

    private void cleanGameMatrixCase() {
        if (gameManager.checkIfCollision ())
            cleanGameMatrix ();
        else
            cleanGameMatrixWithoutCoin ();
    }

    /**
     * Method purpose is when using the sensors the current direction arrow image will appear
     */
    private void updateArrowImageUI() {
        String imageViewName;
        int imageId;
        ImageView imageArrow;

        cleanGameArrows();
        MoveDirection currentFugitiveDirection = gameManager.getCurrentFugitiveDirection ();
        imageViewName = "game_IMG_btn" + currentFugitiveDirection.toString ();
        imageId = this.getResources ().getIdentifier (imageViewName, "id", this.getPackageName ());
        imageArrow = findViewById (imageId);
        imageArrow.setVisibility (View.VISIBLE);
    }

    private void cleanGameArrows() {
        String imageViewName;
        int imageId;
        ImageView imageArrow;

        for (MoveDirection dir : MoveDirection.values ()) {
            if (!dir.equals (MoveDirection.NONE)) {
                imageViewName = "game_IMG_btn" + dir.toString ();
                imageId = this.getResources ().getIdentifier (imageViewName, "id", this.getPackageName ());
                imageArrow = findViewById (imageId);
                imageArrow.setVisibility (View.INVISIBLE);
            }
        }
    }

    private void cleanGameMatrixWithoutCoin() {
        int currentCoinRow = gameManager.getCurrentCoinPositionRow ();
        int currentCoinCol = gameManager.getCurrentCoinPositionCol ();

        for (int i = 0; i < gameManager.getMatRows (); i++) {
            for (int j = 0; j < gameManager.getMatCols (); j++) {
                if (i == currentCoinRow && j == currentCoinCol) {
                    game_IMG_matrix[i][j].setImageResource (R.drawable.ic_cheese);
                }
                else
                    game_IMG_matrix[i][j].setImageResource (0);
            }
        }
    }

    private void updateScoreUI() {
        game_TXT_score.setText ("" + gameManager.getScore ());
    }

    private void updateEnemyPlayerInUI() {
        int currentEnemyRow = gameManager.getCurrentEnemyPositionRow ();
        int currentEnemyCol = gameManager.getCurrentEnemyPositionCol ();
        game_IMG_matrix[currentEnemyRow][currentEnemyCol].setImageResource (R.drawable.ic_cat);
    }

    private void updateFugitivePlayerInUI() {
        int currentFugitiveRow = gameManager.getCurrentFugitivePositionRow ();
        int currentFugitiveCol = gameManager.getCurrentFugitivePositionCol ();
        game_IMG_matrix[currentFugitiveRow][currentFugitiveCol].setImageResource (R.drawable.ic_mouse);
    }

    private void cleanGameMatrix() {
        for (int i = 0; i < gameManager.getMatRows (); i++) {
            for (int j = 0; j < gameManager.getMatCols (); j++) {
                game_IMG_matrix[i][j].setImageResource (0);
            }
        }
    }

    private void vibrateOnce() {
        Vibrator v = (Vibrator) getSystemService (Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate (VibrationEffect.createOneShot (500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate (500);
        }
    }

    private void updateCoin() {
        game_IMG_matrix[gameManager.getCurrentCoinPositionRow ()][gameManager.getCurrentCoinPositionCol ()].setImageResource (R.drawable.ic_mouse);
        gameManager.updateCoinData ();
        updateCoinInUI ();
    }

    private void updateCoinInUI() {
        game_IMG_matrix[gameManager.getCurrentCoinPositionRow ()][gameManager.getCurrentCoinPositionCol ()].setImageResource (R.drawable.ic_cheese);
    }

    private void saveGameData() {
        gameManager.saveData();
    }

    @Override
    protected void onResume() {
        super.onResume ();
        sensorManager.registerListener (accSensorEventListener, acc_sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause ();
        sensorManager.unregisterListener (accSensorEventListener);
    }
}
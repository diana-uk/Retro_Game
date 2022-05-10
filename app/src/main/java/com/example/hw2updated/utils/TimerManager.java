package com.example.hw2updated.utils;

import android.os.Handler;

import com.example.hw2updated.callbacks.CallBack_TimerUpdate;
import com.example.hw2updated.logic.TimerStatus;

public class TimerManager {
    private final int DELAY = 1000;
    private TimerStatus timerStatus = TimerStatus.OFF;
    private CallBack_TimerUpdate callBack_Timer_update;
    private final Runnable r ;
    private final Handler handler = new Handler();



    public TimerManager(CallBack_TimerUpdate callBack) {
        this.callBack_Timer_update = callBack;
        r =  new Runnable() {
            public void run() {
                handler.postDelayed(r, DELAY);
                callBack_Timer_update.CallBackUpdate();
            }
        };
    }




    public void startTimer() {
        timerStatus = TimerStatus.RUNNING;
        handler.postDelayed(r, DELAY);
    }


    public void stopTimer() {
        timerStatus = TimerStatus.PAUSE;
        handler.removeCallbacks(r);
    }

    public TimerStatus getTimerStatus() {
        return timerStatus;
    }


}
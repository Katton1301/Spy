package com.katton.spy;

import static com.katton.spy.GameModel.GetGameModel;

import android.os.Handler;
import android.widget.TextView;

public class TimerClass implements Runnable {
    private final Handler handler = new Handler();
    private GameProcess gameActivity;

    private long gameTimeSeconds;
    private volatile long startTime;
    private volatile long elapsedTime;

    public TimerClass( GameProcess gameActivity ) {
        this.gameActivity = gameActivity;
    }

    public void setGameTime(long _gameTime) {
        this.gameTimeSeconds = _gameTime;
    }

    @Override
    public void run() {
        long millis = (gameTimeSeconds * 1000) - (System.currentTimeMillis() - startTime);
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        if(millis > 0) {
            gameActivity.GetTimerText().setText(String.format("%d:%02d", minutes, seconds));

            if (elapsedTime == -1) {
                handler.postDelayed(this, 500);
            }
        }
        else
        {
            gameActivity.onTimerFinish();
        }
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = -1;
        handler.post(this);
    }

    public void stop() {
        long secondsLeft = (System.currentTimeMillis() - startTime) / 1000;
        this.gameTimeSeconds = gameTimeSeconds - secondsLeft;
        GetGameModel().setGameTime((int) this.gameTimeSeconds);
        this.elapsedTime = System.currentTimeMillis() - startTime;
        handler.removeCallbacks(this);
    }

    public long getElapsedTime() {
        return elapsedTime;
    }
}

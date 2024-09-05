package com.katton.spy;

import static com.katton.spy.GameModel.GetGameModel;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class GameProcess extends AppCompatActivity {

    TextView timerText;
    TextView playerFirstName;
    Button buttonPlay;
    Boolean isSpyAnswer;
    TimerClass timer;
    Boolean timerPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_process);

        timerText = findViewById(R.id.timerView);
        buttonPlay = findViewById(R.id.timerBtn);
        playerFirstName = findViewById(R.id.playerFirstName);

        timer = new TimerClass( this );
        timerPlay = false;
        isSpyAnswer = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK)
        {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        timer.setGameTime(GetGameModel().getGameTime());
        String firstPlayer = "Первым задает вопрос " + GetGameModel().getFirstPlayerName();
        playerFirstName.setText(firstPlayer);
        buttonPlay.callOnClick();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onBtnPlayClick(View view)
    {
        timerPlay = !timerPlay;
        if(timerPlay)
        {
            GetGameModel().setGameStage(GameModel.TGameStage.PROCESS);
            buttonPlay.setBackground(getResources().getDrawable(R.drawable.custom_btn_pause, null));
            timer.start();
        }
        else
        {
            if(isSpyAnswer)
            {
                GetGameModel().setGameStage(GameModel.TGameStage.STOP_SPY_ANSWER);
            }
            else
            {
                GetGameModel().setGameStage(GameModel.TGameStage.STOP_VOTE);
            }
            buttonPlay.setBackground(getResources().getDrawable(R.drawable.custom_btn_play, null));
            timer.stop();
        }
    }
    public void onTimerFinish()
    {
        GetGameModel().setGameStage(GameModel.TGameStage.TIME_OUt);
        timer.stop();
        Intent intent = new Intent(this, GameVote.class);
        startActivity(intent);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onButtonVoteAll(View view)
    {
        if(timerPlay)
        {
            isSpyAnswer = false;
            buttonPlay.callOnClick();
        }
        Intent intent = new Intent(this, GameVote.class);
        startActivity(intent);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onButtonAnswerSpy(View view)
    {
        if(timerPlay)
        {
            isSpyAnswer = true;
            buttonPlay.callOnClick();
        }
        Intent intent = new Intent(this, SpyAnswerActivity.class);
        startActivity(intent);
    }

    public TextView GetTimerText()
    {
        return timerText;
    }

}
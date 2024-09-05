package com.katton.spy;

import static com.katton.spy.GameModel.GetGameModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class CreateGameActivity extends AppCompatActivity {

    TextView viewPlayerNumber;
    TextView viewSpyNumber;
    TextView viewGameTime;
    CheckBox randomSpyNumberCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        viewPlayerNumber = findViewById(R.id.viewPlayerNumber);
        viewSpyNumber = findViewById(R.id.viewSpyNumber);
        randomSpyNumberCheck = findViewById(R.id.randomSpyBox);
        viewGameTime = findViewById(R.id.viewGameTime);


        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        GetGameModel().setGameTime(Integer.parseInt(preferences.getString(
                "GAME_TIME",
                getResources().getString(R.string.default_game_time)
                )) * 60);
        GetGameModel().setPlayersCount(Integer.parseInt(preferences.getString(
                "PLAYER_COUNT",
                getResources().getString(R.string.default_players_number)
        )));
        GetGameModel().setSpyCount(Integer.parseInt(preferences.getString(
                "SPY_COUNT",
                getResources().getString(R.string.default_spy_number)
        )));
        GetGameModel().setRandomSpyNumber(false);

        viewPlayerNumber = findViewById(R.id.viewPlayerNumber);
        viewSpyNumber = findViewById(R.id.viewSpyNumber);
        randomSpyNumberCheck = findViewById(R.id.randomSpyBox);
        viewGameTime = findViewById(R.id.viewGameTime);

        randomSpyNumberCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GetGameModel().setRandomSpyNumber(isChecked);
                Button buttonMinus = findViewById(R.id.minusSpyBtn);
                Button buttonPlus = findViewById(R.id.plusSpyBtn);
                buttonMinus.setEnabled(!isChecked);
                buttonPlus.setEnabled(!isChecked);
                viewSpyNumber.setText( isChecked ? "?" : String.valueOf(GetGameModel().getSpyCount()) );
            }
        });

        viewPlayerNumber.setText(String.valueOf(GetGameModel().getPlayersCount()));
        viewSpyNumber.setText(String.valueOf(GetGameModel().getSpyCount()));
        randomSpyNumberCheck.setChecked(GetGameModel().isRandomSpyNumber());
        viewGameTime.setText(String.valueOf(GetGameModel().getGameTime() / 60));
    }
    public void addPlayerNumber(View view)
    {
        GetGameModel().setPlayersCount(GetGameModel().getPlayersCount() + 1);
        viewPlayerNumber.setText(String.valueOf(GetGameModel().getPlayersCount()));
    }
    public void erasePlayerNumber(View view)
    {
        if(
            GetGameModel().getPlayersCount() > 1 &&
            GetGameModel().getPlayersCount() - 1 > GetGameModel().getSpyCount())
        {
            GetGameModel().setPlayersCount(GetGameModel().getPlayersCount() - 1);
            viewPlayerNumber.setText(String.valueOf(GetGameModel().getPlayersCount()));
        }
    }
    public void addSpyNumber(View view)
    {
        if(GetGameModel().getSpyCount() + 1 < GetGameModel().getPlayersCount())
        {
            GetGameModel().setSpyCount(GetGameModel().getSpyCount() + 1);
            viewSpyNumber.setText(String.valueOf(GetGameModel().getSpyCount()));
        }
    }
    public void eraseSpyNumber(View view)
    {
        if(GetGameModel().getSpyCount() > 1)
        {
            GetGameModel().setSpyCount(GetGameModel().getSpyCount() - 1);
            viewSpyNumber.setText(String.valueOf(GetGameModel().getSpyCount()));
        }

    }
    public void addGameTime(View view)
    {
        GetGameModel().setGameTime(GetGameModel().getGameTime() + 60);
        viewGameTime.setText(String.valueOf(GetGameModel().getGameTime() / 60));
    }
    public void eraseGameTime(View view)
    {
        if(GetGameModel().getGameTime() > 60)
        {
            GetGameModel().setGameTime(GetGameModel().getGameTime() - 60);
            viewGameTime.setText(String.valueOf(GetGameModel().getGameTime() / 60));
        }
    }
    public void goToBeginGame(View view)
    {
        GetGameModel().saveGameSettings(getPreferences(MODE_PRIVATE));
        GetGameModel().generateGame(getResources().getString(R.string.spy_role));
        Intent intent = new Intent(this, PreviewRoleActivity.class);
        startActivity(intent);
    }
    public void goToMain(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
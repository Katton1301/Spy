package com.katton.spy;

import static com.katton.spy.GameModel.GetGameModel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GameVote extends AppCompatActivity {

    private ListView playerList;
    ArrayList<String> playerNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_vote);

        playerList = findViewById(R.id.playerView);

        playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(GetGameModel().getPlayersList().get(position).isSpy)
                {
                    String spyName = GetGameModel().getPlayersList().get(position).getName();
                    GetGameModel().getPlayersList().remove(position);
                    if(GetGameModel().getSpyCount() > 1)
                    {
                        if(GetGameModel().getFirstPlayerIndex() == GetGameModel().getPlayersList().size())
                        {
                            GetGameModel().setFirstPlayerIndex(GetGameModel().getFirstPlayerIndex() - 1);
                        }
                        GetGameModel().setSpyCount(GetGameModel().getSpyCount() - 1);

                        AlertDialog.Builder builder = new AlertDialog.Builder(GameVote.this);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.custom_alert, null);
                        builder.setView(dialogView);
                        AlertDialog dialog = builder.create();
                        String spyLost = GetGameModel().isRandomSpyNumber()
                                ? "Еще остались шпионы!"
                                : "Осталось " + GetGameModel().getSpyCount() + "шпион(ов)";
                        String message = spyName + " шпион! " + spyLost;
                        String gameStage = GetGameModel().getGameStage() == GameModel.TGameStage.TIME_OUt
                                ? "голосование"
                                : "игру";
                        String buttonName = "Продолжить " + gameStage;
                        TextView titleText = dialogView.findViewById(R.id.textTitle);
                        titleText.setText("Шпион найден");
                        TextView msgText = dialogView.findViewById(R.id.textMessage);
                        msgText.setText(message);
                        Button okButton = dialogView.findViewById(R.id.okButton);
                        okButton.setMaxWidth(1000);
                        okButton.setText(buttonName);
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(GetGameModel().getGameStage() == GameModel.TGameStage.TIME_OUt)
                                {
                                    updatePlayersListView();
                                    dialog.dismiss();
                                }
                                else
                                {
                                    dialog.dismiss();
                                    Intent intent = new Intent(GameVote.this, GameProcess.class);
                                    startActivity(intent);
                                }
                            }
                        });
                        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
                        cancelButton.setVisibility(View.INVISIBLE);
                        dialog.show();
                    }
                    else
                    {
                        GetGameModel().setGameResult(true);
                        GetGameModel().setGameStage(GameModel.TGameStage.END);
                        Intent intent = new Intent(GameVote.this, EndGame.class);
                        startActivity(intent);
                    }
                }
                else
                {
                    GetGameModel().setGameResult(false);
                    GetGameModel().setGameStage(GameModel.TGameStage.END);
                    Intent intent = new Intent(GameVote.this, EndGame.class);
                    startActivity(intent);
                }
            }
        });

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
        updatePlayersListView();
    }
    private void updatePlayersListView()
    {
        playerNames = new ArrayList<>();
        for(int i = 0; i < GetGameModel().getPlayersList().size(); i+=1)
        {
            playerNames.add(GetGameModel().getPlayersList().get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this, R.layout.players_to_vote_item, R.id.playerViewItem, playerNames);
        playerList.setAdapter(adapter);
    }
}
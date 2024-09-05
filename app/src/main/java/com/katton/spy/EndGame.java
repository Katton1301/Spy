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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EndGame extends AppCompatActivity {

    TextView resultText;
    ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        resultText = findViewById(R.id.game_result);
        imageBack = findViewById(R.id.imageView);
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

        if(GetGameModel().getGameResult())
        {
            resultText.setText(getResources().getString(R.string.spy_lose));
            resultText.setTextColor(getResources().getColor(R.color.holo_green_dark,null));
            imageBack.setImageResource(R.drawable.win_back);

        }
        else
        {
            resultText.setText(getResources().getString(R.string.spy_win));
            resultText.setTextColor(getResources().getColor(R.color.red_light,null));
            imageBack.setImageResource(R.drawable.lose_back_2);
        }
    }

    public void onStartNew(View view)
    {
        if(GetGameModel().anyUsedLocationExist(GetGameModel().getGameLocationIndex())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.custom_alert, null);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            TextView titleText = dialogView.findViewById(R.id.textTitle);
            titleText.setText("Использование локации");
            TextView msgText = dialogView.findViewById(R.id.textMessage);
            msgText.setText("Оставить локацию в используемых игрой?");
            Button okButton = dialogView.findViewById(R.id.okButton);
            okButton.setText("Да");
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetGameModel().initialize();
                    dialog.dismiss();
                    Intent intent = new Intent(EndGame.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            Button cancelButton = dialogView.findViewById(R.id.cancelButton);
            cancelButton.setText("Нет");
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetGameModel().dontUseGameLocation(EndGame.this);
                    GetGameModel().initialize();
                    dialog.dismiss();
                    Intent intent = new Intent(EndGame.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            dialog.show();
        }
        else
        {
            Intent intent = new Intent(EndGame.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
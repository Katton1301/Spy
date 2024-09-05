package com.katton.spy;

import static com.katton.spy.GameModel.GetGameModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ViewRoleActivity extends AppCompatActivity {

    TextView locationView;
    EditText roleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_role);

        locationView = findViewById(R.id.locationName);
        roleView = findViewById(R.id.editTextRole);
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
        PlayerClass player = GetGameModel().getNextPlayer();
        String location;
        String roleText;
        if(player.isSpy())
        {
            location = "Вы шпион";
            roleText = player.getRole();
        }
        else
        {
            location = GetGameModel().getLocationName();
            String description = GetGameModel().getLocationDescription();
            roleText = description + "\n\nРоль:\n" + player.getRole();
        }

        locationView.setText(location);
        roleView.setText(roleText);
    }

    public void onNextStep(View view)
    {
        if(GetGameModel().getReadyPlayers() < GetGameModel().getPlayersCount())
        {
            Intent intent = new Intent(this, PreviewRoleActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, GameProcess.class);
            startActivity(intent);
        }
    }
}
package com.katton.spy;

import static com.katton.spy.GameModel.GetGameModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class PreviewRoleActivity extends AppCompatActivity {

    TextView playerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_role);

        playerView = findViewById(R.id.playerName);
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
        playerView.setText(GetGameModel().getCurrentPlayerName());
    }

    public void onGoToRoleView(View view)
    {
        Intent intent = new Intent(this, ViewRoleActivity.class);
        startActivity(intent);
    }
}
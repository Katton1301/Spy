package com.katton.spy;

import static com.katton.spy.GameModel.GetGameModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetGameModel().readLocationsDB(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK)
        {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void goToCreateGame(View view)
    {
        Intent intent = new Intent(this, CreateGameActivity.class);
        startActivity(intent);
    }
    public void goToLocation(View view)
    {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }
    public void goToRules(View view)
    {
        Intent intent = new Intent(this, RulesActivity.class);
        startActivity(intent);
    }
}
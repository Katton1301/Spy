package com.katton.spy;

import static com.katton.spy.GameModel.GetGameModel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity {

    private ListView locationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locationView = findViewById(R.id.locationView);

        locationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetGameModel().setEditedLocationIndex(position);
                Intent intent = new Intent(LocationActivity.this, LocationEdit.class);
                startActivity(intent);
            }
        });
    }

    public void onHelpButtonClick(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_alert, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        TextView titleText = dialogView.findViewById(R.id.textTitle);
        titleText.setText("Подсказка");
        TextView msgText = dialogView.findViewById(R.id.textMessage);
        msgText.setText(getResources().getString(R.string.help_location_edit));
        Button okButton = dialogView.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        cancelButton.setVisibility(View.INVISIBLE);
        dialog.show();
    }
    public void goToMain(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onAddLocation(View view)
    {
        GetGameModel().setEditedLocationIndex(GetGameModel().getLocationCount());
        Intent intent = new Intent(LocationActivity.this, LocationEdit.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateLocationListView();
    }
    private void updateLocationListView()
    {
        LocationAdapter adapter = new LocationAdapter( this);
        locationView.setAdapter(adapter);
    }
}
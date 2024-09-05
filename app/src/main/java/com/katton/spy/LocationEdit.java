package com.katton.spy;

import static com.katton.spy.GameModel.GetGameModel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class LocationEdit extends AppCompatActivity {

    private ListView rolesList;

    private RoleAdapter adapter;
    private EditText locationName;
    private EditText descriptionName;
    LocationClass locationEdit;
    boolean unsavedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_edit);

        rolesList = findViewById(R.id.rolesView);
        locationName = findViewById(R.id.locationText);
        descriptionName = findViewById(R.id.descriptionText);


        locationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                unsavedLocation = true;
            }
        });
        descriptionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                unsavedLocation = true;
            }
        });

        rolesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RoleClass role = locationEdit.getRolesList().get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(LocationEdit.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.role_edit, parent, false);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                EditText roleView = dialogView.findViewById(R.id.roleEdit);
                roleView.setText(role.name);
                EditText weightView = dialogView.findViewById(R.id.weightEdit);
                weightView.setText(String.valueOf(role.weight));
                CheckBox onlyOne = dialogView.findViewById(R.id.onlyOneBox);
                onlyOne.setChecked(role.unique);
                Button saveButton = dialogView.findViewById(R.id.saveButton);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        role.name = roleView.getText().toString();
                        role.weight = Integer.valueOf(weightView.getText().toString());
                        role.unique = onlyOne.isChecked();
                        unsavedLocation = true;
                        rolesList.invalidateViews();
                        dialog.dismiss();
                    }
                });
                Button cancelButton = dialogView.findViewById(R.id.cancelButton);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

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

    public void onSaveLocation(View view)
    {
        if(unsavedLocation)
        {
            if(locationEdit.getRolesList().size()==0)
            {
                alertEmptyRoles();
            }
            else
            {
                locationEdit.setName(locationName.getEditableText().toString());
                locationEdit.setDescription(descriptionName.getEditableText().toString());
                if(GetGameModel().getEditedLocationIndex() < GetGameModel().getLocationCount())
                {
                    GetGameModel().changeLocation(this, GetGameModel().getEditedLocationIndex());
                }
                else
                {
                    GetGameModel().addLocation(this,locationEdit);
                }
                unsavedLocation = false;

                Intent intent = new Intent(this, LocationActivity.class);
                startActivity(intent);
            }
        }
    }

    private void alertEmptyRoles()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_alert, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        TextView titleText = dialogView.findViewById(R.id.textTitle);
        titleText.setText("Предупреждение");
        TextView msgText = dialogView.findViewById(R.id.textMessage);
        msgText.setText(getResources().getString(R.string.warning_add_role));
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
        if(unsavedLocation || locationEdit.getRolesList().size() == 0)
        {
            if(unsavedLocation)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.custom_alert, null);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                TextView titleText = dialogView.findViewById(R.id.textTitle);
                titleText.setText("Предупреждение");
                TextView msgText = dialogView.findViewById(R.id.textMessage);
                msgText.setText(getResources().getString(R.string.warning_unsaved_role));
                Button okButton = dialogView.findViewById(R.id.okButton);
                okButton.setText("Вернуться");
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button cancelButton = dialogView.findViewById(R.id.cancelButton);
                boolean savedRolesExist = false;
                for(int i = 0; i < locationEdit.getRolesList().size(); i+=1)
                {
                    if (locationEdit.getRolesList().get(i).roleID != -1)
                    {
                        savedRolesExist = true;
                        break;
                    }
                }

                if(savedRolesExist)
                {
                    cancelButton.setText("Выйти без сохранения");
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int i = 0;
                            while(i < locationEdit.getRolesList().size())
                            {
                                if (locationEdit.getRolesList().get(i).roleID == -1)
                                {
                                    locationEdit.getRolesList().remove(i);
                                }
                                else
                                {
                                    i+=1;
                                }
                            }
                            dialog.dismiss();
                            Intent intent = new Intent(LocationEdit.this, LocationActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    cancelButton.setVisibility(View.INVISIBLE);
                }
                dialog.show();
            }
            else
            {
                alertEmptyRoles();
            }
        }
        else
        {
            Intent intent = new Intent(this, LocationActivity.class);
            startActivity(intent);
        }

    }
    public void onAddRole(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LocationEdit.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.role_edit, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        EditText roleView = dialogView.findViewById(R.id.roleEdit);
        roleView.setText("Роль");
        EditText weightView = dialogView.findViewById(R.id.weightEdit);
        weightView.setText(String.valueOf(3));
        CheckBox onlyOne = dialogView.findViewById(R.id.onlyOneBox);
        onlyOne.setChecked(false);
        TextView titleText = dialogView.findViewById(R.id.textTitle);
        titleText.setText("Добавить роль");
        Button saveButton = dialogView.findViewById(R.id.saveButton);
        saveButton.setText("Добавить");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationEdit.addRole(
                        roleView.getText().toString(),
                        Integer.valueOf(weightView.getText().toString()),
                        onlyOne.isChecked()
                );
                unsavedLocation = true;
                adapter.notifyDataSetChanged();
                rolesList.invalidateViews();
                dialog.dismiss();
            }
        });
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateEditedLocation();
    }
    private void updateEditedLocation()
    {
        if(GetGameModel().getEditedLocationIndex() < GetGameModel().getLocationCount())
        {
            locationEdit = GetGameModel().getEditedLocation();
            locationName.setText(locationEdit.getName());
            descriptionName.setText(locationEdit.getDescription());
            unsavedLocation = false;
        }
        else
        {
            locationEdit = new LocationClass();
            locationEdit.setName(getResources().getString(R.string.location_label));
            locationEdit.setDescription(getResources().getString(R.string.description_label));
            locationEdit.setUse(true);
            locationName.setText(locationEdit.getName());
            descriptionName.setText(locationEdit.getDescription());
            unsavedLocation = true;
        }
        adapter = new RoleAdapter( this, locationEdit);
        rolesList.setAdapter(adapter);
    }
}
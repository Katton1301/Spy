package com.katton.spy;

import static com.katton.spy.GameModel.GetGameModel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class LocationAdapter extends ArrayAdapter<LocationClass> {
    private Context context;

    public LocationAdapter(Context context)
    {
        super(context, R.layout.location_item, GetGameModel().getLocations());
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.location_item, parent, false);
        LocationClass location = GetGameModel().getLocationByIndex(position);
        TextView name = view.findViewById(R.id.locationViewItem);
        name.setText(location.getName());
        CheckBox useBox = view.findViewById(R.id.useCheckBox);
        useBox.setChecked(location.isUsed());
        useBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useBox.isChecked()) {
                    location.setUse(true);
                    GetGameModel().changeLocation(context, position);
                }
                else
                {
                    if(GetGameModel().anyUsedLocationExist(position))
                    {
                        location.setUse(false);
                        GetGameModel().changeLocation(context, position);
                    }
                    else
                    {
                        useBox.setChecked(true);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View dialogView = inflater.inflate(R.layout.custom_alert, null);
                        builder.setView(dialogView);
                        AlertDialog dialog = builder.create();
                        TextView titleText = dialogView.findViewById(R.id.textTitle);
                        titleText.setText("Предупреждение");
                        TextView msgText = dialogView.findViewById(R.id.textMessage);
                        msgText.setText("Должна использоваться хотя бы одна локация!");
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

                }
            }
        });
        ImageButton imageButton = view.findViewById(R.id.deleteButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View dialogView = inflater.inflate(R.layout.custom_alert, null);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                if(GetGameModel().anyUsedLocationExist(position))
                {
                    TextView titleText = dialogView.findViewById(R.id.textTitle);
                    titleText.setText("Подтверждение действия");
                    TextView msgText = dialogView.findViewById(R.id.textMessage);
                    String message = "Вы действительно хотите удалить локацию " + location.getName() + "?";
                    msgText.setText(message);
                    Button okButton = dialogView.findViewById(R.id.okButton);
                    okButton.setText("Да");
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetGameModel().deleteLocation(context, position);
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    Button cancelButton = dialogView.findViewById(R.id.cancelButton);
                    cancelButton.setText("Нет");
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                else
                {
                    TextView titleText = dialogView.findViewById(R.id.textTitle);
                    titleText.setText("Предупреждение");
                    TextView msgText = dialogView.findViewById(R.id.textMessage);
                    msgText.setText("Нельзя удалить единственную выбранную локацию!");
                    Button okButton = dialogView.findViewById(R.id.okButton);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button cancelButton = dialogView.findViewById(R.id.cancelButton);
                    cancelButton.setVisibility(View.INVISIBLE);
                }
                dialog.show();
            }
        });
        return view;
    }

}

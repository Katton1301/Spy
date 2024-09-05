package com.katton.spy;

import static com.katton.spy.GameModel.GetGameModel;

import android.content.Context;
import android.content.DialogInterface;
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

public class RoleAdapter extends ArrayAdapter<RoleClass> {
    private Context context;
    private LocationClass locationClass;
    public RoleAdapter(Context context, LocationClass locationClass)
    {
        super(context, R.layout.role_item, locationClass.getRolesList());
        this.locationClass = locationClass;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.role_item, parent, false);
        RoleClass role = locationClass.getRolesList().get(position);

        TextView name = view.findViewById(R.id.roleViewItem);
        name.setText(role.name);
        TextView weight = view.findViewById(R.id.weightViewItem);
        weight.setText(String.valueOf(role.weight));
        CheckBox useBox = view.findViewById(R.id.onlyOneCheckBox);
        useBox.setChecked(role.unique);
        ImageButton imageButton = view.findViewById(R.id.deleteButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View dialogView = inflater.inflate(R.layout.custom_alert, null);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                TextView titleText = dialogView.findViewById(R.id.textTitle);
                titleText.setText("Подтверждение действия");
                TextView msgText = dialogView.findViewById(R.id.textMessage);
                String message = "Вы действительно хотите удалить роль " + role.name + "?";
                msgText.setText(message);
                Button okButton = dialogView.findViewById(R.id.okButton);
                okButton.setText("Да");
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetGameModel().deleteRole(context, GetGameModel().getEditedLocationIndex(), position);
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
                dialog.show();

            }
        });
        return view;
    }
}

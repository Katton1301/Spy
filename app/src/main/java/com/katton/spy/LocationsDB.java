package com.katton.spy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

public class LocationsDB extends SQLiteOpenHelper {

    public LocationsDB(Context context)
    {
        super( context, "Locations", null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE locations (id NUMBER, name TEXT, description TEXT, use TEXT);");
        db.execSQL("CREATE TABLE roles (id NUMBER, locationID NUMBER, name TEXT, weight NUMBER, only_one BOOLEAN);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS locations;");
        db.execSQL("DROP TABLE IF EXISTS roles;");
        onCreate(db);
    }

    public LocationsDB writeLocationsToDB( ArrayList<LocationClass> locationList )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        MessageFormat fmtLocation = new MessageFormat("INSERT INTO locations VALUES ({0}, \"{1}\", \"{2}\", \"{3}\");");
        MessageFormat fmtRoles = new MessageFormat("INSERT INTO roles VALUES ({0}, {1}, \"{2}\", {3}, \"{4}\");");
        for(int i = 0; i < locationList.size(); i+=1)
        {
            locationList.get(i).setDBID(i);
            Object[] argsLocation = {
                    i,
                    locationList.get(i).getName(),
                    locationList.get(i).getDescription(),
                    locationList.get(i).isUsed() ? "true" : "false"
            };
            db.execSQL(fmtLocation.format(argsLocation));
            ArrayList<RoleClass> rolesList = locationList.get(i).getRolesList();
            for(int j = 0; j < rolesList.size(); j+=1) {
                rolesList.get(j).roleID = j;
                Object[] argsRoles = {
                        j,
                        i,
                        rolesList.get(j).name,
                        rolesList.get(j).weight,
                        rolesList.get(j).unique ? "true" : "false"
                };
                db.execSQL(fmtRoles.format(argsRoles));
            }
        }
        return this;
    }
    public LocationsDB addLocation(LocationClass location)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int locationId = createNewLocationID();
        location.setDBID(locationId);
        MessageFormat fmtLocation = new MessageFormat("INSERT INTO locations VALUES ({0}, \"{1}\", \"{2}\", \"{3}\");");
        Object[] argsLocation = {
                locationId,
                location.getName(),
                location.getDescription(),
                location.isUsed() ? "true" : "false"
        };
        db.execSQL(fmtLocation.format(argsLocation));
        for(int i = 0; i < location.getRolesList().size(); i+=1)
        {
            if(location.getRolesList().get(i).roleID == -1)
            {
                addRole(location.getDBID(),location.getRolesList().get(i));
            }
            else
            {
                changeRole(location.getDBID(),location.getRolesList().get(i));
            }
        }
        return this;
    }

    public LocationsDB addRole( int locationID, RoleClass role)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int roleId = createNewRoleID();
        role.roleID = roleId;
        MessageFormat fmtRoles = new MessageFormat("INSERT INTO roles VALUES ({0}, {1}, \"{2}\", {3}, \"{4}\");");
        Object[] argsRoles = {
                roleId,
                locationID,
                role.name,
                role.weight,
                role.unique ? "true" : "false"
        };
        db.execSQL(fmtRoles.format(argsRoles));
        return this;
    }

    public LocationsDB changeLocation(LocationClass location)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        MessageFormat fmtLocation = new MessageFormat("UPDATE locations SET name=\"{1}\", description=\"{2}\", use=\"{3}\" WHERE id={0};");
        Object[] argsLocation = {
                location.getDBID(),
                location.getName(),
                location.getDescription(),
                location.isUsed() ? "true" : "false"
        };
        db.execSQL(fmtLocation.format(argsLocation));
        for(int i = 0; i < location.getRolesList().size(); i+=1)
        {
            if(location.getRolesList().get(i).roleID == -1)
            {
                addRole(location.getDBID(),location.getRolesList().get(i));
            }
            else
            {
                changeRole(location.getDBID(),location.getRolesList().get(i));
            }
        }
        return this;
    }

    public LocationsDB changeRole(int locationID, RoleClass role)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        MessageFormat fmtRole = new MessageFormat("UPDATE roles SET name=\"{2}\", weight={3}, only_one=\"{4}\" WHERE id={0} AND locationID={1};");
        Object[] argsRole = {
                role.roleID,
                locationID,
                role.name,
                role.weight,
                role.unique ? "true" : "false"
        };
        db.execSQL(fmtRole.format(argsRole));
        return this;
    }

    public LocationsDB deleteRole(int locationID, RoleClass role)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        MessageFormat fmtRoles = new MessageFormat("DELETE FROM roles WHERE id={0} AND locationID={1};");
        Object[] argsRole = {
                role.roleID,
                locationID
        };
        db.execSQL(fmtRoles.format(argsRole));
        return this;
    }

    public LocationsDB deleteLocation(LocationClass location)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        MessageFormat fmtLocation = new MessageFormat("DELETE FROM locations WHERE id={0};");
        MessageFormat fmtRoles = new MessageFormat("DELETE FROM roles WHERE locationID={0};");
        Object[] argsLocation = {
                location.getDBID()
        };
        db.execSQL(fmtLocation.format(argsLocation));
        db.execSQL(fmtRoles.format(argsLocation));
        return this;
    }

    private int createNewLocationID()
    {
        int locationID = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) as id FROM locations;", null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            locationID = cursor.getInt(0);
            cursor.close();
            MessageFormat fmtLocation = new MessageFormat("SELECT * FROM locations WHERE id={0};");
            Object[] argsLocation = { locationID };
            cursor = db.rawQuery(fmtLocation.format(argsLocation), null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                cursor.close();
                locationID += 1;
                Object[] args = { locationID };
                cursor = db.rawQuery(fmtLocation.format(args), null);
                cursor.moveToFirst();
            }
            cursor.close();
        }
        return locationID;
    }

    private int createNewRoleID()
    {
        int roleID = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) as id FROM roles;", null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            roleID = cursor.getInt(0);
            cursor.close();
            MessageFormat fmtRole = new MessageFormat("SELECT * FROM roles WHERE id={0};");
            Object[] argsRole = {
                roleID
            };
            cursor = db.rawQuery(fmtRole.format(argsRole), null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                cursor.close();
                roleID += 1;
                Object[] args = { roleID };
                cursor = db.rawQuery(fmtRole.format(args), null);
                cursor.moveToFirst();
            }
            cursor.close();
        }
        return roleID;
    }
}

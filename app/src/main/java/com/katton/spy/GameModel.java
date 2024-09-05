package com.katton.spy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameModel {
    enum TGameStage
    {
        INITIALIZED,
        PREPARE,
        PROCESS,
        STOP_SPY_ANSWER,
        STOP_VOTE,
        TIME_OUt,
        END
    }
    private static GameModel model;
    int playersCount;
    int spyCount;
    int gameTime;
    boolean randomSpyNumber;
    TGameStage gameStage;
    int readyPlayers;
    int firstPlayerIndex;
    ArrayList<LocationClass> locationList;
    ArrayList<PlayerClass> playersList;
    int gameLocationIndex;
    Boolean gameResult;
    Boolean locationsLoaded;
    int editedLocationIndex;

    private GameModel()
    {
        locationList = new ArrayList<>();
        initialize();
    }
    public static synchronized GameModel GetGameModel()
    {
        if(model == null)
        {
            model = new GameModel();
            model.setLocationsLoaded(false);
        }
        return  model;
    }

    public void saveGameSettings(SharedPreferences preferences)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString( "GAME_TIME", String.valueOf(getGameTime() / 60));
        editor.putString( "PLAYER_COUNT", String.valueOf(getPlayersCount()));
        editor.putString( "SPY_COUNT", String.valueOf(getSpyCount()));
        editor.commit();
    }
    public void LoadBaseLocationDataBase(Context context)
    {
        XmlResourceParser parser = context.getResources().getXml(R.xml.locations);
        try
        {
            LocationClass location = new LocationClass();
            while( parser.getEventType() != XmlResourceParser.END_DOCUMENT)
            {
                if (parser.getEventType() == XmlResourceParser.START_TAG) {

                    if(parser.getName().equals("Location"))
                    {
                        String locationName = parser.getAttributeValue(null, "name");
                        location.setName(locationName);
                        String description = parser.getAttributeValue(null, "description");
                        location.setDescription(description);
                        String useString = parser.getAttributeValue(null, "use");
                        location.setUse(useString.equals("true"));
                        location.clearRoleList();
                    }
                    if(parser.getName().equals("Role"))
                    {
                        String roleName = parser.getAttributeValue(null, "name");
                        int roleWeight = Integer.parseInt(parser.getAttributeValue(null, "weight"));
                        Boolean unique = parser.getAttributeValue(null, "only_one").equals("true");
                        location.addRole(roleName, roleWeight, unique);
                    }
                }
                if (parser.getEventType() == XmlResourceParser.END_TAG)
                {
                    if(parser.getName().equals("Location") && location != null)
                    {
                        LocationClass copiedLocation = new LocationClass(location);
                        locationList.add(copiedLocation);
                    }
                }
                parser.next();
            }
        } catch (XmlPullParserException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void readLocationsDB(Context context)
    {
        if(!getLocationsLoaded()) {
            SQLiteDatabase database = new LocationsDB(context).getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT * FROM locations;", null);
            cursor.moveToFirst();
            if (cursor.isAfterLast()) {
                cursor.close();
                LoadBaseLocationDataBase(context);
                new LocationsDB(context).writeLocationsToDB(locationList);
            } else {
                MessageFormat fmtRoles = new MessageFormat("SELECT * FROM roles WHERE locationID={0};");
                while (!cursor.isAfterLast()) {
                    LocationClass location = new LocationClass();
                    location.setName(cursor.getString(1));
                    location.setDescription(cursor.getString(2));
                    location.setUse(cursor.getString(3).equals("true"));
                    int locationId = cursor.getInt(0);
                    location.setDBID(locationId);
                    Object[] args = {locationId};
                    Cursor cursorRoles = database.rawQuery(fmtRoles.format(args), null);
                    cursorRoles.moveToFirst();
                    while (!cursorRoles.isAfterLast()) {
                        int roleID = cursorRoles.getInt(0);
                        String roleName = cursorRoles.getString(2);
                        int roleWeight = cursorRoles.getInt(3);
                        Boolean unique = cursorRoles.getString(4).equals("true");
                        location.addRole(roleName, roleWeight, unique);
                        location.getRolesList().get(location.getRolesList().size()-1).roleID = roleID;
                        cursorRoles.moveToNext();
                    }
                    cursorRoles.close();
                    cursor.moveToNext();
                    locationList.add(location);
                }
                cursor.close();
            }
            setLocationsLoaded(true);
        }
    }
    public Boolean getGameResult() {
        return gameResult;
    }
    public void setGameResult(Boolean gameResult) {
        this.gameResult = gameResult;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public void setSpyCount(int spyCount) {
        this.spyCount = spyCount;
    }

    public int getSpyCount() {
        return spyCount;
    }

    public void setRandomSpyNumber(boolean randomSpyNumber) {
        this.randomSpyNumber = randomSpyNumber;
    }
    public boolean isRandomSpyNumber() {
        return randomSpyNumber;
    }

    public TGameStage getGameStage() {
        return gameStage;
    }
    public void setGameStage(TGameStage gameStage) {
        this.gameStage = gameStage;
    }

    public int getReadyPlayers() {
        return readyPlayers;
    }

    public int getFirstPlayerIndex() {
        return firstPlayerIndex;
    }

    public void setFirstPlayerIndex(int firstPlayerIndex) {
        this.firstPlayerIndex = firstPlayerIndex;
    }

    public int getGameLocationIndex() {
        return gameLocationIndex;
    }

    public String getLocationName()
    {
        return this.locationList.get(gameLocationIndex).getName();
    }
    public String getLocationDescription()
    {
        return this.locationList.get(gameLocationIndex).getDescription();
    }

    public void dontUseGameLocation(Context context)
    {
        locationList.get(gameLocationIndex).setUse(false);
        changeLocation(context,gameLocationIndex);
    }

    public Boolean getLocationsLoaded() {
        return locationsLoaded;
    }

    public void setLocationsLoaded(Boolean locationsLoaded) {
        this.locationsLoaded = locationsLoaded;
    }
    public void setEditedLocationIndex(int editedLocationIndex) {
        this.editedLocationIndex = editedLocationIndex;
    }

    public int getEditedLocationIndex() {
        return editedLocationIndex;
    }

    public LocationClass getEditedLocation() {
        return locationList.get(editedLocationIndex);
    }

    public void generateGame(String spyString)
    {
        if(this.gameStage == TGameStage.INITIALIZED)
        {
            Random rand = new Random();
            ArrayList<Integer> locationIndexList = new ArrayList<>();
            for(int i = 0; i < this.locationList.size(); i+=1)
            {
                if(this.locationList.get(i).use)
                {
                    locationIndexList.add(i);
                }
            }

            int randIndexLocation = rand.nextInt(locationIndexList.size());
            this.gameLocationIndex = locationIndexList.get(randIndexLocation);
            LocationClass gameLocation = new LocationClass(this.locationList.get(gameLocationIndex));
            int spyNumber = this.spyCount;
            if(isRandomSpyNumber())
            {
                spyNumber = 1;
                int weight = this.playersCount - 1;
                for(int i = 0; i < this.playersCount - 2; i+=1)
                {
                    int randWeight = rand.nextInt(weight);
                    if(randWeight == 0)
                    {
                        spyNumber += 1;
                        weight += 1;
                    }
                }
                setSpyCount(spyNumber);
            }

            this.playersList = new ArrayList<>();
            for(int i = 0; i < spyNumber; i+=1)
            {
                this.playersList.add( new PlayerClass( "Игрок", spyString,  true));
            }
            while(this.playersList.size() < this.playersCount)
            {
                int weightSum = 0;
                for(int i = 0; i < gameLocation.getRolesList().size(); i+=1)
                {
                    weightSum += gameLocation.getRolesList().get(i).weight;
                }
                int randWeight = rand.nextInt(weightSum);
                for(int i = 0; i < gameLocation.getRolesList().size(); i+=1)
                {
                    if(randWeight < gameLocation.getRolesList().get(i).weight)
                    {
                        this.playersList.add( new PlayerClass("Игрок", gameLocation.getRolesList().get(i).name, false));
                        if(gameLocation.getRolesList().get(i).unique)
                        {
                            gameLocation.getRolesList().remove(i);
                        }
                        break;
                    }
                    randWeight -= gameLocation.getRolesList().get(i).weight;
                }
            }
            firstPlayerIndex = rand.nextInt(this.playersList.size());
            Collections.shuffle(this.playersList);
            for(int i = 1; i <= this.playersList.size(); i+=1)
            {
                this.playersList.get(i - 1).setName("Игрок " + i);
            }
            this.gameStage = TGameStage.PREPARE;
        }
    }

    public ArrayList<String> getLocationNames()
    {
        ArrayList<String> locationNames = new ArrayList<>();
        for(int i = 0; i < locationList.size(); i+=1)
        {
            locationNames.add(locationList.get(i).getName());
        }
        return locationNames;
    }

    public int getLocationCount()
    {
        return locationList.size();
    }

    public ArrayList<LocationClass> getLocations()
    {
        return locationList;
    }

    public LocationClass getLocationByIndex(int index)
    {
        return locationList.get(index);
    }


    public String getCurrentPlayerName()
    {
        return this.playersList.get(readyPlayers).getName();
    }
    public PlayerClass getNextPlayer()
    {
        PlayerClass player = this.playersList.get(readyPlayers);
        readyPlayers += 1;
        return player;
    }
    public boolean anyUsedLocationExist(int position) {
        boolean anyUsedLocationExist = false;
        for(int i = 0; i < locationList.size(); i+=1)
        {
            if(i != position && locationList.get(i).isUsed())
            {
                anyUsedLocationExist = true;
                break;
            }
        }
        return anyUsedLocationExist;
    }

    public ArrayList<PlayerClass> getPlayersList() {
        return playersList;
    }

    public String getFirstPlayerName()
    {
        return playersList.get(firstPlayerIndex).getName();
    }

    public void addLocation(Context context, LocationClass location)
    {
        new LocationsDB(context).addLocation(location);
        locationList.add(location);
    }
    public void changeLocation(Context context, int locIndex)
    {
        new LocationsDB(context).changeLocation(locationList.get(locIndex));
    }
    public void deleteLocation(Context context, int locIndex)
    {
        new LocationsDB(context).deleteLocation(locationList.get(locIndex));
        locationList.remove(locIndex);
    }
    public void addRole(Context context, int locIndex, RoleClass role)
    {
        LocationClass location = locationList.get(locIndex);
        new LocationsDB(context).addRole(location.getDBID(),role);
        location.addRole(role.name, role.weight, role.unique);
        location.getRolesList().get(location.getRolesList().size()-1).roleID = role.roleID;
    }
    public void changeRole(Context context, int locIndex, int roleIndex)
    {
        LocationClass location = locationList.get(locIndex);
        new LocationsDB(context).changeRole(location.getDBID(),location.getRolesList().get(roleIndex));
    }
    public void deleteRole(Context context, int locIndex, int roleIndex)
    {
        LocationClass location = locationList.get(locIndex);
        new LocationsDB(context).deleteRole(location.getDBID(),location.getRolesList().get(roleIndex));
        location.getRolesList().remove(roleIndex);
    }
    public void initialize()
    {
        readyPlayers = 0;
        gameStage = TGameStage.INITIALIZED;
    }
}

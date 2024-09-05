package com.katton.spy;

import java.util.ArrayList;

public class LocationClass
{
    String name;
    String description;
    Boolean use;
    int DBID;
    ArrayList<RoleClass> rolesList;

    public LocationClass()
    {
        rolesList = new ArrayList<>();
    }
    public LocationClass( LocationClass another )
    {
        this.name = another.name;
        this.description = another.description;
        this.use = another.use;
        this.DBID = another.DBID;
        this.rolesList = new ArrayList<>();
        for(int i = 0; i < another.rolesList.size(); i+=1)
        {
            this.rolesList.add(new RoleClass(another.rolesList.get(i)));
        }

    }

    public void addRole( String name, Integer weight, Boolean unique )
    {
        this.rolesList.add( new RoleClass(name, weight, unique));
    }

    public void clearRoleList()
    {
        this.rolesList.clear();
    }

    public ArrayList<RoleClass> getRolesList() {
        return rolesList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUse(Boolean use) {
        this.use = use;
    }
    public boolean isUsed()
    {
        return  this.use;
    }

    public void setDBID(int DBID) {
        this.DBID = DBID;
    }
    public int getDBID() {
        return DBID;
    }
}


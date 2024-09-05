package com.katton.spy;

public class RoleClass
{
    String name;
    Integer weight;
    Boolean unique;

    Integer roleID;

    public  RoleClass(RoleClass another)
    {
        this.name = another.name;
        this.weight = another.weight;
        this.unique = another.unique;
        this.roleID = another.roleID;
    }
    public RoleClass( String _name, Integer _weight, Boolean _unique )
    {
        name = _name;
        weight = _weight;
        unique = _unique;
        roleID = -1;
    }
}

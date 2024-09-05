package com.katton.spy;

public class PlayerClass {
    String name;
    String role;
    Boolean isSpy;

    PlayerClass( String name, String role, Boolean isSpy )
    {
        this.name = name;
        this.role = role;
        this.isSpy = isSpy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isSpy() {
        return isSpy;
    }

    public void setSpy(Boolean spy) {
        isSpy = spy;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

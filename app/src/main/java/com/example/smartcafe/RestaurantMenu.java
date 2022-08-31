package com.example.smartcafe;

public class RestaurantMenu
{
    private String menuID;
    private String resID;

    public RestaurantMenu(String menuID, String resID)
    {
        this.menuID = menuID;
        this.resID = resID;
    }
    public String getResID() {
        return resID;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

}

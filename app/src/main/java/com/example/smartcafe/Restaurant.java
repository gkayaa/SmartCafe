package com.example.smartcafe;

public class Restaurant
{
    private String resID;
    private String resMenu;
    private String resName;
    private double resLongitude;
    private double resLatitude;
    private double resRate;

    public double getResRate() {
        return resRate;
    }

    public void setResRate(double resRate) {
        this.resRate = resRate;
    }

    public Restaurant() {}

    public Restaurant(String resID, String resMenu, String resName, double resLatitude, double resLongitude)
    {
        this.resID = resID;
        this.resMenu = resMenu;
        this.resName = resName;
        this.resLongitude = resLongitude;
        this.resLatitude = resLatitude;
    }
    public String getResID() {
        return resID;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public String getResMenu() {
        return resMenu;
    }

    public void setResMenu(String resMenu) {
        this.resMenu = resMenu;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public double getResLongitude() {
        return resLongitude;
    }

    public void setResLongitude(double resLongitude) {
        this.resLongitude = resLongitude;
    }

    public double getResLatitude() {
        return resLatitude;
    }

    public void setResLatitude(double resLatitude) {
        this.resLatitude = resLatitude;
    }
}

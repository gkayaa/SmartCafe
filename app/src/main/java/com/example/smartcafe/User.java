package com.example.smartcafe;

public class User
{
    private String name;
    private String dateOfBirth;
    private String Phone;
    private String Email;
    private String Password;
    private String userID;
    private boolean isVerified;
    private double longitude;
    private double latitude;
    private double userBalance;
    private String currentRestaurant;
    private String resTable;

    public String getResTable() {
        return resTable;
    }

    public void setResTable(String resTable) {
        this.resTable = resTable;
    }

    public String getCurrentRestaurant() {
        return currentRestaurant;
    }

    public void setCurrentRestaurant(String currentRestaurant) {
        this.currentRestaurant = currentRestaurant;
    }

    public double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(double userBalance) {
        this.userBalance = userBalance;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longtitude) {
        this.longitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userrID) {
        userID = userrID;
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

}

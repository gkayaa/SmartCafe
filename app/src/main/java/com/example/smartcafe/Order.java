package com.example.smartcafe;

import java.util.ArrayList;
import java.util.HashMap;

public class Order
{
    private HashMap<String,FoodDrink> itemsOrdered;
    private double totalCost;
    private String restaurant;
    private String date;
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public HashMap<String, FoodDrink> getItemsOrdered() {
        return itemsOrdered;
    }

    public void setItemsOrdered(HashMap<String,FoodDrink> itemsOrdered)
    {
        this.itemsOrdered = itemsOrdered;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Order(HashMap<String,FoodDrink> itemsOrdered, double totalCost, String restaurant, String date)
    {
        this.itemsOrdered = itemsOrdered;
        this.totalCost = totalCost;
        this.restaurant = restaurant;
        this.date = date;
    }

    public Order()
    {}

}

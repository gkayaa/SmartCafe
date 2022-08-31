package com.example.smartcafe;

public class FoodDrink
{
    private String name;
    private Double cost;
    private String category;
    private String menuID;
    private String itemID;

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public FoodDrink(String name, Double cost, String category, String menuID, String itemID)
    {
        this.name = name;
        this.cost = cost;
        this.category = category;
        this.menuID = menuID;
        this.itemID = itemID;
    }
    public FoodDrink()
    {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

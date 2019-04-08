package com.example.fyp.Customer.AddItem;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemModel implements Serializable {

    private String itemName;
    private String quanhtity;
    private CircleImageView imageView;
    private String description;

    public ItemModel(String itemName, String quantity, String description) {
        this.itemName = itemName;
        this.quanhtity = quantity;
       // this.imageView = imageView;
        this.description = description;
    }

    public String getItemName() {
        return itemName;
    }

    public String getQuanhtity() {
        return quanhtity;
    }

    public CircleImageView getImageView() {
        return imageView;
    }

    public String getDescription() {
        return description;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setQuanhtity(String  quanhtity) {
        this.quanhtity = quanhtity;
    }

    public void setImageView(CircleImageView imageView) {
        this.imageView = imageView;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

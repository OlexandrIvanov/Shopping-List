package com.example.saa.shoppinglist.db;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity (tableName = "shoppingList")
public class PurchaseDBModel {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "purchase")
    private String purchase;

    @ColumnInfo(name = "ifBought")
    private boolean ifBought;



    @ColumnInfo(name = "imageURL")
    private String imageURL;

    public PurchaseDBModel() {
    }
    @Ignore
    public PurchaseDBModel(String purchase, boolean ifBought, String imageURL) {
        this.purchase = purchase;
        this.ifBought = ifBought;
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public boolean isIfBought() {
        return ifBought;
    }

    public void setIfBought(boolean ifBought) {
        this.ifBought = ifBought;
    }
    public String getImageURL() {
        return imageURL;
    }


    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return new StringBuilder(purchase).append("\n").append(imageURL).toString();
    }
}

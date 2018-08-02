package com.example.saa.shoppinglist.db;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;



public interface IPurchaseDataSource {

    Flowable<PurchaseDBModel> getPurchaseById(int purchaseId);

    Flowable<List<PurchaseDBModel>> getAllPurchase();


    void insertPurchase(PurchaseDBModel... purchaseDBModels);

    void updatePurchase(PurchaseDBModel... purchaseDBModels);

    void deletePurchase (PurchaseDBModel purchaseDBModels);

    void deleteAllPurchases();

}

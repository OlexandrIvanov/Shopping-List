package com.example.saa.shoppinglist.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;
import io.reactivex.Flowable;


@Dao
public interface PurchaseDao {

    @Query("SELECT * FROM shoppingList WHERE id=:purchaseId")
    Flowable<PurchaseDBModel> getPurchaseById(int purchaseId);

    @Query("SELECT * FROM shoppingList")
    Flowable<List<PurchaseDBModel>> getAllPurchase();


    @Insert
    void insertPurchase(PurchaseDBModel... purchaseDBModels);

    @Update
    void updetaPurchase (PurchaseDBModel... purchaseDBModels);

    @Delete
    void deletePurchase (PurchaseDBModel purchaseDBModels);

    @Query("DELETE FROM shoppingList")
    void deleteAllUsers();


}

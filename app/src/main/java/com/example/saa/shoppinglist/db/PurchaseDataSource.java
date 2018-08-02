package com.example.saa.shoppinglist.db;

import java.util.List;

import io.reactivex.Flowable;

public class PurchaseDataSource implements IPurchaseDataSource {

    private PurchaseDao purchaseDao;

    private static PurchaseDataSource mInstance;

    public PurchaseDataSource(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

    public static PurchaseDataSource getInstance(PurchaseDao purchaseDao){
        if (mInstance ==null){
            mInstance = new PurchaseDataSource(purchaseDao);
        }
        return mInstance;
    }


    @Override
    public Flowable<PurchaseDBModel> getPurchaseById(int purchaseId) {
        return purchaseDao.getPurchaseById(purchaseId);
    }

    @Override
    public Flowable<List<PurchaseDBModel>> getAllPurchase() {
        return purchaseDao.getAllPurchase();
    }

    @Override
    public void insertPurchase(PurchaseDBModel... purchaseDBModels) {
        purchaseDao.insertPurchase(purchaseDBModels);
    }

    @Override
    public void updatePurchase(PurchaseDBModel... purchaseDBModels) {
        purchaseDao.updetaPurchase(purchaseDBModels);
    }

    @Override
    public void deletePurchase(PurchaseDBModel purchaseDBModels) {
        purchaseDao.deletePurchase(purchaseDBModels);
    }

    @Override
    public void deleteAllPurchases() {
        purchaseDao.deleteAllUsers();
    }
}

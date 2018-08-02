package com.example.saa.shoppinglist.db;

import java.util.List;

import io.reactivex.Flowable;

public class PurchaseRepository implements IPurchaseDataSource {

    private IPurchaseDataSource iPurchaseDataSource;

    private static PurchaseRepository mInstance;

    public PurchaseRepository(IPurchaseDataSource iPurchaseDataSource) {
        this.iPurchaseDataSource = iPurchaseDataSource;
    }

    public static PurchaseRepository getInstance (IPurchaseDataSource iPurchaseDataSource){

        if (mInstance == null){
            mInstance = new PurchaseRepository(iPurchaseDataSource);
        }
        return mInstance;
    }



    @Override
    public Flowable<PurchaseDBModel> getPurchaseById(int purchaseId) {
        return iPurchaseDataSource.getPurchaseById(purchaseId);
    }

    @Override
    public Flowable<List<PurchaseDBModel>> getAllPurchase() {
        return iPurchaseDataSource.getAllPurchase();
    }

    @Override
    public void insertPurchase(PurchaseDBModel... purchaseDBModels) {
        iPurchaseDataSource.insertPurchase(purchaseDBModels);
    }

    @Override
    public void updatePurchase(PurchaseDBModel... purchaseDBModels) {
        iPurchaseDataSource.updatePurchase(purchaseDBModels);
    }

    @Override
    public void deletePurchase(PurchaseDBModel purchaseDBModels) {
        iPurchaseDataSource.deletePurchase(purchaseDBModels);
    }

    @Override
    public void deleteAllPurchases() {
        iPurchaseDataSource.deleteAllPurchases();
    }
}

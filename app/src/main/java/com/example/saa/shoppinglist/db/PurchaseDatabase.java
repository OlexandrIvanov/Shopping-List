package com.example.saa.shoppinglist.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import org.w3c.dom.UserDataHandler;

import static com.example.saa.shoppinglist.db.PurchaseDatabase.DATABASE_VERSION;

@Database(entities = PurchaseDBModel.class, version = DATABASE_VERSION)

public abstract class PurchaseDatabase extends RoomDatabase {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Purchase-Database";

    public abstract PurchaseDao purchaseDao();

    private static PurchaseDatabase mInstance;

    public static PurchaseDatabase getInstance (Context context){
        if (mInstance ==null){
            mInstance = Room.databaseBuilder(context,PurchaseDatabase.class,DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }

}

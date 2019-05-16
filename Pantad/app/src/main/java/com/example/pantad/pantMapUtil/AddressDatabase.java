package com.example.pantad.pantMapUtil;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class AddressDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "gotbigbasic.db";
    private static final int DATABASE_VERSION = 1;

    public AddressDatabase(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
}

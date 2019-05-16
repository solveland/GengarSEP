package com.example.pantad.addresses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class AddressDatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "gotbigbasic.db";
    private static final int DATABASE_VERSION = 1;
    private final SQLiteDatabase db;
    private static AddressDatabaseHelper instance;


    private AddressDatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        db = getReadableDatabase();
    }

    public static AddressDatabaseHelper getInstance(Context context){
        if (instance == null){
            return new AddressDatabaseHelper(context);
        } else {
            return instance;
        }
    }

    public Cursor Autocomplete(CharSequence s){
        String text = (s == null)? "" : s.toString();
        int lastDivider = text.lastIndexOf(' ');
        String addressPart = (lastDivider >= 0)? text.substring(0,lastDivider): text;
        String numberPart = (lastDivider >= 0)? text.substring(lastDivider+1): "";
        Cursor c = db.rawQuery("SELECT * FROM addresses WHERE street LIKE '" + text + "%' or (street like '" + addressPart + "%' and housenumber like '" + numberPart + "%') order by housenumber limit 5 ",null);
        return c;

    }
}

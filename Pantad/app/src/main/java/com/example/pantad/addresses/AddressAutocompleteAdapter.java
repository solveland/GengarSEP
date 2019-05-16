package com.example.pantad.addresses;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.pantad.R;

public class AddressAutocompleteAdapter extends CursorAdapter {

    public AddressAutocompleteAdapter(Context context,Cursor c,int flags){
        super(context,c,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.address_autocomplete_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView addressText = view.findViewById(R.id.addressText);
        String address = cursor.getString(3) + " " + cursor.getString(5);
        addressText.setText(address);
    }

    @Override
    public CharSequence convertToString(Cursor cursor){
        return cursor.getString(3) + " " + cursor.getString(5);
    }
}

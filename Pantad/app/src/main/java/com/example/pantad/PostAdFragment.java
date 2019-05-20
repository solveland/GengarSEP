package com.example.pantad;


import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.TextView;

import com.example.pantad.addresses.AddressAutocompleteAdapter;
import com.example.pantad.addresses.AddressDatabaseHelper;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;


public class PostAdFragment extends DialogFragment {
    private TextView name;
    private AutoCompleteTextView address;
    private EditText value;
    private EditText message;
    private UserModel userModel;
    private Button submit;
    private String regID;
    private GeoPoint location;
    private boolean locationUpdated = false;
    private UserProfileModel upm;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        userModel= ViewModelProviders.of(getActivity()).get(UserModel.class);   //The userModel is a shared object between the fragments, it handles the communication between them

        upm = ViewModelProviders.of(getActivity()).get(UserProfileModel.class);

        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View postAdView = inflater.inflate(R.layout.fragment_post_ad, null);

        initInputFields(postAdView);
        initSubmit(postAdView);

        builder.setView(postAdView)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        name.setText(upm.getName());
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void initInputFields(View root) {
        name = root.findViewById(R.id.postAdName);
        address = root.findViewById(R.id.adressInput);
        value = root.findViewById(R.id.valueInput);
        message = root.findViewById(R.id.messageInput);




        // If you select an item from the dropdownbox we save the location of the selected address and mark it as updated.
        address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getItemAtPosition(position);
                location = new GeoPoint(c.getDouble(7),c.getDouble(6));
                locationUpdated = true;
                c.close();
            }
        });

        // If you freetype without selecting from the dropdown box we mark the location as not updated.
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locationUpdated = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // Makes it possible to scroll in the text editor (instead of scrolling the scrollview)
        message.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (message.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;

            }
        });

        View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };


        final AddressAutocompleteAdapter addressAutocompleteAdapter = new AddressAutocompleteAdapter(getContext(),null,0);
        address.setAdapter(addressAutocompleteAdapter);

        addressAutocompleteAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return AddressDatabaseHelper.getInstance(getContext()).Autocomplete(constraint);
            }
        });





        name.setOnFocusChangeListener(focusListener);
        address.setOnFocusChangeListener(focusListener);
        value.setOnFocusChangeListener(focusListener);
        message.setOnFocusChangeListener(focusListener);
    }

    /*
    Creates a reference to the submit button
    Also defines the onClick() method. Currently, a posting is added into the list (through userModel)
    if the input is valid.
     */
    private void initSubmit(View root){
        final String donatorID = upm.getUid();
        submit=root.findViewById(R.id.submitAdBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameInput=name.getText().toString();
                String addressInput=address.getText().toString();
                String valueInput=value.getText().toString();
                String messageInput=message.getText().toString();
                Timestamp startTime = Timestamp.now();
                if(nameInput.equals("") ||addressInput.equals("") || valueInput.equals("") || !locationUpdated){
                    Snackbar.make(submit, "You fucked up", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    addAds(nameInput, addressInput, Integer.parseInt(valueInput), messageInput, donatorID, startTime,location);
                    Snackbar.make(getActivity().findViewById(R.id.navigation), "AD was added", Snackbar.LENGTH_SHORT).show();
                    address.getText().clear();
                    value.getText().clear();
                    message.getText().clear();
                    dismiss();
                }
            }
        });
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }

/*
Adds another ad to the list of ads
@param name  The name of the person posting the ad
@param address The location where the trade will take place
@param estimatedValue The estimated value of the pant in whole SEK:s
*/

    public void addAds(String name, String adress, int value, String message, String donatorID, Timestamp startTime,GeoPoint location){

        userModel.addAd(name, adress, value, message, donatorID, startTime, regID,location);
    }

    private void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

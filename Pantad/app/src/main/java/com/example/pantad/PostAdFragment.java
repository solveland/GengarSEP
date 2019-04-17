package com.example.pantad;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * Handles the submitting of ads.
 */
public class PostAdFragment extends Fragment {
    //Relaterar till IV
    private Button submit;
    private EditText name;
    private EditText adress;
    private EditText value;
    private EditText message;
    private UserModel userModel;

    public PostAdFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_post_ad, container, false);
        userModel= ViewModelProviders.of(getActivity()).get(UserModel.class);

        initiatepostInputView(rootView);
        // Inflate the layout for this fragment
        return rootView;
    }

    /*
    Refrences the Editext fields and the submit button where all the user input comes from.
     */
    private void initiatepostInputView(View root){
        initInputfields(root);
        initSubmit(root);
    }
    private void initInputfields(View root ){
        name=root.findViewById(R.id.nameInput);
        adress=root.findViewById(R.id.adressInput);
        value=root.findViewById(R.id.valueInput);
        message = root.findViewById(R.id.messageInput);

    }

    /*
    Creates a reference to the submit button
    Also defines the onClick() method. Currently, a posting is added into the list (through userModel)
    if the input is valid.
     */
    private void initSubmit(View root){
        submit=root.findViewById(R.id.submitAdBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameInput=name.getText().toString();
                String adressInput=adress.getText().toString();
                String valueInput=value.getText().toString();
                String messageInput=message.getText().toString();

                if(nameInput.equals("") ||adressInput.equals("") || valueInput.equals("")){
                    Snackbar.make(submit, "You fucked up", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    addAnnons(nameInput, adressInput, Integer.parseInt(valueInput), messageInput);
                    Snackbar.make(submit, "AD was added", Snackbar.LENGTH_SHORT).show();
                    name.getText().clear();
                    adress.getText().clear();
                    value.getText().clear();
                    message.getText().clear();
                }
            }
        });
    }

    /*
Adds another annons to the list of annonser
@param name  The name of the person posting the ad
@param adress The location where the trade will take place
@param estimatedValue The estimated value of the pant in whole SEK:s
*/
    public void addAnnons(String name,String adress,int value, String message){
    userModel.addAnnons(name, adress, value, message);
    }
}

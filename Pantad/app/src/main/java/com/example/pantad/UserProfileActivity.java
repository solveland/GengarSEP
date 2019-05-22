package com.example.pantad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {

    private TextView name;
    private ImageView profilePic;
    private TextView email;
    private UserProfileModel upm;
    private EditText phoneNumber;
    private ImageButton messageBtn;
    private FloatingActionButton edit_button;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        profilePic = (ImageView) findViewById(R.id.profilePic);
        upm = (UserProfileModel) getIntent().getSerializableExtra("user profile model");
        phoneNumber = findViewById(R.id.profilePhone);

        edit_button = findViewById(R.id.edit_profile);
        ImageButton back_button = findViewById(R.id.profile_back_button);
        Button sign_out_button = findViewById(R.id.sign_out_button);

        messageBtn= findViewById(R.id.messageBtn);

        if(upm != null){
            updateProfile(upm.getUid());
            messageBtn.setVisibility(View.INVISIBLE);
        } else {
            edit_button.hide();
            sign_out_button.setVisibility(View.INVISIBLE);
            String uid = getIntent().getStringExtra("uid");
            updateProfile(uid);
        }


        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity(true);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity(false);
            }
        });
        edit_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!phoneNumber.isEnabled()){
                phoneNumber.setEnabled(true);
                phoneNumber.setSelected(true);
                phoneNumber.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                edit_button.setImageResource(R.drawable.ic_done_black_24dp);
            }
            else{
                upm.setPhoneNumber(phoneNumber.getText().toString());
                phoneNumber.setEnabled(false);
                edit_button.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            }

        }});

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("smsto:" + phoneNumber.getText())); // This ensures only SMS apps respond
                startActivity(intent);
            }
        });
    }

    public UserProfileActivity() {

    }

    public void updateProfile(String uid) {
        DocumentReference profileRef = db.collection("userProfile").document(uid);
        profileRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        name.setText(document.getString("name"));
                        email.setText(document.getString("email"));
                        String photoUrl = document.getString("photoUrl");
                        ImageLoader.loadImageFromUrl(photoUrl, profilePic, 300);
                        phoneNumber.setText(document.getString("phoneNumber"));
                    }
                } else {
                    Log.w("user profile", "error fetching user profile information");
                }

            }
        });
    }

    private void finishActivity(boolean signOut) {
        Intent result = new Intent();
        result.putExtra("signOutResult", signOut);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}

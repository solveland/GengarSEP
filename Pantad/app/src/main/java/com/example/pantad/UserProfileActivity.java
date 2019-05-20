package com.example.pantad;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    private TextView name;
    private ImageView profilePic;
    private TextView email;
    private UserProfileModel upm;
    private EditText phoneNumber;
    private Button edit_button;
    private Button save_button;
    private Button sign_out_button;

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

        edit_button = findViewById(R.id.edit_button);
        save_button = findViewById(R.id.save_button);
        sign_out_button = findViewById(R.id.sign_out_button);

        if(upm != null){
            updateProfile(upm.getUid());
        } else {
            edit_button.setVisibility(View.GONE);
            save_button.setVisibility(View.GONE);
            sign_out_button.setVisibility(View.GONE);
            String uid = getIntent().getStringExtra("uid");
            updateProfile(uid);
        }


        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        findViewById(R.id.profile_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        edit_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                phoneNumber.setEnabled(true);
                edit_button.setVisibility(View.GONE);
                save_button.setVisibility(View.VISIBLE);
            }
        });

        save_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                phoneNumber.setEnabled(false);
                save_button.setVisibility(View.GONE);
                edit_button.setVisibility(View.VISIBLE);
                upm.setPhoneNumber(phoneNumber.getText().toString());

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(upm != null){
            updateProfile(upm.getUid());
        } else {
            edit_button.setVisibility(View.GONE);
            save_button.setVisibility(View.GONE);
            sign_out_button.setVisibility(View.GONE);
            String uid = getIntent().getStringExtra("uid");
            updateProfile(uid);
        }
    }

    public UserProfileActivity() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2){
            if(resultCode == Activity.RESULT_OK){
                upm.setUid(data.getStringExtra("Uid"));

            }
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        launchLoginActivity();
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 2);
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
                        ImageLoader.loadImageFromUrl(document.getString("photoUrl"), profilePic);
                        phoneNumber.setText(document.getString("phoneNumber"));
                    }
                } else {
                    Log.w("user profile", "error fetching user profile information");
                }

            }
        });
    }

}

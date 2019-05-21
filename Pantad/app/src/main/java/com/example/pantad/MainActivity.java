package com.example.pantad;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import com.example.pantad.firebaseUtil.Config;
import com.example.pantad.firebaseUtil.NotificationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/*
Our main activity, which handles the different fragments and the navigationBar
Most of the processes should be handled by the fragments themselves, this activity only
initiates them and handles the menu navigation
 */
public class MainActivity extends AppCompatActivity implements PropertyChangeListener {

    PickupFragment pickupFrag;
    MapFragment mapFrag;
    DonatorFragment donatorFrag;
    UserProfileModel upm;
    UserModel userModel;
    TextView toolbar_title;
    ImageView profileButton;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtMessage;
    private static final String TAG = MainActivity.class.getSimpleName();

    /*
    Autogenerated for the most part, decides what to do when a navigationButton is pressed
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    setFragment(mapFrag);
                    toolbar_title.setText("Map");
                    return true;
                case R.id.navigation_pickups:
                    setFragment(pickupFrag);
                    toolbar_title.setText("Pickups");
                    return true;
                case R.id.navigation_donator:
                    setFragment(donatorFrag);
                    toolbar_title.setText("Donator");
                    return true;
            }
            return false;
        }
    };

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("TestChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createNotificationChannel();
        super.onCreate(savedInstanceState);

        pickupFrag = new PickupFragment();
        mapFrag = new MapFragment();
        donatorFrag = new DonatorFragment();
        createFragments();
        // Start login activity
        launchLoginActivity();
        txtMessage = (TextView) findViewById(R.id.txt_push_message);
        upm = ViewModelProviders.of(MainActivity.this).get(UserProfileModel.class);
        userModel = ViewModelProviders.of(MainActivity.this).get(UserModel.class);
        upm.setObserver(this);



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }
            }
        };

        //This is temporary, i'm printing the token in Logcat so i can see that it works
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e(TAG, "Firebase reg id: " + regId);
        donatorFrag.setRegID(regId);
        setContentView(R.layout.activity_main);
        // initiateRecycleView();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_map);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar_title = findViewById(R.id.toolbar_title);
        profileButton = findViewById(R.id.profile_button);


        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                launchProfileActivity();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2){
            if(resultCode == Activity.RESULT_OK){
                String uid = data.getStringExtra("Uid");
                upm.setUid(uid);
                upm.updateCurrentProfile();
                userModel.setProfileID(uid);
                userModel.updateAds();
            }
        } else if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra("signOutResult", false)) {
                    signOut();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /*
    Switches the currently active fragment
    @param fragment The fragment that should be set s active
     */
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    /*Initially creates the fragments (calling their onCreate() methods)
    Not needed but speeds up the menu-navigation.
    There is a trade-off between app boot-time and menu navigation smoothness
    */
    public void createFragments(){
        setFragment(donatorFrag);
        setFragment(pickupFrag);
        setFragment(mapFrag);
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 2);
    }
     private void launchProfileActivity(){
         Intent intent = new Intent(this, UserProfileActivity.class);
         intent.putExtra("user profile model", upm);
         startActivityForResult(intent, 3);
     }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        launchLoginActivity();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals("currentProfile")) {
            ImageLoader.loadImageFromUrl(upm.getPhotoUrl(), profileButton);
        }
    }

}

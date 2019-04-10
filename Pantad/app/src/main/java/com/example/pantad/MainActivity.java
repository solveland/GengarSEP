package com.example.pantad;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/*
Our main activity, which handles the different fragments and the navigationbar
Most of the processes should be handled by the fragments themselves, this activity only
initiates them and handles the menu navigation
 */
public class MainActivity extends AppCompatActivity {

    HomeFragment homeFrag;
    PostAdFragment postFrag;
    MapFragment mapFrag;

    /*
    Autogenerated for the most part, decides what to do when a navigationbutton is pressed
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(homeFrag);
                    return true;
                case R.id.navigation_postAd:
                    setFragment(postFrag);
                    return true;
                case R.id.navigation_map:
                    setFragment(mapFrag);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeFrag = new HomeFragment();
        postFrag = new PostAdFragment();
        mapFrag = new MapFragment();
        createFragments();
        setFragment(homeFrag);

        setContentView(R.layout.activity_main);
        // initiateRecycleView();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
        setFragment(postFrag);
        setFragment(mapFrag);
        setFragment(homeFrag);
    }
}

package com.example.myinstaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myinstaapp.Fragments.HomeFragment;
import com.example.myinstaapp.Fragments.NotificationFragment;
import com.example.myinstaapp.Fragments.ProfileFragment;
import com.example.myinstaapp.Fragments.SearchFrahment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView  btmNavView;
    private Fragment selectionFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btmNavView=findViewById(R.id.bottomnav);


        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeID:
                        selectionFragment=new HomeFragment();
                        break;
                    case R.id.personID:
                        selectionFragment=new ProfileFragment();
                        break;
                    case R.id.favoID:
                        selectionFragment=new NotificationFragment();
                        break;
                    case R.id.searchID:
                        selectionFragment=new SearchFrahment();
                        break;
                    case R.id.addID:
                        selectionFragment=null;
                        startActivity(new Intent(HomeActivity.this,PostActivity.class));
                        break;
                }
                if(selectionFragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectionFragment).commit();
                }

           return true; }
        });
        Bundle intent=getIntent().getExtras();
        if(intent!=null){
            String profileID= intent.getString("publisherID");
            getSharedPreferences("PROFILE",MODE_PRIVATE).edit().putString("profileID",profileID).apply();
    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
    btmNavView.setSelectedItemId(R.id.personID);
        }
     else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        }
    }


}
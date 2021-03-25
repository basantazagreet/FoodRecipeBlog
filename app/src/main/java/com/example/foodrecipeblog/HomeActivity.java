package com.example.foodrecipeblog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodrecipeblog.Fragments.AccountFragment;
import com.example.foodrecipeblog.Fragments.FavouritesFragment;
import com.example.foodrecipeblog.Fragments.HomeFragment;
import com.example.foodrecipeblog.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FloatingActionButton fab;
    private static final int GALLERY_ADD_POST = 2;
    private BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Buttom nav ma 4 wata fragments linked cha
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        //I added this if statement to keep the selected fragment when rotating the device,
        //Suru ma landing fragment ho Homefragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeContainer,
                    new HomeFragment()).commit();
        }

        init();


    }

    //onCreate() mai cha item selected listener ko object passed
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.item_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.item_favourites:
                            selectedFragment = new FavouritesFragment();
                            break;
                        case R.id.item_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.item_account:
                            selectedFragment = new AccountFragment();
                            break;
                    }
                    //Frame homecontainer vaneko fragment dekhine view ho
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeContainer,
                            selectedFragment).commit();
                    return true;
                }
            };


    //when Plus(Floating actionbar) is clicked, user is redirected to the smartphone gallery
    private void init() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, GALLERY_ADD_POST);
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ADD_POST && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            Intent i = new Intent(HomeActivity.this, AddRecipeActivity.class);
            i.setData(imgUri);
            startActivity(i);
        }
    }






}
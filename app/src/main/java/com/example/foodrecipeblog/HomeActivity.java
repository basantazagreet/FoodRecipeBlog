package com.example.foodrecipeblog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.foodrecipeblog.Fragments.AccountFragment;
import com.example.foodrecipeblog.Fragments.FavouritesFragment;
import com.example.foodrecipeblog.Fragments.HomeFragment;
import com.example.foodrecipeblog.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FloatingActionButton fab;
    private static final int GALLERY_ADD_POST = 2;
    private BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //4 items are listed in Buttomnav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //Suru ma landing fragment ho Homefragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeContainer,
                    new HomeFragment()).commit();
        }
        init();
    }

    //navlisteners are listed inside oncreate method
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
                    //Frame homecontainer is where fragments are replaced
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeContainer,
                            selectedFragment).commit();
                    return true;
                }
            };


    //when Plus(Floating actionbar) is clicked, user is redirected to the smartphone gallery if isLoggedIn
    private void init() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {

            SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            boolean isLoggedIn = userPref.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, GALLERY_ADD_POST);
            } else {
                Intent i = new Intent(HomeActivity.this, AuthActivity.class);
                Toast.makeText(getApplicationContext(), "Please login to upload recipe", Toast.LENGTH_LONG).show();
                startActivity(i);

            }
        });
    }

    //when add recipe is clicked, we need to send img uri data through intent to next activity
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





    //profile ma jada signin garna paros
    /*private BottomNavigationView.OnNavigationItemSelectedListener navListener2 =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    if (item.getItemId() == R.id.item_account) {
                        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                        boolean isLoggedIn = userPref.getBoolean("isLoggedIn", false);
                        if (isLoggedIn) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeContainer,
                                    new AccountFragment()).commit();
                        } else {
                            Intent i = new Intent(HomeActivity.this, AuthActivity.class);
                            Toast.makeText(getApplicationContext(), "Please signup first", Toast.LENGTH_LONG).show();
                            startActivity(i);

                        }

                    }
                    return true;
                }
            };*/
}
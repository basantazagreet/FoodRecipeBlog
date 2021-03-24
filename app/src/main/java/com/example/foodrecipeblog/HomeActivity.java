package com.example.foodrecipeblog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodrecipeblog.Fragments.HomeFragment;
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

        navigationView = findViewById(R.id.bottom_nav);

        //Suru ma landing in home fragment lai
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameHomeContainer, new HomeFragment()).commit();
        init();

    }

    //when Plus is clicked, user is redirected to the smartphone gallery
    private void init() {
        navigationView = findViewById(R.id.bottom_nav);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, GALLERY_ADD_POST);

        });

        //Pachi haleko code haru ho


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ADD_POST && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();

            //Bundle wala

           /* Bundle bundle = new Bundle();
            bundle.putString("IMAGE_URI", String.valueOf(imgUri));*/

            Intent i = new Intent(HomeActivity.this, AddRecipeActivity.class);
//            i.putExtras(bundle);
            //I think problem is here.
            i.setData(imgUri);
            startActivity(i);
        }
    }


}
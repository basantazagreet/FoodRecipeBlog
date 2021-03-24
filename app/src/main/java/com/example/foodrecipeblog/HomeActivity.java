package com.example.foodrecipeblog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodrecipeblog.Fragments.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Suru ma landing in home fragment lai
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameHomeContainer,new HomeFragment(), HomeFragment.class.getSimpleName()).commit();


    }


    public void gotoProfile(View v) {
        Intent i = new Intent(HomeActivity.this, UserInfoActivity.class);
        startActivity(i);
    }

    public void gotoAuthorization(View v) {
        Intent i = new Intent(HomeActivity.this, AuthActivity.class);
        startActivity(i);
    }



    //Logout function stops the program
    private void logout(View view) {


        StringRequest request = new StringRequest(Request.Method.POST, Constant.LOGOUT, response -> {

            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {

                    //get shared preference and reset token
                    SharedPreferences userPref = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token","");
                    editor.putString("name","");
                    editor.putInt("id",0);
                    editor.putString("lastname","");
                    editor.putString("photo","");
                    editor.putBoolean("isLoggedIn",false);
                    editor.apply();



                    startActivity(new Intent(HomeActivity.this, AuthActivity.class));
                    finish();
                    Toast.makeText(HomeActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
            error.printStackTrace();

        }) {

            //add token to headers


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences userPref = getSharedPreferences("user", MODE_PRIVATE);
                String token = userPref.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }


        };

        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(request);
    }


}
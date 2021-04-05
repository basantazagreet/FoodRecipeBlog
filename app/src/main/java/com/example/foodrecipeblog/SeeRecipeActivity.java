package com.example.foodrecipeblog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class SeeRecipeActivity extends AppCompatActivity {

    ImageView ivfood;
    TextView tvtitle;
    TextView tvreadyin;
    TextView tvorigin;
    TextView tvdescription;
    TextView tvingredients;
    TextView tvsteps;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_recipe);

        ivfood = findViewById(R.id.ivfood);
        tvtitle = findViewById(R.id.tvtitle);
        tvreadyin = findViewById(R.id.tvreadyin);
        tvorigin = findViewById(R.id.tvorigin);
        tvdescription = findViewById(R.id.tvdescription);
        tvingredients = findViewById(R.id.tvingredients);
        tvsteps = findViewById(R.id.tvsteps);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        getRecipe();



    }


    @Override
    protected void onResume() {
       if(dialog.isShowing()){
           dialog.dismiss();
       }

        super.onResume();
       getRecipe();
    }

    /*public void init() {
        ivfood = findViewById(R.id.ivfood);
        tvtitle = findViewById(R.id.tvtitle);
        tvreadyin = findViewById(R.id.tvreadyin);
        tvorigin = findViewById(R.id.tvorigin);
        tvdescription = findViewById(R.id.tvdescription);
        tvingredients = findViewById(R.id.tvingredients);
        tvsteps = findViewById(R.id.tvsteps);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        getRecipe();
    }*/

    public void getRecipe() {
        dialog.setMessage("Getting recipe");
        dialog.show();

        //Intent bata id lyako
        Bundle bundle = getIntent().getExtras();
        String recipeid = bundle.getString("id");


        StringRequest request = new StringRequest(Request.Method.GET, Constant.GET_A_RECIPE + recipeid, response -> {
            //we get response if connection success
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {

                    JSONObject recipe = object.getJSONObject("recipe");

                    tvtitle.setText(recipe.getString("title"));
                    tvdescription.setText(recipe.getString("desc"));
                    tvingredients.setText(recipe.getString("ingredients"));
                    tvsteps.setText(recipe.getString("steps"));
                    tvreadyin.setText(recipe.getString("readyin"));
                    tvorigin.setText(recipe.getString("origin"));


                    Picasso.get().load(Constant.URL + "storage/recipes/" + recipe.getString("photo")).into(ivfood);

                    JSONObject user = object.getJSONObject("user");

                    Toast.makeText(this, "Recipe Fetched", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();

        }, error -> {
            // error if connection not success
            error.printStackTrace();
            dialog.dismiss();
        }) {

            // add parameters, if not leave blank

        };

        //add this request to requestqueue
        RequestQueue queue = Volley.newRequestQueue(SeeRecipeActivity.this);
        queue.add(request);
    }

    public void goBack(View view) {
        super.onBackPressed();
    }

}
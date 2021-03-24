package com.example.foodrecipeblog;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodrecipeblog.Fragments.HomeFragment;

//import com.habib.blogapp.Models.Post;
//import com.habib.blogapp.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddRecipeActivity extends AppCompatActivity {
    private Button btnPost;
    private ImageView imgPost;

    private EditText txttitle;
    private EditText txtdesc;
    private EditText txtorigin;
    private EditText txtreadyin;
    private EditText txtcategory;
    private EditText txtingredients;
    private EditText txtsteps;

    private Bitmap bitmap = null;
    private static final int GALLERY_CHANGE_POST = 3;
    private ProgressDialog dialog;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        init();
    }

    private void init() {
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        btnPost = findViewById(R.id.btnAddPost);
        imgPost = findViewById(R.id.imgAddPost);

        txttitle = findViewById(R.id.txttitle);
        txtdesc = findViewById(R.id.txtdesc);
        txtorigin = findViewById(R.id.txtorigin);
        txtcategory = findViewById(R.id.txtcategory);
        txtreadyin = findViewById(R.id.txtreadyin);
        txtingredients = findViewById(R.id.txtingredients);
        txtsteps = findViewById(R.id.txtsteps);


        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);


        //Yaha problem cha hola

        //Bundle wala code
        /*Bundle bundle = getIntent().getExtras();
        String IMG_URL = bundle.getString(HomeActivity.IMAGE_URI);
        imgPost.setImageURI(Uri.parse(IMG_URL));*/

        imgPost.setImageURI(getIntent().getData());
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), getIntent().getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnPost.setOnClickListener(v -> {
            if (!txttitle.getText().toString().isEmpty() && !txtorigin.getText().toString().isEmpty() &&
                    !txtdesc.getText().toString().isEmpty() && !txtcategory.getText().toString().isEmpty()
                    && !txtreadyin.getText().toString().isEmpty() && !txtingredients.getText().toString().isEmpty()

                    && !txtsteps.getText().toString().isEmpty()
            ) {
                post();
            } else {
                Toast.makeText(this, "Check all fields once.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void post() {
        dialog.setMessage("Posting");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Constant.ADD_RECIPE, response -> {

            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {

                    Toast.makeText(this, "Recipe posted", Toast.LENGTH_SHORT).show();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();

        }, error -> {
            error.printStackTrace();
            dialog.dismiss();
        }) {

            // add token to header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }

            // add params

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();

                map.put("title", txttitle.getText().toString().trim());
                map.put("origin", txtorigin.getText().toString().trim());
                map.put("category", txtcategory.getText().toString().trim());
                map.put("readyin", txtreadyin.getText().toString().trim());
                map.put("desc", txtdesc.getText().toString().trim());
                map.put("ingredients", txtingredients.getText().toString().trim());
                map.put("steps", txtsteps.getText().toString().trim());


                map.put("photo", bitmapToString(bitmap));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(AddRecipeActivity.this);
        queue.add(request);

    }

    private String bitmapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array, Base64.DEFAULT);
        }

        return "";
    }


    public void cancelPost(View view) {
        super.onBackPressed();
    }

    public void changePhoto(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, GALLERY_CHANGE_POST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result ok kata bata aako?
        if (requestCode == GALLERY_CHANGE_POST && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            imgPost.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

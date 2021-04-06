package com.example.foodrecipeblog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodrecipeblog.Adapter.CommentsAdapter;
import com.example.foodrecipeblog.Model.Comment;
import com.example.foodrecipeblog.Model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeeRecipeActivity extends AppCompatActivity {

    ImageView ivfood;
    TextView tvtitle;
    TextView tvreadyin;
    TextView tvorigin;
    TextView tvdescription;
    TextView tvingredients;
    TextView tvsteps;
    ProgressDialog dialog;
    private SharedPreferences preferences;
    private EditText txtAddComment;
    private CommentsAdapter adapter;

    private String recipeid ;


    private RecyclerView recyclerView;
    private ArrayList<Comment> list;



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


        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = findViewById(R.id.recyclerComments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        txtAddComment = findViewById(R.id.txtAddComment);


        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        if (!isLoggedIn) {
            txtAddComment.setVisibility(View.GONE);
        }



        //Intent bata id lyako
        Bundle bundle = getIntent().getExtras();
         recipeid = bundle.getString("id");


        getRecipe();
        getComments();

    }


    @Override
    protected void onResume() {
       if(dialog.isShowing()){
           dialog.dismiss();
       }

        super.onResume();
       getRecipe();
    }



    public void getRecipe() {
        dialog.setMessage("Getting recipe");
        dialog.show();


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

    private void getComments() {
        list = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST,Constant.COMMENTS,res->{

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")){
                    JSONArray comments = new JSONArray(object.getString("comments"));
                    for (int i = 0; i < comments.length(); i++) {
                        JSONObject comment = comments.getJSONObject(i);
                        JSONObject user = comment.getJSONObject("user");

                        User mUser = new User();
                        mUser.setId(user.getInt("id"));
                        mUser.setPhoto(Constant.URL+"storage/profiles/"+user.getString("photo"));
                        mUser.setUserName(user.getString("name")+" "+user.getString("lastname"));

                        Comment mComment = new Comment();
                        mComment.setId(comment.getInt("id"));
                        mComment.setUser(mUser);
                        mComment.setDate(comment.getString("created_at"));
                        mComment.setComment(comment.getString("comment"));
                        list.add(mComment);
                    }
                }

                adapter = new CommentsAdapter(this,list);
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        },error -> {
            error.printStackTrace();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",recipeid+"");
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(SeeRecipeActivity.this);
        queue.add(request);
    }


    public void addComment(View view) {
        String commentText = txtAddComment.getText().toString();
        dialog.setMessage("Adding comment");
        dialog.show();
        if (commentText.length()>0){
            StringRequest request = new StringRequest(Request.Method.POST,Constant.CREATE_COMMENT,res->{

                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getBoolean("success")){
                        JSONObject comment = object.getJSONObject("comment");
                        JSONObject user = comment.getJSONObject("user");

                        Comment c = new Comment();
                        User u = new User();
                        u.setId(user.getInt("id"));
                        u.setUserName(user.getString("name")+" "+user.getString("lastname"));
                        u.setPhoto(Constant.URL+"storage/profiles/"+user.getString("photo"));
                        c.setUser(u);
                        c.setId(comment.getInt("id"));
                        c.setDate(comment.getString("created_at"));
                        c.setComment(comment.getString("comment"));



                        list.add(c);
                        recyclerView.getAdapter().notifyDataSetChanged();
                        txtAddComment.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            },err->{
                err.printStackTrace();
                dialog.dismiss();
            }){
                //add token to header

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String token = preferences.getString("token","");
                    HashMap<String,String> map = new HashMap<>();
                    map.put("Authorization","Bearer "+token);
                    return map;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("id",recipeid+"");
                    map.put("comment",commentText);
                    return map;
                }


            };
            RequestQueue queue = Volley.newRequestQueue(SeeRecipeActivity.this);
            queue.add(request);
        }
    }


}
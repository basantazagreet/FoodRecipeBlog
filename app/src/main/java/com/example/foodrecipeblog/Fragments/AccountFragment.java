package com.example.foodrecipeblog.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodrecipeblog.AuthActivity;
import com.example.foodrecipeblog.Constant;
import com.example.foodrecipeblog.EditUserInfoActivity;
import com.example.foodrecipeblog.HomeActivity;
import com.example.foodrecipeblog.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    private View view;
    private MaterialToolbar toolbar ;
    private CircleImageView imgProfile ;
    private TextView txtName, txtPostsCount;
    private Button btnEditAccount;


    private SharedPreferences preferences;
    private String imgUrl = "";

    public AccountFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_account, container, false);

        toolbar = v.findViewById(R.id.toolbarAccount);
        imgProfile = v.findViewById(R.id.imgAccountProfile);
        txtName = v.findViewById(R.id.txtAccountName);
        txtPostsCount = v.findViewById(R.id.txtAccountPostCount);
        btnEditAccount = v.findViewById(R.id.btnEditAccount);


        init();
        return v;

    }

    private void init() {
        preferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        //Yaha hola problem fragment ko kei save nagarera ho ki?
        ((HomeActivity) getContext()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        getData();


        btnEditAccount.setOnClickListener(v -> {
            Intent i = new Intent(((HomeActivity) getContext()), EditUserInfoActivity.class);
            i.putExtra("imgUrl", imgUrl);
            startActivity(i);
        });
    }


    private void getData() {

        StringRequest request = new StringRequest(Request.Method.GET, Constant.PROFILE, res -> {

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")) {

                    JSONObject user = object.getJSONObject("user");
                    txtName.setText(user.getString("name") + " " + user.getString("lastname"));
                    Picasso.get().load(Constant.URL + "storage/profiles/" + user.getString("photo")).into(imgProfile);
                    imgUrl = Constant.URL + "storage/profiles/" + user.getString("photo");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_logout: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        StringRequest request = new StringRequest(Request.Method.GET, Constant.LOGOUT, res -> {

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(((HomeActivity) getContext()), AuthActivity.class));
                    ((HomeActivity) getContext()).finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
            error.printStackTrace();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }


    //on hidden change wala method cha refresh garna

    @Override
    public void onResume() {
        super.onResume();
    }
}

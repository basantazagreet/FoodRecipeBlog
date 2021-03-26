package com.example.foodrecipeblog.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import com.example.foodrecipeblog.HomeActivity;
import com.example.foodrecipeblog.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private SharedPreferences preferences;

    Button btlogout;
    Button btauth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btlogout = view.findViewById(R.id.btlogout);
        btlogout.setOnClickListener(v -> {
            logout();
        });

        btauth = view.findViewById(R.id.btauth);
        btauth.setOnClickListener(v -> {
            gotoAuth();
        });
        return view;
    }


    private void logout() {

        StringRequest request = new StringRequest(Request.Method.GET, Constant.LOGOUT, res -> {

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")) {

                    Toast.makeText(getActivity(), "Logout successs", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(((HomeActivity) getContext()), AuthActivity.class));
                    ((HomeActivity) getContext()).finish();

                } else if (object.getBoolean("success") == false) {
                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    //Token expire vaye pani shp data clear gardim ta

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

    public void gotoAuth() {
        Intent i = new Intent(((HomeActivity) getContext()), AuthActivity.class);
        startActivity(i);
    }

}

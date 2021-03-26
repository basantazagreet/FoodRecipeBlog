package com.example.foodrecipeblog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class OnBoardActivity extends AppCompatActivity {


    //Make a layout for welcome to this app in this activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
    }

    public void gotoAuth(View v) {
        Intent i = new Intent(OnBoardActivity.this, AuthActivity.class);
        startActivity(i);
    }
}
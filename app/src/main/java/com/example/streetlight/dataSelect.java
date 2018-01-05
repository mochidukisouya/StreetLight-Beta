package com.example.streetlight;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Mochiduki on 2017/11/17.
 */

public class dataSelect extends AppCompatActivity {
    String account,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dataselect);
        getSupportActionBar().setTitle("現有路燈");
        account = this.getIntent().getExtras().getString("userID");
        url = this.getIntent().getExtras().getString("serverIP");

    }




}

package com.example.jhjun.firebasebbs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startBbs(View view){  // view -> 클릭한 시점에?
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}

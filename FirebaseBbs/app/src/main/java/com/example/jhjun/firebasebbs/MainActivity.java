package com.example.jhjun.firebasebbs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.View;

import com.example.jhjun.firebasebbs.util.PermissionControl;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PermissionControl.CallBack{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionControl.checkVersion(this);
    }

    public void startBbs(View view){  // view -> 클릭한 시점에?
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionControl.onResult(this, requestCode, grantResults);
    }

    @Override
    public void init() {
        Intent intent = new Intent(this, NaviActivity.class);
        startActivity(intent);

        // 스플래쉬 화면은 backstack에 남아있으면 안됨.
        finish();
    }
}

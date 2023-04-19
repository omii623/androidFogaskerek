package com.szerviz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
private static final String LOG_TAG = MainActivity.class.getName();
private static final int SECRET_KEY = 1358;

    EditText userNameET;
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        userNameET = findViewById(R.id.editTextUserName);
        passwordET = findViewById(R.id.editTextPassword);

        RelativeLayout relativeLayout = findViewById(R.id.mainid);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();
    }

    public void login(View view) {
        String userNameStr = userNameET.getText().toString();
        String passwordStr = passwordET.getText().toString();

        Log.i(LOG_TAG,"login: "+userNameStr+", "+passwordStr);

        Intent intentHome = new Intent(MainActivity.this, HomeActivity.class);
        intentHome.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intentHome);
    }

    public void regist(View view) {
        Intent intentRegist = new Intent(MainActivity.this, RegistActivity.class);
        intentRegist.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intentRegist);
    }

    public void info(View view) {
        Intent intentInfo = new Intent(MainActivity.this, InfoActivity.class);
        intentInfo.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intentInfo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "MainActivity-onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "MainActivity-onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "MainActivity-onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "MainActivity-onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "MainActivity-onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "MainActivity-onRestart");
    }
}
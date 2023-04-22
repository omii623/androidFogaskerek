package com.szerviz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistActivity.class.getName();
    private static final int SECRET_KEY = 1358;

    EditText userNameEditText;
    EditText userEmailEditText;
    EditText passwordEditText;
    EditText passwordConfirmEditText;
    EditText phoneEditText;
    EditText addressEditText;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_regist);

        int secret_key = getIntent().getIntExtra("SECRET_KEY",0);
        if(secret_key != 1358){
            finish();
        }

        userNameEditText = findViewById(R.id.userNameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordConfirmEditText = findViewById(R.id.passwordAgainEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("notifi","notifi", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void notification(String title, String text){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(RegistActivity.this,"notifi");
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(RegistActivity.this);
        managerCompat.notify(1,builder.build());
    }

    public void cencel(View view) {
        finish();
    }

    public void regist(View view) {
        String userName = userNameEditText.getText().toString();
        String email = userEmailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();

        if(userName.equals("") || email.equals("")){//todo többi
            notification("HIBA","Nem lehet üres block");
            Log.e(LOG_TAG, "Nem lehet üres block");
            return;
        }

        if (!password.equals(passwordConfirm) || password.equals("")) {
            notification("HIBA","Nem egyenlő a jelszó és a megerősítése.");
            Log.e(LOG_TAG, "Nem egyenlő a jelszó és a megerősítése.");
            return;
        }

        Log.i(LOG_TAG, "Regisztrált: " + userName + ", e-mail: " + email);

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG,"Sikeresen létrehozva");
                    //todo kiszervezni
                    Intent intentHome = new Intent(RegistActivity.this, HomeActivity.class);
                    startActivity(intentHome);
                }else{
                    notification("HIBA","Hiba merült fel a regisztráció során");
                    Log.e(LOG_TAG,"Hiba merült fel");
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "RegistActivity-onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "RegistActivity-onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "RegistActivity-onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "RegistActivity-onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "RegistActivity-onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "RegistActivity-onRestart");
    }

}
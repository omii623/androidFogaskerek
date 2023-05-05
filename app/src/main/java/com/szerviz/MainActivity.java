package com.szerviz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.szerviz.data.controller.UserController;
import com.szerviz.data.model.User;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int SECRET_KEY = 1358;
    private static final int RC_SING_IN = 1541;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    EditText userNameET;
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        userNameET = findViewById(R.id.editTextUserName);
        passwordET = findViewById(R.id.editTextPassword);

        RelativeLayout relativeLayout = findViewById(R.id.mainid);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("notifi","notifi", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public void notification(String title, String text){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,"notifi");
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.fogaskerek);
        builder.setAutoCancel(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
        managerCompat.notify(1,builder.build());
    }

    public void login(View view) {
        String userNameStr = userNameET.getText().toString();
        String passwordStr = passwordET.getText().toString();

        Log.i(LOG_TAG,"login: "+userNameStr);

        if(userNameStr.isEmpty() || passwordStr.isEmpty()){
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(userNameStr,passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG,"Sikeres belépés");
                    //todo kiszervezni
                    Intent intentHome = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intentHome);
                }else{
                    notification("HIBA","Hiba merült fel a belépés során");
                    Log.e(LOG_TAG,"Hiba merült fel a belépés során");
                }
            }
        });
    }

    public void registGoogle(View view){
        Intent intentSignIn = googleSignInClient.getSignInIntent();
        startActivityForResult(intentSignIn,RC_SING_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SING_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                Log.d(LOG_TAG,"id: "+ googleSignInAccount.getIdToken());
                firebaseAuthGoogleSingIn(googleSignInAccount.getIdToken());
            }catch (ApiException e){
                Log.w(LOG_TAG,"Google bejelenkezés sikertelen",e);
            }
        }
    }

    public void firebaseAuthGoogleSingIn(String token){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(token,null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG,"Sikeres belépés (google)");
                    //todo kiszervezni
                    Intent intentHome = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intentHome);
                }else{
                    Log.e(LOG_TAG,"Hiba merült fel (google)");
                }
            }
        });
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
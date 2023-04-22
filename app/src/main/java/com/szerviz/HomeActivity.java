package com.szerviz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.*;
import com.szerviz.ui.main.*;
import com.szerviz.databinding.*;

public class HomeActivity extends AppCompatActivity {
    private static final String LOG_TAG = HomeActivity.class.getName();

    private ActivityHomeBinding binding;
    private FirebaseUser firebaseUser;

    EditText userNameEditText;
    EditText phoneEditText;
    EditText addressEditText;
    EditText passwordOldEditText;
    EditText passwordEditText;
    EditText passwordConfirmEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            Log.d(LOG_TAG,"Sikeres belépés");
        }else{
            Log.d(LOG_TAG,"Sikertelen belépés");
            finish();
        }

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }

    public void updateEditText(){
        userNameEditText = findViewById(R.id.userNameEditTextHome);
        phoneEditText = findViewById(R.id.phoneEditTextHome);
        addressEditText = findViewById(R.id.addressEditTextHome);
        passwordOldEditText = findViewById(R.id.passwordOldEditTextHome);
        passwordEditText = findViewById(R.id.passwordNewEditTextHome);
        passwordConfirmEditText = findViewById(R.id.passwordNewAgainEditTextHome);
    }

    public void map(View view) {
        String address = "3832 Léh, Baross tér 89.";
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void call(View view) {
        String phoneNumber = "+36654528432"; // Telefonszám beállítása
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(dialIntent);
    }

    public void save(View view){
        updateEditText();
        if(!passwordOldEditText.getText().toString().isEmpty() &&
           !passwordEditText.getText().toString().isEmpty() &&
           !passwordConfirmEditText.getText().toString().isEmpty() &&
           passwordConfirmEditText.getText().toString().equals(passwordEditText.getText().toString())
        ){
            //todo check
            Log.d(LOG_TAG,"jelszó csere: "+passwordEditText.getText().toString());
            firebaseUser.updatePassword(passwordEditText.toString());
        }

    }

}
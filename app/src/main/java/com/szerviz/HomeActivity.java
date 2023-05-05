package com.szerviz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.*;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.szerviz.data.controller.TimePickerController;
import com.szerviz.data.controller.UserController;
import com.szerviz.data.model.User;
import com.szerviz.ui.main.*;
import com.szerviz.databinding.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity/* implements LoaderManager.LoaderCallbacks<String> */{
    private static final String LOG_TAG = HomeActivity.class.getName();

    private ActivityHomeBinding binding;
    private FirebaseUser firebaseUser;

    EditText userNameEditText;
    EditText phoneEditText;
    EditText addressEditText;
    EditText passwordOldEditText;
    EditText passwordEditText;
    EditText passwordConfirmEditText;

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference userCollection;
    private CollectionReference newsCollection;
    private CollectionReference timePickerCollection;
    private Task<User> user;

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

        UserController uc = UserController.getInstance();
        user = uc.getUserByEmail(firebaseUser.getEmail());
    }

    public void updateEditText(){
        userNameEditText = findViewById(R.id.userNameEditTextHome);
        phoneEditText = findViewById(R.id.phoneEditTextHome);
        addressEditText = findViewById(R.id.addressEditTextHome);
        //passwordOldEditText = findViewById(R.id.passwordOldEditTextHome);
        //passwordEditText = findViewById(R.id.passwordNewEditTextHome);
        //passwordConfirmEditText = findViewById(R.id.passwordNewAgainEditTextHome);
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

        UserController.getInstance().updateUser(userNameEditText.getText().toString(),phoneEditText.getText().toString(),addressEditText.getText().toString());
        user = UserController.getInstance().getUserByEmail(firebaseUser.getEmail());
        //userNameEditText.setText(user.getResult().getUserName());
        //phoneEditText.setText(user.getResult().getPhoneNumber());
        //addressEditText.setText(user.getResult().getAddres());

    }

    public void update(View view){
        updateEditText();

        if(user.getResult() == null){
            User userCreate = new User("",firebaseUser.getEmail(),"","","");
            UserController uc = UserController.getInstance();
            uc.addUser(userCreate);
        } else {
            userNameEditText.setText(user.getResult().getUserName());
            phoneEditText.setText(user.getResult().getPhoneNumber());
            addressEditText.setText(user.getResult().getAddres());

            Log.d(LOG_TAG,"lekérdezés: "+UserController.getInstance().reference);
        }
    }

    String date = LocalDate.now().getYear()+"/"+LocalDate.now().getMonth().getValue()+"/"+LocalDate.now().getDayOfMonth();
    public void apply(View view){
        CalendarView calendarView = findViewById(R.id.calendarView);
        TimePicker timePicker = findViewById(R.id.timePicker);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                int selectedYear = selectedDate.get(Calendar.YEAR);
                int selectedMonth = selectedDate.get(Calendar.MONTH)+1;
                int selectedDay = selectedDate.get(Calendar.DAY_OF_MONTH);

                date = selectedYear + "/" +selectedMonth + "/" + selectedDay;
            }
        });

        String timeSelect = timePicker.getHour()+":"+timePicker.getMinute();

        TimePickerController.getInstance().addTimePicker(new com.szerviz.data.model.TimePicker(firebaseUser.getEmail(),date,timeSelect));

        Log.i(LOG_TAG, "timePicker: "+date+"-"+timeSelect);
    }

    public void updateNews(View view){
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        int childCount = tableLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                tableLayout.removeView(child);
            }
        }

        tableLayout.removeAllViews();

        TimePickerController.getInstance().getAllTimePicker(tableLayout,this);
    }

    public void deleteLast(View view){
        TimePickerController.getInstance().deleteLastTimePicker();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "HomeActivity-onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "HomeActivity-onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "HomeActivity-onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "HomeActivity-onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "HomeActivity-onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "HomeActivity-onRestart");
    }

}
package com.szerviz.data.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.szerviz.R;
import com.szerviz.data.model.TimePicker;
import com.szerviz.data.model.User;

public class TimePickerController {
    private static TimePickerController instance = new TimePickerController();
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collection;
    public String reference = "";

    private TimePickerController(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        collection = firebaseFirestore.collection("timePicker");
    }

    public static TimePickerController getInstance() {return instance;}

    public  void deleteLastTimePicker(){
        collection.orderBy("date").limit(1).get().addOnCompleteListener(task ->  {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    document.getReference().delete();
                }
            }
        });
    }

    public void getAllTimePicker(TableLayout tableLayout, Context context){
        collection.orderBy("date").get().addOnCompleteListener(task ->  {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    TimePicker timePicker = document.toObject(TimePicker.class);

                    TableRow tableRow = new TableRow(context);

                    // Keret beállítása
                    ShapeDrawable shape = new ShapeDrawable(new RectShape());
                    shape.getPaint().setColor(Color.BLACK);
                    shape.getPaint().setStyle(Paint.Style.STROKE);
                    tableRow.setBackground(shape);

                    // Szövegméret beállítása
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    ));
                    textView.setTextColor(R.color.purple_500);
                    textView.setText(timePicker.getUserEmail() + " \t [" +timePicker.getDate() + " " + timePicker.getTime()+"]");
                    textView.setTextSize(20);

                    tableRow.addView(textView);
                    tableLayout.addView(tableRow);

                }
            }
        });
    }

    public void addTimePicker(TimePicker timePicker){
        collection.add(timePicker);
    }

}

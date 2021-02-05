package com.example.e_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    TextView about4;
    Button Health_Care,Reminder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();

        Health_Care=findViewById(R.id.button1_Main);
        Reminder=findViewById(R.id.button2_Main);
        about4=findViewById(R.id.about_Main);
        about4.setPaintFlags(about4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Health_Care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity2.this,Health_Care_Activity.class);
                startActivity(intent1);
            }
        });

        Reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(MainActivity2.this,NotificationPane.class);
                startActivity(intent2);
            }
        });
        about4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_about=new Intent(MainActivity2.this,About_Activity.class);
                startActivity(intent_about);
            }
        });


    }
}
package com.example.e_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class Medicament_Activity extends AppCompatActivity {

    TextView about3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_medicament_);
        getSupportActionBar().hide();

        about3=findViewById(R.id.about_Notif);
        about3.setPaintFlags(about3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}
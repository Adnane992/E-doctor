package com.example.e_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;

public class About_Activity extends AppCompatActivity {
    TextView copyrightL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_);
        copyrightL=findViewById(R.id.copyrightLabel);
        copyrightL.setText("Copyright \u00a9 "+ Calendar.getInstance().get(Calendar.YEAR) +" E-doctor");
    }
}
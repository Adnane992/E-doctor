package com.example.e_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Register_Activity extends AppCompatActivity {

    Button B1,B2;
    TextView about1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        getSupportActionBar().hide();

        B1=findViewById(R.id.button1_Regi);
        B2=findViewById(R.id.button2_Regi);
        about1=findViewById(R.id.textView2_about);
        about1.setPaintFlags(about1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Register_Activity.this,MainActivity2.class);
                startActivity(intent1);
                finish();
            }
        });

        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(Register_Activity.this,Login_Acticity.class);
                startActivity(intent2);
                finish();
            }
        });

        about1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Reg=new Intent(Register_Activity.this,About_Activity.class);
                startActivity(intent_Reg);
            }
        });

    }
}
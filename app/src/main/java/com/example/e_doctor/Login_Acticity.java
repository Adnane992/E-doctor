package com.example.e_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Login_Acticity extends AppCompatActivity {
    TextView Register,about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__acticity);
        getSupportActionBar().hide();

        Register=findViewById(R.id.Register_login);
        about=findViewById(R.id.textView1_about);
        about.setPaintFlags(about.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Reg=new Intent(Login_Acticity.this,Register_Activity.class);
                startActivity(intent_Reg);
                finish();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Reg=new Intent(Login_Acticity.this,About_Activity.class);
                startActivity(intent_Reg);
            }
        });



    }
}
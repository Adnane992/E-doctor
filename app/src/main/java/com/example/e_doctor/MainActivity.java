package com.example.e_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=3000;

    //variables
    Animation top,botton;
    ImageView img;
    TextView name_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //Les animations
        top= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botton= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        img=findViewById(R.id.imageLogin);
        name_logo=findViewById(R.id.Tv_First);
        img.setAnimation(top);
        name_logo.setAnimation(botton);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this,Login_Acticity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);




    }
}
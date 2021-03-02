package com.example.e_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=3000;

    //variables
    Animation top,bottom;
    ImageView img;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ///// Notification channel //////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("ch1","channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        ///// Notification channel //////
        //Les animations
        top= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        img=findViewById(R.id.imageLogin);
        img.setAnimation(top);

        title=findViewById(R.id.edoc_title);
        title.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest sr=new StringRequest(Request.Method.POST, "http://192.168.1.107/E-doctor.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("response session",response);
                                if(!response.equals("false") && !response.equals("null")){
                                    String[] split = response.split("@");
                                    int user_id = Integer.parseInt(split[0]);
                                    String username = split[1];
                                    Intent intent_connect = new Intent(MainActivity.this, MainActivity2.class);
                                    intent_connect.putExtra("user_id",user_id);
                                    intent_connect.putExtra("username",username);
                                    startActivity(intent_connect);
                                    finish();
                                }
                                else{
                                    Intent intent=new Intent(MainActivity.this, Login_Activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this,"Server error !", Toast.LENGTH_SHORT).show();
                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String,String> data=new HashMap<String,String>();
                        data.put("operation","SessionIdVerification");
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        Log.e("sessionId sent",prefs.getString("SessionId","none"));
                        data.put("SessionId",prefs.getString("SessionId",""));
                        return data;
                    }
                };
                RequestQueue rq= Volley.newRequestQueue(MainActivity.this);
                rq.add(sr);
            }
        },SPLASH_SCREEN);




    }
}
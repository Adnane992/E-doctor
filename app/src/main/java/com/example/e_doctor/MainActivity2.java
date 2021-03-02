package com.example.e_doctor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    TextView about4;
    Button Health_Care,Reminder;
    ImageButton history,logout;
    int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Health_Care=findViewById(R.id.button1_Main);
        Reminder=findViewById(R.id.button2_Main);
        about4=findViewById(R.id.about_Main);
        about4.setPaintFlags(about4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        history=findViewById(R.id.history);
        logout=findViewById(R.id.logoutB);
        user_id=getIntent().getIntExtra("user_id",0);
        TextView welcome = findViewById(R.id.welcomeTxt);
        welcome.setText("Hey, " + getIntent().getStringExtra("username"));

        Health_Care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity2.this,Health_Care_Activity.class);
                intent1.putExtra("user_id",user_id);
                startActivity(intent1);
            }
        });

        Reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(MainActivity2.this,NotificationPane.class);
                intent2.putExtra("user_id",user_id);
                intent2.putExtra("username",getIntent().getStringExtra("username"));
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

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String operation="fetch_history";
                StringRequest sr=new StringRequest(Request.Method.POST, "http://192.168.1.107/E-doctor.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.contains("@")){
                                    ArrayList<String> DiseaseNames=new ArrayList<>();
                                    ArrayList<String> timeStamps=new ArrayList<>();
                                    String[] historyData=response.split("@");
                                    int length=Integer.parseInt(historyData[0]);
                                    int i=0,j=0;
                                    for(i=0;i<length;i++){
                                        DiseaseNames.add(historyData[i+1]);
                                        j=i+1;
                                    }
                                    for(i=j;i<length*2;i++) timeStamps.add(historyData[i+1]);
                                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity2.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                                    builder.setTitle("Your checkup history");
                                    builder.setIcon(R.drawable.ic_history2);
                                    String msg="";
                                    for(i=0;i<length;i++) msg += "'" + DiseaseNames.get(i) + "' \t " + timeStamps.get(i) + "\n\n";
                                    builder.setMessage(msg);
                                    AlertDialog historyDialog=builder.create();
                                    historyDialog.show();
                                }
                                else{
                                    Log.i("fetch_history_response",response);
                                    Toast.makeText(MainActivity2.this, "Your checkup history is empty", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("fetching_history:error",error.toString());
                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String,String> data=new HashMap<String,String>();
                        data.put("operation",operation);
                        data.put("User",String.valueOf(user_id));
                        return data;
                    }
                };
                RequestQueue rq= Volley.newRequestQueue(MainActivity2.this);
                rq.add(sr);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ProgressDialog pd=new ProgressDialog(MainActivity2.this);
                pd.setTitle("Login");
                pd.setMessage("please wait while we log you in");
                pd.setCancelable(false);
                pd.show();
                SystemClock.sleep(3000);
                pd.dismiss();*/

                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity2.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                builder.setTitle("Data collision error");
                builder.setIcon(R.drawable.ic_error);
                builder.setMessage("The app does not support multiple users yet , so please be sure to delete all your reminders before logout , so they do not interfere with other users'");
                builder.setPositiveButton("Already done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest sr=new StringRequest(Request.Method.POST, "http://192.168.1.107/E-doctor.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("Logout successful")){
                                            Intent logout = new Intent(MainActivity2.this,Login_Activity.class);
                                            startActivity(logout);
                                            finish();
                                        }
                                        Toast.makeText(MainActivity2.this,response,Toast.LENGTH_SHORT).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("logout:error",error.toString());
                                    }
                                }
                        ){
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String,String> data=new HashMap<String,String>();
                                data.put("operation","Logout");
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity2.this);
                                data.put("SessionId",prefs.getString("SessionId","none"));
                                return data;
                            }
                        };
                        RequestQueue rq= Volley.newRequestQueue(MainActivity2.this);
                        rq.add(sr);
                    }
                });
                builder.setNegativeButton("Cancel",null);
                AlertDialog logout_dialog=builder.create();
                logout_dialog.show();
            }
        });
    }
}
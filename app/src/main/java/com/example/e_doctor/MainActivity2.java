package com.example.e_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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
    ImageButton history;
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
        user_id=getIntent().getIntExtra("user_id",0);

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

    }
}
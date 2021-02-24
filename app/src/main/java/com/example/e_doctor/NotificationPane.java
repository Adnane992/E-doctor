package com.example.e_doctor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NotificationPane extends AppCompatActivity  {

   Button Alarm;
   EditText hour_Clock,minute_Clock,drug,dos;
   ImageButton medList;
   int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_pane);
        Alarm=findViewById(R.id.Alarm_Notif);
        hour_Clock=findViewById(R.id.hour_Notif);
        minute_Clock=findViewById(R.id.minute_Notif);
        drug=findViewById(R.id.drug_name);
        dos=findViewById(R.id.dosage);
        medList=findViewById(R.id.medsListB);
        user_id=getIntent().getIntExtra("user_id",0);
        if(getIntent().getStringExtra("alter") != null){
            drug.setText(getIntent().getStringExtra("Name"));
            dos.setText(getIntent().getStringExtra("Dosage"));
            hour_Clock.setText(getIntent().getStringExtra("Hours"));
            minute_Clock.setText(getIntent().getStringExtra("Minutes"));
            Alarm.setText("Update reminder");
            Alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!hour_Clock.getText().toString().equals("") && !minute_Clock.getText().toString().equals("") && !drug.getText().toString().equals("") && !dos.getText().toString().equals("")){
                        int heur=Integer.parseInt(hour_Clock.getText().toString());
                        int min=Integer.parseInt(minute_Clock.getText().toString());
                        if(heur<=23 && min<=59){
                            String operation="updateMed";
                            StringRequest sr=new StringRequest(Request.Method.POST, "http://192.168.1.107/E-doctor.php",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.e("updateReminder",response);

                                            int position = getIntent().getIntExtra("Position",0);
                                            ReminderBroadcast.removeAlarm(NotificationPane.this,position);
                                            ReminderBroadcast.setAlarm(NotificationPane.this,position,heur,min,drug.getText().toString().trim(),dos.getText().toString());
                                            Toast.makeText(NotificationPane.this,"Reminder updated",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(NotificationPane.this,Medicament_Activity.class);
                                            intent.putExtra("user_id",user_id);
                                            startActivity(intent);
                                            finish();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("updateReminder",error.toString());
                                            Toast.makeText(NotificationPane.this,"Could not update the reminder",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            ){
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String,String> data=new HashMap<String,String>();
                                    data.put("operation",operation);
                                    data.put("Id",getIntent().getStringExtra("Med_id"));
                                    data.put("Name",drug.getText().toString().trim());
                                    data.put("Hours",hour_Clock.getText().toString());
                                    data.put("Minutes",minute_Clock.getText().toString());
                                    data.put("Dosage",dos.getText().toString());
                                    return data;
                                }
                            };
                            RequestQueue rq= Volley.newRequestQueue(NotificationPane.this);
                            rq.add(sr);
                        }
                        else Toast.makeText(NotificationPane.this,"Incorrect time value",Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(NotificationPane.this,"All fields must be set",Toast.LENGTH_SHORT).show();
                }
            });
            Button delReminder=findViewById(R.id.deleteReminder);
            delReminder.setVisibility(View.VISIBLE);
            delReminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(NotificationPane.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    builder.setTitle("Confirm delete");
                    builder.setIcon(R.drawable.ic_delete);
                    builder.setMessage("Do you really want to delete this reminder ?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String operation="delReminder";
                            StringRequest sr=new StringRequest(Request.Method.POST, "http://192.168.1.107/E-doctor.php",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if(response.equals("Reminder removed")){
                                                int position = getIntent().getIntExtra("Position",0);
                                                ReminderBroadcast.removeAlarm(NotificationPane.this,position);
                                                Toast.makeText(NotificationPane.this,"Reminder deleted",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(NotificationPane.this,Medicament_Activity.class);
                                                intent.putExtra("user_id",user_id);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(NotificationPane.this, response, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("deleteReminder",error.toString());
                                            Toast.makeText(NotificationPane.this,"Could not delete this reminder",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            ){
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String,String> data=new HashMap<String,String>();
                                    data.put("operation",operation);
                                    data.put("Id",getIntent().getStringExtra("Med_id"));
                                    return data;
                                }
                            };
                            RequestQueue rq= Volley.newRequestQueue(NotificationPane.this);
                            rq.add(sr);
                        }
                    });
                    builder.setNegativeButton("NO",null);
                    AlertDialog confirm_dialog=builder.create();
                    confirm_dialog.show();
                }
            });
        }
        else{
            Alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!hour_Clock.getText().toString().equals("") && !minute_Clock.getText().toString().equals("") && !drug.getText().toString().equals("") && !dos.getText().toString().equals("")){
                        int heur=Integer.parseInt(hour_Clock.getText().toString());
                        int min=Integer.parseInt(minute_Clock.getText().toString());
                        if(heur<=23 && min<=59)
                        {
                            String operation="setReminder";
                            String drugName=drug.getText().toString().trim();
                            float dosage=Float.parseFloat(dos.getText().toString());
                            StringRequest sr=new StringRequest(Request.Method.POST, "http://192.168.1.107/E-doctor.php",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.e("setReminder",response);
                                            drug.setText("");
                                            dos.setText("");
                                            hour_Clock.setText("");;
                                            minute_Clock.setText("");
                                            Toast.makeText(NotificationPane.this, "Reminder added", Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("setReminder",error.toString());
                                            Toast.makeText(NotificationPane.this,"Could not save the reminder to db",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            ){
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String,String> data=new HashMap<String,String>();
                                    data.put("operation",operation);
                                    data.put("User",String.valueOf(user_id).trim());
                                    data.put("Name",drugName);
                                    data.put("Hours",String.valueOf(heur));
                                    data.put("Minutes",String.valueOf(min));
                                    data.put("Dosage",String.valueOf(dosage));
                                    return data;
                                }
                            };
                            RequestQueue rq= Volley.newRequestQueue(NotificationPane.this);
                            rq.add(sr);
                        }
                        else Toast.makeText(NotificationPane.this,"Incorrect time value",Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(NotificationPane.this,"All fields must be set",Toast.LENGTH_SHORT).show();
                }
            });
        }

        medList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NotificationPane.this,Medicament_Activity.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }
        });
    }
}
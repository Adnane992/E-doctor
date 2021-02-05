package com.example.e_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class NotificationPane extends AppCompatActivity  {

   Button add_Med,My_Med,Alarm;
   EditText hour_Clock,minute_Clock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_pane);

       // add_Med=findViewById(R.id.button_Notif);
        My_Med=findViewById(R.id.button2_Notif);
        Alarm=findViewById(R.id.Alarm_Notif);
        hour_Clock=findViewById(R.id.hour_Notif);
        minute_Clock=findViewById(R.id.minute_Notif);

        My_Med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NotificationPane.this,Medicament_Activity.class);
                startActivity(intent);
            }
        });

        Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int heure=Integer.parseInt(hour_Clock.getText().toString());
                int min=Integer.parseInt(minute_Clock.getText().toString());

                Intent alarm=new Intent(AlarmClock.ACTION_SET_ALARM);
                alarm.putExtra(AlarmClock.EXTRA_HOUR,heure);
                alarm.putExtra(AlarmClock.EXTRA_MINUTES,min);

                if(heure<=24 && min<=60)
                {
                    startActivity(alarm);
                }
            }
        });



    }


}
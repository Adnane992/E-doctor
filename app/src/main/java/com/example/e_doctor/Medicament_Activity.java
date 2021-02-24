package com.example.e_doctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.AlarmClock;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Medicament_Activity extends AppCompatActivity {

    ListView medsList;
    Switch on_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament_);
        medsList=findViewById(R.id.medsListView);
        String operation="fetchMeds";
        int user_id=getIntent().getIntExtra("user_id",0);
        StringRequest sr=new StringRequest(Request.Method.POST, "http://192.168.1.107/E-doctor.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("@")){
                            String[] response_split=response.split("@");
                            ArrayList<String> Ids=new ArrayList<>();
                            ArrayList<String> Names=new ArrayList<>();
                            ArrayList<String> Minutes=new ArrayList<>();
                            ArrayList<String> Hours=new ArrayList<>();
                            ArrayList<String> Dosages=new ArrayList<>();
                            ArrayList<String> Status=new ArrayList<>();
                            int num_rows=Integer.parseInt(response_split[0]);
                            int i=0,j=0;
                            for(i=0;i<num_rows;i++){
                                Ids.add(response_split[i+1]);
                                j=i+1;
                            }
                            for(i=j;i<num_rows*2;i++){
                                Names.add(response_split[i+1]);
                                j=i+1;
                            }
                            for(i=j;i<num_rows*3;i++){
                                Hours.add(response_split[i+1]);
                                j=i+1;
                            }
                            for(i=j;i<num_rows*4;i++){
                                Minutes.add(response_split[i+1]);
                                j=i+1;
                            }
                            for(i=j;i<num_rows*5;i++){
                                Dosages.add(response_split[i+1]);
                                j=i+1;
                            }
                            for(i=j;i<num_rows*6;i++){
                                Status.add(response_split[i+1]);
                            }
                            for(i=0;i<num_rows;i++) Log.i("status",Status.get(i));
                            ///// call a function to create medicines list ui
                            Adap medListAdapter = new Adap(Medicament_Activity.this,Ids,Names,Dosages,Hours,Minutes,Status);
                            medsList.setAdapter(medListAdapter);
                            /// create notifications for enabled reminders at activity start
                            for (i=0;i<medsList.getCount();i++){
                                ReminderBroadcast.setAlarm(Medicament_Activity.this,i,Integer.parseInt(Hours.get(i)),Integer.parseInt(Minutes.get(i)),Names.get(i),Dosages.get(i));
                            }
                            //// set on item click listener
                            medsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Log.i("clicked","true");
                                    Log.i("item_clicked",Names.get(position)+Dosages.get(position));
                                    Intent alter=new Intent(Medicament_Activity.this,NotificationPane.class);
                                    alter.putExtra("alter","true");
                                    alter.putExtra("user_id",user_id);
                                    alter.putExtra("Med_id",Ids.get(position));
                                    alter.putExtra("Name",Names.get(position));
                                    alter.putExtra("Dosage",Dosages.get(position));
                                    alter.putExtra("Hours",Hours.get(position));
                                    alter.putExtra("Minutes",Minutes.get(position));
                                    alter.putExtra("Position",position);
                                    startActivity(alter);
                                    finish();
                                }
                            });
                        }
                        else{
                            Log.i("reponsenot@",response);
                            Toast.makeText(Medicament_Activity.this, "No reminders to show", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("fetchMeds",error.toString());
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
        RequestQueue rq= Volley.newRequestQueue(Medicament_Activity.this);
        rq.add(sr);
    }
}

class Adap extends ArrayAdapter<String>{

    Context c;
    ArrayList<String> Names,Dosages,Hours,Minutes,Status,Ids;

    Adap(Context c,ArrayList<String> Ids, ArrayList<String> Names, ArrayList<String> Dosages, ArrayList<String> Hours,ArrayList<String> Minutes,ArrayList<String> Status){
        super(c,R.layout.customlistview,R.id.medName,Names);
        this.c=c;
        this.Ids=Ids;
        this.Names=Names;
        this.Dosages=Dosages;
        this.Hours=Hours;
        this.Minutes=Minutes;
        this.Status=Status;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)c.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item=inflater.inflate(R.layout.customlistview,parent,false);
        TextView medName,medDosage,timing;
        ImageView ImgSick;
        Switch onoff;
        medName=item.findViewById(R.id.medName);
        medDosage=item.findViewById(R.id.medDosage);
        timing=item.findViewById(R.id.timing);
        ImgSick=item.findViewById(R.id.ImgSick);
        onoff=item.findViewById(R.id.activate);


        if(Status.get(position).equals("1")) onoff.setChecked(true);
        else onoff.setChecked(false);
        medName.setText(Names.get(position));
        medDosage.setText("Dosage : " + Dosages.get(position));
        timing.setText("at : " + Hours.get(position) + ":" + Minutes.get(position));
        ImgSick.setImageResource(R.drawable.ic_sick);
        onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String status=null;
                if(isChecked){

                    /*Intent alarm=new Intent(AlarmClock.ACTION_SET_ALARM);
                    ArrayList<Integer> days=new ArrayList<>();
                    days.add(Calendar.MONDAY);
                    days.add(Calendar.TUESDAY);
                    days.add(Calendar.WEDNESDAY);
                    days.add(Calendar.THURSDAY);
                    days.add(Calendar.FRIDAY);
                    days.add(Calendar.SATURDAY);
                    days.add(Calendar.SUNDAY);
                    alarm.putExtra(AlarmClock.EXTRA_DAYS,days);
                    alarm.putExtra(AlarmClock.EXTRA_MINUTES,Integer.parseInt(Minutes.get(position)));
                    alarm.putExtra(AlarmClock.EXTRA_HOUR,Integer.parseInt(Hours.get(position)));
                    alarm.putExtra(AlarmClock.EXTRA_MESSAGE,Names.get(position)+" [ Dosage : "+Dosages.get(position)+" ]");
                    alarm.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
                    c.startActivity(alarm);*/


                    /*Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(Hours.get(position)));
                    calendar.set(Calendar.MINUTE,Integer.parseInt(Minutes.get(position)));
                    calendar.set(Calendar.SECOND,0);
                    if(calendar.getTimeInMillis() <= System.currentTimeMillis()) calendar.add(Calendar.DATE,1);
                    AlarmManager am = (AlarmManager)c.getSystemService(c.ALARM_SERVICE);
                    Intent intent = new Intent(c,ReminderBroadcast.class);
                    intent.putExtra("Name",Names.get(position));
                    intent.putExtra("Dosage",Dosages.get(position));
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(c,position,intent,0);
                    am.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);*/

                    ReminderBroadcast.setAlarm(c,position,Integer.parseInt(Hours.get(position)),Integer.parseInt(Minutes.get(position)),Names.get(position),Dosages.get(position));
                    status="1";
                }
                else{
                    /*Intent alarm=new Intent(AlarmClock.ACTION_DISMISS_ALARM);
                    c.startActivity(alarm);
                    Toast.makeText(c, "Reminder dismissed", Toast.LENGTH_SHORT).show();*/

                    /*AlarmManager am = (AlarmManager)c.getSystemService(c.ALARM_SERVICE);
                    Intent intent = new Intent(c,ReminderBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(c,position,intent,0);
                    am.cancel(pendingIntent);*/

                    ReminderBroadcast.removeAlarm(c,position);
                    status="0";
                }
                String operation="updateStatus";
                String finalStatus = status;
                StringRequest sr=new StringRequest(Request.Method.POST, "http://192.168.1.107/E-doctor.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("updateStatus",error.toString());
                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String,String> data=new HashMap<String,String>();
                        data.put("operation",operation);
                        data.put("Id",Ids.get(position));
                        data.put("Status", finalStatus);
                        return data;
                    }
                };
                RequestQueue rq= Volley.newRequestQueue(c);
                rq.add(sr);
            }
        });
        return item;
    }
}
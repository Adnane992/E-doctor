package com.example.e_doctor;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"ch1");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(intent.getStringExtra("Name"));
        builder.setContentText("Dosage : "+intent.getStringExtra("Dosage"));
        builder.setGroup("edoc");
        //////////////////////
        NotificationCompat.Builder summary=new NotificationCompat.Builder(context,"ch1");
        summary.setSmallIcon(R.mipmap.ic_launcher);
        summary.setGroup("edoc");
        summary.setGroupSummary(true);
        summary.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_ALL);
        /////////////////////
        NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
        nmc.notify(-1,summary.build());
        nmc.notify(intent.getIntExtra("id",0),builder.build());
    }

    static public void setAlarm(Context c ,int position,int Hour,int Minute,String Name,String Dosage){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,Hour);
        calendar.set(Calendar.MINUTE,Minute);
        calendar.set(Calendar.SECOND,0);
        if(calendar.getTimeInMillis() <= System.currentTimeMillis()) calendar.add(Calendar.DATE,1);
        AlarmManager am = (AlarmManager)c.getSystemService(c.ALARM_SERVICE);
        Intent intent = new Intent(c,ReminderBroadcast.class);
        intent.putExtra("Name",Name);
        intent.putExtra("Dosage",Dosage);
        intent.putExtra("id",position);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c,position,intent,0);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    static public void removeAlarm(Context c,int notifyId){
        AlarmManager am = (AlarmManager)c.getSystemService(c.ALARM_SERVICE);
        Intent intent = new Intent(c,ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c,notifyId,intent,0);
        am.cancel(pendingIntent);
    }
}

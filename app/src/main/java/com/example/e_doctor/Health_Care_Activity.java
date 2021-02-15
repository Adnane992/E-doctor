package com.example.e_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Health_Care_Activity extends AppCompatActivity {
    RadioButton yes,no,def;
    int id=1,yes_id,no_id,default_id;
    Button next;
    ArrayList<Disease> list;

    public void Fetch(){
        String operation="fetch_diseases";
        list=new ArrayList<Disease>();
        StringRequest sr=new StringRequest(Request.Method.POST, "http://192.168.1.107/E-doctor.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] response_split=response.split("@");
                        int num_rows=Integer.parseInt(response_split[0]);
                        int i=0,j=0,k=0;
                        for(i=0;i<num_rows;i++){
                            list.add(new Disease(Integer.parseInt(response_split[i+1]),"",""));
                            j=i+1;
                        }
                        for(i=j,k=0;i<num_rows*2 && k<num_rows;i++,k++){
                            list.get(k).Name=response_split[i+1];
                            j=i+1;
                        }
                        for(i=j,k=0;i<num_rows*3 && k<num_rows;i++,k++) list.get(k).Description=response_split[i+1];
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> data=new HashMap<String,String>();
                data.put("operation",operation);
                return data;
            }
        };
        RequestQueue rq= Volley.newRequestQueue(Health_Care_Activity.this);
        rq.add(sr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fetch();
        setContentView(R.layout.activity_health__care_);
        String operation="qsts";
        next=findViewById(R.id.Next_HealthCare);
        yes=findViewById(R.id.Choice1_HealthCare);
        def=findViewById(R.id.default_radio);
        no=findViewById(R.id.Choice2_HealthCare);
        TextView question_txt=findViewById(R.id.question);

        StringRequest sr=new StringRequest(Request.Method.POST, "http://192.168.1.107/E-doctor.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String question) {
                        if (question.equals("empty")) {
                            Toast.makeText(Health_Care_Activity.this, "empty question !!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String[] Question=question.split("@"); /// content,default,yes,no
                            question_txt.setText(Question[0]);
                            if(!Question[1].equals("null")) default_id=Integer.parseInt(Question[1]);
                            yes_id=Integer.parseInt(Question[2]);
                            no_id=Integer.parseInt(Question[3]);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Health_Care_Activity.this,"Server error !!", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> data=new HashMap<String,String>();
                data.put("operation",operation);
                data.put("id",String.valueOf(id));
                return data;
            }
        };
        RequestQueue rq= Volley.newRequestQueue(Health_Care_Activity.this);
        rq.add(sr);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Health_Care_Activity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setTitle("Checkup results");
                builder.setIcon(R.drawable.ic_notfound);
                builder.setMessage("Sorry, we could not identify the issue");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Health_Care_Activity.this,MainActivity2.class);
                        startActivity(intent);
                        finish();
                    }
                });
                AlertDialog error_dialog=builder.create();
                if(!yes.isChecked() && !no.isChecked() && !def.isChecked()){
                    Toast.makeText(Health_Care_Activity.this,"No option selected",Toast.LENGTH_SHORT);
                }
                else{
                    if(yes.isChecked()){
                        if(yes_id != 0) id=yes_id;
                        else id=0;
                    }
                    else if(no.isChecked()){
                        if(no_id != 0) id=no_id;
                        else id=0;
                    }
                    else if(def.isChecked()){
                        if(default_id != 0) id=default_id;
                        else id=0;
                    }
                    if(id != 0){
                        /////// test if diseases list contains id
                        boolean isDisease=false;
                        Disease theDisease=null;
                        for(int i=0;i<list.size();i++){
                            if(list.get(i).Id == id){
                                isDisease = true;
                                theDisease=new Disease(id,list.get(i).Name,list.get(i).Description);
                                break;
                            }
                        }
                        if(!isDisease){
                            StringRequest sr2=new StringRequest(Request.Method.POST, "http://192.168.1.107/E-doctor.php",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String question) {
                                            if (question.equals("empty")) {
                                                Toast.makeText(Health_Care_Activity.this, "empty question !!", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                String[] Question=question.split("@"); /// content,default,yes,no
                                                question_txt.setText(Question[0]);
                                                if(!Question[1].equals("null")){
                                                    default_id=Integer.parseInt(Question[1]);
                                                    def.setVisibility(View.VISIBLE);
                                                }
                                                if(!Question[2].equals("null")) yes_id=Integer.parseInt(Question[2]);
                                                else yes_id=0;
                                                if(!Question[3].equals("null")) no_id=Integer.parseInt(Question[3]);
                                                else no_id=0;
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(Health_Care_Activity.this,"Server error !!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            ){
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String,String> data=new HashMap<String,String>();
                                    data.put("operation",operation);
                                    data.put("id",String.valueOf(id));
                                    return data;
                                }
                            };
                            rq.add(sr2);
                            def.setVisibility(View.GONE);
                        }
                        else{
                            Disease _theDisease=theDisease;
                            AlertDialog.Builder builder_=new AlertDialog.Builder(Health_Care_Activity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                            builder_.setTitle("Checkup results");
                            builder_.setIcon(R.drawable.ic_found);
                            builder_.setMessage("You might have : '" + _theDisease.Name + "'\n" + _theDisease.Description);
                            builder_.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(Health_Care_Activity.this,MainActivity2.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            AlertDialog info_dialog=builder_.create();
                            info_dialog.show();
                        }
                    }
                    else error_dialog.show();
                }
            }
        });
    }
}
package com.example.e_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class Login_Activity extends AppCompatActivity {
    TextView Register,about;
    Button connect,regis;
    ImageButton showHide;
    EditText Username,Password;
    static boolean hiddenPassword=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connect=findViewById(R.id.button_login);
        regis=findViewById(R.id.button_Register);
        about=findViewById(R.id.textView1_about);
        Username=findViewById(R.id.User_Login);
        Password=findViewById(R.id.Mdp_Login);
        showHide=findViewById(R.id.showHide);
        about.setPaintFlags(about.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog pd=new ProgressDialog(Login_Activity.this);
                pd.setTitle("Login");
                pd.setMessage("please wait while we log you in");
                pd.setCancelable(false);
                String operation="login";
                String username=Username.getText().toString().trim();
                String password=Password.getText().toString().trim();
                StringRequest sr=new StringRequest(Request.Method.POST, "http://192.168.1.107/E-doctor.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("success")) {
                                    Intent intent_connect = new Intent(Login_Activity.this, MainActivity2.class);
                                    startActivity(intent_connect);
                                    finish();
                                }
                                else{
                                    Toast.makeText(Login_Activity.this, response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Login_Activity.this,"Server error !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String,String> data=new HashMap<String,String>();
                        data.put("operation",operation);
                        data.put("username",username);
                        data.put("password",password);
                        return data;
                    }
                };
                RequestQueue rq= Volley.newRequestQueue(Login_Activity.this);
                rq.add(sr);
            }
        });

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Username.getText().toString().trim();
                String password = Password.getText().toString().trim();
                if(!username.isEmpty() && !password.isEmpty()){
                    String operation = "insert";
                    StringRequest sr=new StringRequest(Request.Method.POST, "http://192.168.1.107/E-doctor.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(Login_Activity.this, response, Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(Login_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    ){
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String,String> data=new HashMap<String,String>();
                            data.put("operation",operation);
                            data.put("username",username);
                            data.put("password",password);
                            return data;
                        }
                    };
                    RequestQueue rq= Volley.newRequestQueue(Login_Activity.this);
                    rq.add(sr);
                }
                else{
                    Toast.makeText(Login_Activity.this, "fill the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Reg=new Intent(Login_Activity.this,About_Activity.class);
                startActivity(intent_Reg);
            }
        });

        showHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hiddenPassword){
                    Password.setTransformationMethod(null);
                    showHide.setImageResource(R.drawable.ic_showpass);
                    hiddenPassword=false;
                }
                else{
                    Password.setTransformationMethod(new PasswordTransformationMethod());
                    showHide.setImageResource(R.drawable.ic_hidepass);
                    hiddenPassword=true;
                }
            }
        });
    }
}
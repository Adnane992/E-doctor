package com.example.e_doctor;

import android.content.Context;
import android.util.Log;
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

public class Disease {
    int Id;
    String Name,Description;

    public Disease(){}
    public Disease(int id, String name, String description) {
        Id = id;
        Name = name;
        Description = description;
    }
}

package com.lit.paytol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RiwayatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RiwayatAdapter riwayatAdapter;
    private ArrayList<Riwayat> riwayatArrayList;
    private SharedPreferences getUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);


        getUser = getSharedPreferences("user",MODE_PRIVATE);
        getData();
        recyclerView = findViewById(R.id.recyclerview);
        riwayatAdapter = new RiwayatAdapter(riwayatArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(riwayatAdapter);
    }

    public void getData() {
        riwayatArrayList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.LOCALHOST_API+"riwayat",  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arrRiwayat = new JSONArray(response);
                    int len = arrRiwayat.length();
                    for (int i=0;i<len;i++){
                        JSONObject jObj = arrRiwayat.getJSONObject(i);
                        String rute = jObj.getString("rute");
                        String tarif = jObj.getString("tarif");
                        String tanggal = jObj.getString("tanggal");
                        String waktu = jObj.getString("waktu");
                        riwayatArrayList.add(new Riwayat(rute,"Rp."+tarif,tanggal,waktu));
                    }
                    recyclerView.setAdapter(riwayatAdapter);
                } catch (JSONException e) {
                    Toast.makeText(RiwayatActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RiwayatActivity.this,"wadu erorr : "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                Map<String, String> pars = new HashMap<String, String>();
                pars.put("Content-Type", "application/x-www-form-urlencoded");
                //return pars;
                return "application/x-www-form-urlencoded";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<String, String>();
                data.put("id", getUser.getString("idUser",null));
                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
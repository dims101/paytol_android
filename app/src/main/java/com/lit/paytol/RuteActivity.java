package com.lit.paytol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class RuteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RuteAdapter ruteAdapter;
    private ArrayList<Rute> ruteArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rute);
        getData();
        recyclerView = findViewById(R.id.recyclerview);
        ruteAdapter = new RuteAdapter(ruteArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( RuteActivity.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(ruteAdapter);
    }

    public void getData(){

        ruteArrayList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestKendaraan = new StringRequest(Request.Method.GET, Server.LOCALHOST_API + "rute", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//
                try {
                    JSONArray arrRute = new JSONArray(response);
                    int len = arrRute.length();
                    for (int i=0;i<len;i++){
                        JSONObject jObj = arrRute.getJSONObject(i);
                        String id = jObj.getString("id");
                        String gateMasuk = jObj.getString("gate_masuk");
                        String gateKeluar = jObj.getString("gate_keluar");
                        String tarifI = jObj.getString("tarif_golongan_i");
                        String tarifII = jObj.getString("tarif_golongan_ii");
                        String tarifIII = jObj.getString("tarif_golongan_iii");
                        String tarifIV = jObj.getString("tarif_golongan_iv");
                        String tarifV = jObj.getString("tarif_golongan_v");
                        ruteArrayList.add(new Rute(id,gateMasuk,gateKeluar,"Rp."+tarifI,"Rp."+tarifII,"Rp."+tarifIII,"Rp."+tarifIV,"Rp."+tarifV));
                    }
                    recyclerView.setAdapter(ruteAdapter);
                } catch (JSONException e) {
                    Toast.makeText(RuteActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RuteActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestKendaraan.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(requestKendaraan);

    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
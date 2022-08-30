package com.lit.paytol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
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

public class InfoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InformasiAdapter informasiAdapter;
    private ArrayList<Informasi> informasiArraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getdata();
        recyclerView = findViewById(R.id.recyclerview);
        informasiAdapter = new InformasiAdapter(informasiArraylist);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(informasiAdapter);
    }

    public void getdata(){
        informasiArraylist = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestInformasi = new StringRequest(Request.Method.GET, Server.LOCALHOST_API + "informasi", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//
                try {
                    JSONArray arrInformasi = new JSONArray(response);
                    int len = arrInformasi.length();
                    for (int i=0;i<len;i++){
                        JSONObject jObj = arrInformasi.getJSONObject(i);
                        String namaInformasi = jObj.getString("nama_informasi");
                        String keterangan = jObj.getString("keterangan");
                        informasiArraylist.add(new Informasi(namaInformasi,keterangan));
                    }
                    recyclerView.setAdapter(informasiAdapter);
                } catch (JSONException e) {
                    Toast.makeText(InfoActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestInformasi.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(requestInformasi);

    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    public void tentang(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                InfoActivity.this
        );
        builder.setTitle("Tentang payTol");
        builder.setMessage("Aplikasi pembayaran tol dengan sistem scan QRCode untuk memudahkan pengguna tol dalam pembayaran.\ncreated by lit studio");
        builder.setPositiveButton("Yeay!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
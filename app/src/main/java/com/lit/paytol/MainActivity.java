package com.lit.paytol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SharedPreferences getUser,getGate;
    SharedPreferences.Editor setGate;
    TextView textSaldo,textGate;
    CardView scan,cvGate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textSaldo = findViewById(R.id.saldo);
        textGate = findViewById(R.id.textGate);
        scan = findViewById(R.id.scan);
        cvGate = findViewById(R.id.cvGate);

        getUser = getSharedPreferences("user",MODE_PRIVATE);
        getGate = getSharedPreferences("gate",MODE_PRIVATE);
        setGate = getSharedPreferences("gate",MODE_PRIVATE).edit();

        if(getUser.contains("idUser")){
            getSaldo();

            if (getGate.contains("idGateMasuk")){
                textGate.setText(getGate.getString("namaGate",null));
                cvGate.setVisibility(View.VISIBLE);

            } else {

                cvGate.setVisibility(View.INVISIBLE);
            }
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    IntentIntegrator intentIntegrator = new IntentIntegrator(
                            MainActivity.this
                    );
                    intentIntegrator.setPrompt("Gunakan tombol volume atas untuk menyalakan flash");
                    intentIntegrator.setBeepEnabled(true);
                    intentIntegrator.setOrientationLocked(true);
                    intentIntegrator.setCaptureActivity(Capture.class);
                    intentIntegrator.initiateScan();
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT);
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );
        if(intentResult.getContents() != null){

//            getGate.putString("gate",intentResult.getContents().toString());
//            textGate.setText(intentResult.getContents().toString());
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.POST, Server.LOCALHOST_API + "gate", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject objGate = new JSONObject(response);
                        if(objGate.has("data")){
                            String dataGate = objGate.getString("data");
                            JSONObject objData = new JSONObject(dataGate);
                            String idGate = objData.getString("id");
                            String namaGate = objData.getString("nama");

                            if(getGate.contains("idGateMasuk")){
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.LOCALHOST_API+"transaksi/store",  new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject objTransaksi = new JSONObject(response);
                                            String message = objTransaksi.getString("message");
                                            String success = objTransaksi.getString("success");
                                            if(success.equals("1")){
                                                String tarif = objTransaksi.getString("tarif");
                                                String sisaSaldo = objTransaksi.getString("sisa_saldo");
                                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                                        MainActivity.this
                                                );
                                                builder.setTitle("Keluar Gerbang Tol "+namaGate);
                                                builder.setMessage(message+"\nTarif : "+tarif+"\nSisa saldo : "+sisaSaldo);
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                        getSaldo();
                                                    }
                                                });
                                                builder.show();
                                                cvGate.setVisibility(View.INVISIBLE);
                                                getGate.edit().clear().commit();
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                                        MainActivity.this
                                                );
                                                builder.setTitle(message);
                                                builder.setMessage("Silahkan melakukan topup untuk dapat melakukan pembayaran.");
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                });
                                                builder.show();
                                            }


                                        } catch (JSONException e) {
                                            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this,"wadu erorr : "+error.toString(), Toast.LENGTH_SHORT).show();
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
                                        data.put("id_gate_masuk", getGate.getString("idGateMasuk",null));
                                        data.put("id_gate_keluar",idGate);
                                        data.put("id_user",getUser.getString("idUser",null));
                                        return data;
                                    }
                                };
                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                        10000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(stringRequest);

                            } else {
                                //jika belum scan masuk
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        MainActivity.this
                                );
                                builder.setTitle("Masuk Gerbang Tol "+namaGate);
                                builder.setMessage("Berhasil masuk!\nPatuhi rambu yang berlaku dan selamat berkendara.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.show();

                                setGate.putString("idGateMasuk",idGate);
                                setGate.putString("namaGate", namaGate);
                                setGate.apply();

                                textGate.setText(namaGate);
                                cvGate.setVisibility(View.VISIBLE);
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    MainActivity.this
                            );
                            builder.setTitle("Gate tidak ditemukan");
                            builder.setMessage("Silahkan scan QRCode pada gate yang terdaftar!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                    data.put("kode_barcode", intentResult.getContents());
                    return data;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(request);


        } else {
            Toast.makeText(getApplicationContext(), "QRCode tidak ditemukan",Toast.LENGTH_SHORT).show();
        }
    }
    public void getSaldo(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.LOCALHOST_API+"saldo",  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                textSaldo.setText("Rp. "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"wadu erorr : "+error.toString(), Toast.LENGTH_SHORT).show();
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
    public void onResume(){
        super.onResume();
        getSaldo();
    }

    public void rute(View view) {
        startActivity(new Intent(getApplicationContext(),RuteActivity.class));

    //        getUser.edit().clear().commit();
    //        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    //        finish();
    }

    public void profil(View view) {
//        getUser.edit().clear().commit();
        startActivity(new Intent(getApplicationContext(),ProfilActivity.class));
//        finish();
    }

    public void riwayat(View view) {
        startActivity(new Intent(getApplicationContext(),RiwayatActivity.class));
    }

    public void informasi(View view) {
        startActivity(new Intent(getApplicationContext(),InfoActivity.class));
    }

    public void topup(View view) {
        startActivity(new Intent(getApplicationContext(),TopupActivity.class));
    }
}
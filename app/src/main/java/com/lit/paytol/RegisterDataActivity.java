package com.lit.paytol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Map;

public class RegisterDataActivity extends AppCompatActivity {

    SharedPreferences getRegisterUser;
    EditText etNama, etEmail, etTelepon;
    Spinner spKendaraan;
    ArrayList<String> listKendaraan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_data);
        getRegisterUser = getSharedPreferences("registeredUser",MODE_PRIVATE);
        etNama = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        etTelepon = findViewById(R.id.etTelepon);
        spKendaraan = findViewById(R.id.spKendaraan);
        listKendaraan = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestKendaraan = new StringRequest(Request.Method.GET, Server.LOCALHOST_API + "kendaraan", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray arrKendaraan = new JSONArray(response);
                    int len = arrKendaraan.length();
                    for (int i=0;i<len;i++){
                        listKendaraan.add(arrKendaraan.get(i).toString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,listKendaraan);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spKendaraan.setAdapter(adapter);

                } catch (JSONException e) {
                    Toast.makeText(RegisterDataActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterDataActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestKendaraan.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestKendaraan);

    }// tutup onCreate

    public void register(View view) {
        if(TextUtils.isEmpty(etNama.getText())){
            etNama.setError("Masukan nama");
            etNama.requestFocus();
        } else if(TextUtils.isEmpty(etEmail.getText())){
            etEmail.setError("Masukan email");
            etEmail.requestFocus();
        } else if(TextUtils.isEmpty(etTelepon.getText())) {
            etTelepon.setError("Masukan nomor telepon");
            etTelepon.requestFocus();
        } else {
            RequestQueue queue = Volley.newRequestQueue(this);
//            Toast.makeText(this, getRegisterUser.getString("username",null) + " " + getRegisterUser.getString("password",null), Toast.LENGTH_SHORT).show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.LOCALHOST_API+"register",  new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObject = new JSONObject(response);
                        String success = jObject.getString("success");
                        if(success.equals("1")){
                            Toast.makeText(RegisterDataActivity.this, jObject.getString("message").toString(), Toast.LENGTH_LONG).show();
                            getRegisterUser.edit().clear().commit();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            finish();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"wadu erorr : "+error.toString(), Toast.LENGTH_SHORT).show();
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
                    data.put("username", getRegisterUser.getString("username",null));
                    data.put("password",getRegisterUser.getString("password",null));
                    data.put("nama",etNama.getText().toString());
                    data.put("email",etEmail.getText().toString());
                    data.put("telepone",etTelepon.getText().toString());
                    data.put("kendaraan",spKendaraan.getSelectedItem().toString());
                    return data;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);

        }
    }

    public void back(View view) {
//    getRegisterUser.edit().clear().commit();
    startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    finish();
    }
}
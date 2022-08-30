package com.lit.paytol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TopupActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView textSaldo;
    EditText etTopup;
    SharedPreferences getUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        btnBack = findViewById(R.id.btnBack);
        textSaldo = findViewById(R.id.textSaldo);
        etTopup = findViewById(R.id.etTopup);
        getUser = getSharedPreferences("user",MODE_PRIVATE);
        getSaldo();
    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    public void getSaldo(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.LOCALHOST_API+"saldo",  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                textSaldo.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TopupActivity.this,"wadu erorr : "+error.toString(), Toast.LENGTH_SHORT).show();
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
    public void topup(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.LOCALHOST_API+"topup",  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String nominal = jsonObject.getString("nominal");
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            TopupActivity.this
                    );
                    builder.setTitle(message);
                    builder.setMessage("Berhasil melakukan topup sebesar\n"+nominal);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            etTopup.setText("");
                            getSaldo();
                        }
                    });
                    builder.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TopupActivity.this,"wadu erorr : "+error.toString(), Toast.LENGTH_SHORT).show();
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
                data.put("nominal",etTopup.getText().toString());
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
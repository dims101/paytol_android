package com.lit.paytol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    SharedPreferences.Editor setUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        setUser = getSharedPreferences("user", MODE_PRIVATE).edit();
    }

    public void login(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        if(TextUtils.isEmpty(etUsername.getText())){
            etUsername.setError("Masukan username");
            etUsername.requestFocus();
        } else if(TextUtils.isEmpty(etPassword.getText())){
            etPassword.setError("Masukan password");
            etPassword.requestFocus();
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.LOCALHOST_API+"login",  new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jObject = new JSONObject(response);
                        if(jObject.has("success")){
                            String success = jObject.getString("success");
                            String message = jObject.getString("message");
                            if (success.equals("1")){
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                String stringUser = jObject.getString("data");
                                JSONObject dataUser = new JSONObject(stringUser);
                                String idUser = dataUser.getString("id");
                                setUser.putString("idUser",idUser);
                                setUser.apply();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }

//                        String data_user = jObject.getString("data");
//                        JSONObject user = new JSONObject(data_user);
//                        String nama = user.getString("nama");
//
//                        setDataUser.putString("id",user.getString("id"));
//                        setDataUser.putString("username",user.getString("username"));
//                        setDataUser.apply();
//                        Toast.makeText(MainActivity.this,"Selamat datang "+getDataUser.getString("username", null)+"!",Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(getApplicationContext(),Dashboard.class));
//                        finish();

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
                    data.put("username",etUsername.getText().toString());
                    data.put("password",etPassword.getText().toString());
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

    public void register(View view) {
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
        finish();
    }
}
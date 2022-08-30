package com.lit.paytol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity {
    SharedPreferences getUser;
    EditText etNama, etEmail, etTelepon,etPassword,etReenterpassword;
    Spinner spKendaraan;
    ArrayList<String> listKendaraan;
    ImageView imageProfile;
    TextView textUbah;
    String sImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        getUser = getSharedPreferences("user",MODE_PRIVATE);
        etNama = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        etTelepon = findViewById(R.id.etTelepon);
        etPassword = findViewById(R.id.etPassword);
        etReenterpassword = findViewById(R.id.etReenterpassword);
        spKendaraan = findViewById(R.id.spKendaraan);
        imageProfile = findViewById(R.id.imageProfile);
        textUbah = findViewById(R.id.textUbah);

        listKendaraan = new ArrayList<>();
        getDataUser();
        etReenterpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                verifyPassword();
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                verifyPassword();
            }
        });

        textUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(ProfilActivity.this
                , Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ProfilActivity.this
                    ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                    ,100);
                } else {
                    selectImage();
                }
            }
        });

    }
    //gambar dari sini
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
//        startActivityForResult(Intent.createChooser(intent, "Select Image"),100);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),100);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectImage();
        } else {
            Toast.makeText(this, "Izin ditolak!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 &&resultCode== RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                Bitmap rawbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                Bitmap bitmap = getResizedBitmap(rawbitmap,320);
                ByteArrayOutputStream stream =new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,90,stream);
                byte[] bytes = stream.toByteArray();
                sImage = Base64.encodeToString(bytes,Base64.DEFAULT);
                if(!sImage.equals("")){
                    RequestQueue queue = Volley.newRequestQueue(this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.LOCALHOST_API+"updatephoto",  new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                getDataUser();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ProfilActivity.this,"wadu erorr : "+error.toString(), Toast.LENGTH_SHORT).show();
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
                            data.put("profile",sImage.toString());
                            return data;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(stringRequest);
                } else {
                    Toast.makeText(getApplicationContext(),"Tidak ada gambar yang dipilih", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void verifyPassword(){
        if(!etPassword.getText().toString().equals(etReenterpassword.getText().toString())){
            etReenterpassword.setError("Password tidak cocok");
        }
    }
    public void back(View view) {
//    getRegisterUser.edit().clear().commit();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    public void logout(View view) {
        getUser.edit().clear().commit();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }
    public void simpanProfil(View view){
        if(!etPassword.getText().toString().equals(etReenterpassword.getText().toString())){
            Toast.makeText(this, "Password tidak cocok.", Toast.LENGTH_SHORT).show();
        } else {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.LOCALHOST_API+"updateprofile",  new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObject = new JSONObject(response);
                        String success = jObject.getString("success");
                        String message = jObject.getString("message");
                        if(success.equals("1")){
                            Toast.makeText(ProfilActivity.this, message, Toast.LENGTH_SHORT).show();
                            getDataUser();
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
                    data.put("id", getUser.getString("idUser",null));
                    data.put("nama",etNama.getText().toString());
                    data.put("email",etEmail.getText().toString());
                    data.put("telepone",etTelepon.getText().toString());
                    data.put("kendaraan",spKendaraan.getSelectedItem().toString());
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
    //iki get data user
    public void getDataUser(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.LOCALHOST_API+"getuser",  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseUser = new JSONObject(response);
                    String user = responseUser.getString("data");
                    JSONObject dataUser = new JSONObject(user);
                    String nama = dataUser.getString("nama");
                    String email = dataUser.getString("email");
                    String telepon = dataUser.getString("telepone");
                    String kendaraan = dataUser.getString("kendaraan");
                    String profil = dataUser.getString("profil");

                    Toast.makeText(getApplicationContext(),profil,Toast.LENGTH_SHORT).show();
                    if (profil.isEmpty() || profil.equals("") || profil.length() == 0 || profil == null){
                        profil = "default-profile.png";
                    }
                    getProfile(profil);
                    etNama.setText(nama);
                    etEmail.setText(email);
                    etTelepon.setText(telepon);
                    getListKendaraan(kendaraan);

                } catch (JSONException e) {
                    Toast.makeText(ProfilActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfilActivity.this,"wadu erorr : "+error.toString(), Toast.LENGTH_SHORT).show();
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
                data.put("idUser", getUser.getString("idUser",null));
                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
    public void getListKendaraan(String kendaraan){
        listKendaraan.clear();
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
                    if (kendaraan != null) {
                        int spinnerPosition = adapter.getPosition(kendaraan);
                        spKendaraan.setSelection(spinnerPosition);
                    }

                } catch (JSONException e) {
                    Toast.makeText(ProfilActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfilActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestKendaraan.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestKendaraan);
    }
    public void getProfile(String profil){
        String urlLink = Server.LOCALHOST_PUBLIC+"img/profile/"+profil;

        LoadImage loadImage= new LoadImage(imageProfile);
        loadImage.execute(urlLink);
    }

    private class LoadImage extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;
        public LoadImage(ImageView imageProfile) {
            this.imageView = imageProfile;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlLink = strings[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new java.net.URL(urlLink).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageProfile.setImageBitmap(bitmap);
        }
    }
}
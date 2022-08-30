package com.lit.paytol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText etUsername,etPassword,etReenterpassword;
    Button btnRegister;
    TextView passwordNotMatch;
    SharedPreferences.Editor setRegisterUser;
    SharedPreferences getRegisterUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        passwordNotMatch = findViewById(R.id.passwordNotMatch);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etReenterpassword = findViewById(R.id.etReenterpassword);
        btnRegister = findViewById(R.id.btnRegister);
        setRegisterUser = getSharedPreferences("registeredUser",MODE_PRIVATE).edit();
        getRegisterUser = getSharedPreferences("registeredUser",MODE_PRIVATE);
        if(!getRegisterUser.contains(null)){
            etUsername.setText(getRegisterUser.getString("username",null));
        }
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
    }

    public void verifyPassword(){
        if(!etPassword.getText().toString().equals(etReenterpassword.getText().toString())){
            passwordNotMatch.setText(getString(R.string.passwordNotMatch));
        } else {
            passwordNotMatch.setText("");
        }
    }

    public void register(View view) {
        if(TextUtils.isEmpty(etUsername.getText())){
            etUsername.setError("Masukan username");
            etUsername.requestFocus();
        } else if(TextUtils.isEmpty(etPassword.getText())){
            etPassword.setError("Masukan password");
            etPassword.requestFocus();
        } else if(TextUtils.isEmpty(etReenterpassword.getText())) {
            etReenterpassword.setError("Masukan password kembali");
            etReenterpassword.requestFocus();
        } else {
            if(!passwordNotMatch.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Password tidak cocok",Toast.LENGTH_SHORT).show();
            } else {
                setRegisterUser.putString("username",etUsername.getText().toString());
                setRegisterUser.putString("password",etPassword.getText().toString());
                setRegisterUser.apply();
                startActivity(new Intent(getApplicationContext(),RegisterDataActivity.class));
                finish();
            }
        }

    }

    public void backToLogin(View view) {
        getRegisterUser.edit().clear().commit();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}
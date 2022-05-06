package com.example.drunkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = RegActivity.class.getName();
    private static final String PREF = RegActivity.class.getPackage().toString();
    private static final int KEY = 1999;

    private SharedPreferences prefs;
    private FirebaseAuth auth;

    EditText uName;
    EditText uEmail;
    EditText uPassword;
    EditText uRePassword;
    EditText uPhone;
    Spinner spinner;
    EditText uAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        int key = getIntent().getIntExtra("KEY", 0);

        if(key != 2022){ finish();}

        uName = findViewById(R.id.userName);
        uEmail = findViewById(R.id.userEmail);
        uPassword = findViewById(R.id.userPassword);
        uRePassword = findViewById(R.id.userRePassword);
        uPhone = findViewById(R.id.userMobile);
        spinner = findViewById(R.id.orderType);
        uAddress = findViewById(R.id.userAddress);

        prefs = getSharedPreferences(PREF, MODE_PRIVATE);
        String email = prefs.getString("email", "");
        String password = prefs.getString("password", "");

        uEmail.setText(email);
        uPassword.setText(password);
        uRePassword.setText(password);

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.order_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
    }

    public void reg(View view) {
        String name = uName.getText().toString();

        if(name.length() < 5){
            Toast.makeText(RegActivity.this, "Túl rövid név", Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, "Tul rovid nev");
            return;
        }

        String email = uEmail.getText().toString();
        String password = uPassword.getText().toString();

        if(password.length() < 6){
            Toast.makeText(RegActivity.this, "Túl rövid jelszo", Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, "Tul rovid jelszo");
        }

        String rePassword = uRePassword.getText().toString();

        if(!password.equals(rePassword)){
            Toast.makeText(RegActivity.this, "Eltérő jelszavak", Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, "Eltérő jelszavak");
            return;
        }

        String phone = uPhone.getText().toString();

        if(phone.length() < 5){
            Toast.makeText(RegActivity.this, "Túl rövid telószám", Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, "Tul rovid mobile");
            return;
        }
        String orderType = spinner.getSelectedItem().toString();
        String address = uAddress.getText().toString();

        if(address.length() < 5){
            Toast.makeText(RegActivity.this, "Túl rövid cím", Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, "Tul rovid address");
            return;
        }

        Log.i(LOG_TAG, "Sikeres regisztráció" + name + email + password + rePassword);
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG,"Sikeres user regisztracio");
                    Toast.makeText(RegActivity.this, "Sikeres regisztráció", Toast.LENGTH_SHORT).show();
                    letsGetDrunk();
                } else {
                    Log.d(LOG_TAG, "Valami gebasz tortent a bejelentkezes soran");
                    Toast.makeText(RegActivity.this, "Sikertelen regisztráció", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void back(View view) {
        finish();
    }

    private void letsGetDrunk(){
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selected = adapterView.getItemAtPosition(i).toString();
        Log.i(LOG_TAG, selected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
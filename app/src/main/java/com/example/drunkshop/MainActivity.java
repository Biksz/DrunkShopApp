package com.example.drunkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF = MainActivity.class.getPackage().toString();
    private static final int KEY = 2022;

    private SharedPreferences prefs;
    private FirebaseAuth auth;

    EditText uName;
    EditText uPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uName = findViewById(R.id.editTextUserName);
        uPassword = findViewById(R.id.editTextPassword);

        prefs = getSharedPreferences(PREF, MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();

        getSupportLoaderManager().restartLoader(0, null, this);
    }

    public void login(View view) {

        String name = uName.getText().toString();
        String password = uPassword.getText().toString();

        Log.i(LOG_TAG, "Sikeres bejelentkezes" + name + password);
        auth.signInWithEmailAndPassword(name, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "Sikeres bejelentkezés");
                    Toast.makeText(MainActivity.this, "Sikeres bejelentkezés", Toast.LENGTH_SHORT).show();
                    letsGetDrunk();
                } else {
                    Log.d(LOG_TAG, "Sikertelen bejelentkezés");
                    Toast.makeText(MainActivity.this, "Sikertelen bejelentkezés", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void reg(View view) {
        Intent intent = new Intent(this, RegActivity.class);
        intent.putExtra("KEY", KEY);
        startActivity(intent);
    }

    private void letsGetDrunk(){
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("name", uName.getText().toString());
        edit.putString("password", uPassword.getText().toString());
        edit.apply();
    }

    public void guestLogin(View view) {
        auth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "Anonim belépés");
                    Toast.makeText(MainActivity.this, "Üdvözöllek vendég", Toast.LENGTH_SHORT).show();
                    letsGetDrunk();
                } else {
                    Log.d(LOG_TAG, "Nem sikerült belépni");
                    Toast.makeText(MainActivity.this, "Sikertelen belépés", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        Button btn = findViewById(R.id.loginButton);
        btn.setText(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
    }
}
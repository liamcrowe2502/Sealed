package com.liam.sealed;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this,login.class);
            startActivity(intent);
        }
    }
}
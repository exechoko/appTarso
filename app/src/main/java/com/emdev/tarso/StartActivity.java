package com.emdev.tarso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    Button login, register;

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //check if user is null
        if (firebaseUser != null){
            Intent intent = new Intent(StartActivity.this, MainChatActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        StartActivity.this.setTitle("Tarso Chat");
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0080B3"));
        Objects.requireNonNull(StartActivity.this.getSupportActionBar()).setBackgroundDrawable(colorDrawable);
        Objects.requireNonNull(StartActivity.this.getSupportActionBar()).setElevation(0f);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                Toast.makeText(StartActivity.this, "Para registrarse vaya a los menu Estudiante o Profesor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
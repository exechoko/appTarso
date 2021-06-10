package com.emdev.tarso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPaswordActivity extends AppCompatActivity {

    EditText edtCorreo;
    Button btnRestaurar;

    private FirebaseAuth mAuth;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pasword);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        edtCorreo = findViewById(R.id.edtCorreo);
        btnRestaurar = findViewById(R.id.btnRestaurar);
        
        btnRestaurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCorreo.getText().toString().equals("")){
                    Toast.makeText(ResetPaswordActivity.this, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
                } else {
                    mDialog.setMessage("Espere un momento...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    restaurarClave(edtCorreo.getText().toString());
                }
            }
        });

    }

    private void restaurarClave(String email) {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(ResetPaswordActivity.this, "Se ha enviado un correo para reestablecer su contraseña", Toast.LENGTH_SHORT).show();
                    Intent irAlPrincipio = new Intent(ResetPaswordActivity.this, MainActivity.class);
                    startActivity(irAlPrincipio);
                    finish();
                } else {
                    Toast.makeText(ResetPaswordActivity.this, "No se pudo enviar el correo para reestablecer contraseña", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
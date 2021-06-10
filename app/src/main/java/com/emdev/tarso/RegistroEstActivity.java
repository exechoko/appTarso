package com.emdev.tarso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.emdev.tarso.model.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistroEstActivity extends AppCompatActivity {

    EditText edtNombre, edtDni, edtCorreo, edtPass, edtRepass, edtTelefono;
    Button btnRegistrarse;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_est);

        edtNombre = findViewById(R.id.edtNombreEst);
        edtDni = findViewById(R.id.edtDniEst);
        edtCorreo = findViewById(R.id.edtEmailEst);
        edtPass = findViewById(R.id.edtPassEst);
        edtRepass = findViewById(R.id.edtRePassEst);
        edtTelefono = findViewById(R.id.edtTelefonoEst);
        btnRegistrarse = findViewById(R.id.btnRegistrarEst);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNombre.getText().toString().equals("") ||
                        edtDni.getText().toString().equals("") ||
                        edtCorreo.getText().toString().equals("") ||
                        edtPass.getText().toString().equals("") ||
                        edtRepass.getText().toString().equals("") ||
                        edtTelefono.getText().toString().equals("")){
                    Toast.makeText(RegistroEstActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                } else if (edtTelefono.getText().toString().length() < 10 || edtTelefono.getText().toString().length() > 10){
                    Toast.makeText(RegistroEstActivity.this, "Nro de Teléfono inválido \n(sin 0 ni 15)", Toast.LENGTH_SHORT).show();
                } else if (!edtPass.getText().toString().equals(edtRepass.getText().toString())){
                    Toast.makeText(RegistroEstActivity.this, "No coinciden las contraseñas", Toast.LENGTH_SHORT).show();
                } else if (edtPass.getText().toString().length() < 6){
                    Toast.makeText(RegistroEstActivity.this, "Su contraseña es muy corta,\n necesita mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    crearUsuario(edtCorreo.getText().toString(), edtPass.getText().toString());
                }
            }
        });
    }

    private void crearUsuario(String correo, String pass) {
        mAuth.createUserWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AUTHENTICATION", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Crea la cuenta estudiante y la agrega a la coleccion Usuarios
                            Usuarios estudiante = new Usuarios(edtNombre.getText().toString(), pass, edtDni.getText().toString(), correo, "NO", edtTelefono.getText().toString(), user.getUid());
                            String uid = user.getUid();
                            db.collection("Usuarios").document(uid).set(estudiante);

                            //Una vez crea la cuenta redigir al Menu Estudiante pasame ID para cargar perfil
                            Intent irAMenuEstudiante = new Intent(RegistroEstActivity.this,MenuEstudiantesActivity.class);
                            irAMenuEstudiante.putExtra("IDESTUDIANTE", mAuth.getCurrentUser().getUid());
                            startActivity(irAMenuEstudiante);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AUTHENTICATION", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistroEstActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
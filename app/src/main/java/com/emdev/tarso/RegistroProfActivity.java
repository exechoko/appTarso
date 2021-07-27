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

import com.emdev.tarso.model.SolicitudProfesor;
import com.emdev.tarso.model.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistroProfActivity extends AppCompatActivity {

    EditText edtNombreProf, edtDniProf, edtCorreoProf, edtPassProf, edtRepassProf, edtTelefonoProf;
    Button btnRegistrarseProf;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_prof);
        RegistroProfActivity.this.setTitle("Reg. Profesores");

        edtNombreProf = findViewById(R.id.edtNombreProf);
        edtDniProf = findViewById(R.id.edtDniProf);
        edtCorreoProf = findViewById(R.id.edtEmailProf);
        edtPassProf = findViewById(R.id.edtPassProf);
        edtRepassProf = findViewById(R.id.edtRePassProf);
        edtTelefonoProf = findViewById(R.id.edtTelefonoProf);
        btnRegistrarseProf = findViewById(R.id.btnRegistrarProf);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegistrarseProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNombreProf.getText().toString().equals("") ||
                        edtDniProf.getText().toString().equals("") ||
                        edtCorreoProf.getText().toString().equals("") ||
                        edtPassProf.getText().toString().equals("") ||
                        edtRepassProf.getText().toString().equals("") ||
                        edtTelefonoProf.getText().toString().equals("")){
                    Toast.makeText(RegistroProfActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                } else if (edtTelefonoProf.getText().toString().length() < 10 || edtTelefonoProf.getText().toString().length() > 10){
                    Toast.makeText(RegistroProfActivity.this, "Nro de Teléfono inválido \n(sin 0 ni 15)", Toast.LENGTH_SHORT).show();
                } else if (!edtPassProf.getText().toString().equals(edtRepassProf.getText().toString())){
                    Toast.makeText(RegistroProfActivity.this, "No coinciden las contraseñas", Toast.LENGTH_SHORT).show();
                } else if (edtPassProf.getText().toString().length() < 6){
                    Toast.makeText(RegistroProfActivity.this, "Su contraseña es muy corta,\n necesita mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    crearUsuario(edtCorreoProf.getText().toString(), edtPassProf.getText().toString());
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
                            Usuarios estudiante = new Usuarios(edtNombreProf.getText().toString(), pass, edtDniProf.getText().toString(), correo, "NO", edtTelefonoProf.getText().toString(), user.getUid());
                            SolicitudProfesor nuevaSolicitud = new SolicitudProfesor(edtNombreProf.getText().toString(),edtDniProf.getText().toString(), correo, edtTelefonoProf.getText().toString(), user.getUid());
                            String uid = user.getUid();
                            db.collection("Usuarios").document(uid).set(estudiante);
                            db.collection("SolicitudesProfesores").document(uid).set(nuevaSolicitud);

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                            reference.setValue(estudiante);

                            Toast.makeText(RegistroProfActivity.this, "Espere a que se verifique su cuenta como Docente.\nSe le informará a su nro telefónico o su correo electrónico.\nMuchas Gracias.", Toast.LENGTH_LONG).show();

                            //Una vez crea la cuenta redigir al Menu Principal
                            Intent irAlPrincipio = new Intent(RegistroProfActivity.this,MainActivity.class);
                            startActivity(irAlPrincipio);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AUTHENTICATION", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistroProfActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
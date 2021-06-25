package com.emdev.tarso.ui.slideshow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.emdev.tarso.MenuDocentesActivity;
import com.emdev.tarso.R;
import com.emdev.tarso.RegistroEstActivity;
import com.emdev.tarso.RegistroProfActivity;
import com.emdev.tarso.ResetPaswordActivity;
import com.emdev.tarso.model.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    private static final String TAG = "FIRESTORE PROFESOR";
    FirebaseFirestore db;
    Usuarios user;
    String esProfesor = "";
    private FirebaseAuth mAuth;

    ProgressDialog mDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);


        mDialog = new ProgressDialog(getContext());

        EditText emailProf, passProf;
        Button btnIngresarProf, btnRegistrarProf;
        TextView olvideClaveProf;

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Init Firebase
        /*database = FirebaseDatabase.getInstance();
        tabla_user = database.getReference("User");*/

        emailProf = root.findViewById(R.id.edtEmailProf);
        passProf = root.findViewById(R.id.edtPassProf);
        btnIngresarProf = root.findViewById(R.id.btnIngresarProf);
        btnRegistrarProf = root.findViewById(R.id.btnRegistrarProf);
        olvideClaveProf = root.findViewById(R.id.olvidoClaveProf);

        btnIngresarProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.setMessage("Iniciando sesión...");
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();

                comprobarProfesor_IniciarSesion(emailProf.getText().toString(), passProf.getText().toString());

            }
        });

        btnRegistrarProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Registrarse como profesor", Toast.LENGTH_SHORT).show();
                Intent registrarDocente = new Intent(getActivity(), RegistroProfActivity.class);
                startActivity(registrarDocente);
            }
        });

        olvideClaveProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Olvide clave de profesor", Toast.LENGTH_SHORT).show();
                Intent restaurarClave = new Intent(getActivity(), ResetPaswordActivity.class);
                startActivity(restaurarClave);
            }
        });

        return root;
    }

    private void iniciarSesion(String email, String pass) {

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (esProfesor.equals("YES")){
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Ingreso correcto", Toast.LENGTH_SHORT).show();
                                Intent irMenuDocentes = new Intent(getContext(), MenuDocentesActivity.class);
                                irMenuDocentes.putExtra("IDDOCENTE", mAuth.getCurrentUser().getUid());
                                startActivity(irMenuDocentes);
                            } else {
                                Toast.makeText(getActivity(), "Fallo el ingreso, verifique CORREO y CONTRASEÑA", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Fallo el ingreso porque necesita una cuenta Profesor", Toast.LENGTH_SHORT).show();
                        }
                        mDialog.dismiss();
                    }
                });
    }

    private void comprobarProfesor_IniciarSesion(String e, String p) {
        if (e.equals("")|| p.equals("")){
            mDialog.dismiss();
            Toast.makeText(getActivity(), "Ingrese su clave y contraseña", Toast.LENGTH_SHORT).show();
        } else {
            db.collection("Usuarios")
                    .whereEqualTo("correo", e)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    user = document.toObject(Usuarios.class);
                                    esProfesor = user.getIsProfesor();
                                    Log.d(TAG, esProfesor);

                                    iniciarSesion(e, p);

                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }


    }
/*
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && esProfesor.equals("YES")) {
            // User is signed in
            Intent irMenuDocente = new Intent(getContext(), MenuDocentesActivity.class);
            irMenuDocente.putExtra("IDDOCENTE", mAuth.getCurrentUser().getUid());
            startActivity(irMenuDocente);
        } else {
            // No user is signed in
        }
    }*/

    @Override
    public void onStop() {
        super.onStop();
    }
}
package com.emdev.tarso.ui.gallery;

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

import com.emdev.tarso.MenuEstudiantesActivity;
import com.emdev.tarso.R;
import com.emdev.tarso.RegistroEstActivity;
import com.emdev.tarso.ResetPaswordActivity;
import com.emdev.tarso.model.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class GalleryFragment extends Fragment {

    private static final String TAG = "FIRESTORE";
    private GalleryViewModel galleryViewModel;

    FirebaseFirestore db;
    Usuarios user;

    String esProfesor = "";

    ProgressDialog mDialog;

    private FirebaseAuth mAuth;
    /*FirebaseDatabase database;
    DatabaseReference tabla_user;*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        EditText email, pass;
        Button btnIngresar, btnRegistrarEst;
        TextView olvideClaveEst;


        mDialog = new ProgressDialog(getContext());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = root.findViewById(R.id.edtEmail);
        pass = root.findViewById(R.id.edtPass);
        btnIngresar = root.findViewById(R.id.btnIngresar);
        btnRegistrarEst = root.findViewById(R.id.btnRegistrarEst);
        olvideClaveEst = root.findViewById(R.id.olvideClaveEst);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comprobarProfesor(email.getText().toString());

                mDialog.setMessage("Iniciando sesión...");
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();

                if (email.getText().toString().equals("") || pass.getText().toString().equals("")){
                    mDialog.dismiss();
                    Toast.makeText(getActivity(), "Ingrese su clave y contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    iniciarSesion(email.getText().toString(), pass.getText().toString());
                }

            }
        });

        btnRegistrarEst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Registrarse", Toast.LENGTH_SHORT).show();
                Intent registrarEstudiante = new Intent(getActivity(), RegistroEstActivity.class);
                startActivity(registrarEstudiante);
            }
        });

        olvideClaveEst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Olvide clave", Toast.LENGTH_SHORT).show();
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

                        if (esProfesor.equals("NO")){
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Es estudiante");

                                Toast.makeText(getActivity(), "Ingreso correcto", Toast.LENGTH_SHORT).show();
                                Intent irMenuEstudent = new Intent(getContext(), MenuEstudiantesActivity.class);
                                irMenuEstudent.putExtra("IDESTUDIANTE", mAuth.getCurrentUser().getUid());
                                startActivity(irMenuEstudent);
                            } else {
                                Toast.makeText(getActivity(), "Fallo el ingreso, verifique CORREO y CONTRASEÑA", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "Es profesor");
                            Toast.makeText(getActivity(), "Fallo el ingreso porque necesita una cuenta Estudiante", Toast.LENGTH_SHORT).show();
                        }
                        mDialog.dismiss();


                    }
                });
    }

    private void comprobarProfesor(String e) {

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

                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /*@Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && esProfesor.equals("NO")) {
            // User is signed in
            Intent irMenuEstudent = new Intent(getContext(), MenuEstudiantesActivity.class);
            irMenuEstudent.putExtra("IDESTUDIANTE", mAuth.getCurrentUser().getUid());
            startActivity(irMenuEstudent);
        } else {
            // No user is signed in
        }
    }*/

    @Override
    public void onStop() {
        super.onStop();
    }
}
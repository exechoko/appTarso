package com.example.tarso.ui.slideshow;

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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tarso.MenuDocentesActivity;
import com.example.tarso.MenuEstudiantesActivity;
import com.example.tarso.R;
import com.example.tarso.model.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    private static final String TAG = "FIRESTORE PROFESOR";
    FirebaseFirestore db;
    Usuarios user;
    String esProfesor = "NO";
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        EditText emailProf, passProf;
        Button btnIngresarProf;

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Init Firebase
        /*database = FirebaseDatabase.getInstance();
        tabla_user = database.getReference("User");*/

        emailProf = root.findViewById(R.id.edtEmailProf);
        passProf = root.findViewById(R.id.edtPassProf);
        btnIngresarProf = root.findViewById(R.id.btnIngresarProf);

        btnIngresarProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarProfesor(emailProf.getText().toString());
                mAuth.signInWithEmailAndPassword(emailProf.getText().toString(), passProf.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful() && esProfesor.equals("YES")) {
                                    Toast.makeText(getActivity(), "Ingreso correcto", Toast.LENGTH_SHORT).show();
                                    Intent irMenuDocentes = new Intent(getContext(), MenuDocentesActivity.class);
                                    irMenuDocentes.putExtra("IDDOCENTE", mAuth.getCurrentUser().getUid());
                                    startActivity(irMenuDocentes);
                                } else {
                                    Toast.makeText(getActivity(), "Fallo el ingreso porque necesita una cuenta Profesor", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

        return root;
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
    }
}
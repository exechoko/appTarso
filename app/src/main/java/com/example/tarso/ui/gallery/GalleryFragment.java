package com.example.tarso.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tarso.MenuEstudiantesActivity;
import com.example.tarso.R;
import com.example.tarso.model.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class GalleryFragment extends Fragment {

    private static final String TAG = "FIRESTORE";
    private GalleryViewModel galleryViewModel;

    FirebaseFirestore db;
    Usuarios user;
    String esProfesor = "NO";

    private FirebaseAuth mAuth;
    /*FirebaseDatabase database;
    DatabaseReference tabla_user;*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        EditText email, pass;
        Button btnIngresar;

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        //Init Firebase
        /*database = FirebaseDatabase.getInstance();
        tabla_user = database.getReference("User");*/

        email = root.findViewById(R.id.edtEmail);
        pass = root.findViewById(R.id.edtPass);
        btnIngresar = root.findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comprobarProfesor(email.getText().toString());
                mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful() && esProfesor.equals("NO")) {
                                    Log.d(TAG, "Es estudiante");
                                    Toast.makeText(getActivity(), "Ingreso correcto", Toast.LENGTH_SHORT).show();
                                    Intent irMenuEstudent = new Intent(getContext(), MenuEstudiantesActivity.class);
                                    irMenuEstudent.putExtra("IDESTUDIANTE", mAuth.getCurrentUser().getUid());
                                    startActivity(irMenuEstudent);
                                } else {
                                    Log.d(TAG, "Es profesor");
                                    Toast.makeText(getActivity(), "Fallo el ingreso porque necesita una cuenta Estudiante", Toast.LENGTH_SHORT).show();

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
        if (user != null && esProfesor.equals("NO")) {
            // User is signed in
            Intent irMenuEstudent = new Intent(getContext(), MenuEstudiantesActivity.class);
            irMenuEstudent.putExtra("IDESTUDIANTE", mAuth.getCurrentUser().getUid());
            startActivity(irMenuEstudent);
        } else {
            // No user is signed in
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
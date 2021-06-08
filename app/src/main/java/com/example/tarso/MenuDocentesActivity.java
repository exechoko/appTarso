package com.example.tarso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tarso.Adapter.SliderData;
import com.example.tarso.model.Documentos;
import com.example.tarso.model.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class MenuDocentesActivity extends AppCompatActivity {

    Spinner spinCursos, spinAsignatura;
    String c="";
    String a="";
    String idProfesor="";
    Button btnSubirNoticia, btnVerTrabajos, btnSeleccionarTrabajo, btnSubirTrabajo;
    ImageButton btnFotoPerfilDocente, btnSalir;

    FirebaseFirestore db;
    StorageReference storageReference, storageReferenceNews;

    Usuarios usuario;
    TextView txtNombre;

    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_docentes);

        btnSubirNoticia = findViewById(R.id.btnSubirNoticia);
        btnVerTrabajos = findViewById(R.id.btnVerTrabajo);
        btnSeleccionarTrabajo = findViewById(R.id.btnSeleccionarTrabajo);
        btnSubirTrabajo = findViewById(R.id.btnSubirTrabajo);
        btnFotoPerfilDocente = findViewById(R.id.fotoPerfilDocente);
        btnSalir = findViewById(R.id.salir);

        if (getIntent()!=null){
            idProfesor = getIntent().getStringExtra("IDDOCENTE");
        } else {
            //
        }

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("documentos");
        storageReferenceNews = FirebaseStorage.getInstance().getReference("noticias");

        //Spinners
        spinCursos = findViewById(R.id.spinnerCurso);
        spinAsignatura = findViewById(R.id.spinnerAsignatura);
        ArrayAdapter<CharSequence> adapterCursos = ArrayAdapter.createFromResource(this,
                R.array.Cursos, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterAsignatura = ArrayAdapter.createFromResource(this,
                R.array.Asignaturas, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterCursos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterAsignatura.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinCursos.setAdapter(adapterCursos);
        spinAsignatura.setAdapter(adapterAsignatura);

        spinCursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c = String.valueOf(parent.getItemIdAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinAsignatura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                a = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
        //-------------------------------------------------------------

        btnVerTrabajos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a.equals("") || a.equals("Seleccione") || c.equals("") || c.equals("0")){
                    Toast.makeText(MenuDocentesActivity.this, "Seleccione un curso y materia", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MenuDocentesActivity.this, "Curso: " + c + " " + "Asign: " + a , Toast.LENGTH_SHORT).show();
                    /*Intent irATrabajos = new Intent(MenuDocentesActivity.this, TrabajosDocentesActivity.class);
                    irATrabajos.putExtra("CURSO", c);
                    irATrabajos.putExtra("ASIGNATURA", a);
                    startActivity(irATrabajos);*/

                }

            }
        });
        
        btnSubirNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuDocentesActivity.this, "Subir una imagen con la noticia", Toast.LENGTH_SHORT).show();
                openGallery();
            }
        });
        
        btnSeleccionarTrabajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarTrabajo();
            }
        });

        cargarUsuario(idProfesor);
    }

    private void cargarUsuario(String idProfesor) {
        db.collection("Usuarios")
                .whereEqualTo("id", idProfesor)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                usuario = document.toObject(Usuarios.class);
                                txtNombre.setText("Bienvenid@ " + usuario.getNombre());
                                //esProfesor = user.getIsProfesor();
                                //Log.d(TAG, esProfesor);

                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        }
                    }
                });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void seleccionarTrabajo() {
        Intent selecWork = new Intent();
        selecWork.setType("application/pdf");
        selecWork.setAction(selecWork.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(selecWork, "SELECCIONAR PDF"), 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null){
            btnSubirTrabajo.setEnabled(true);
            btnSeleccionarTrabajo.setText("Archivo selec.");

            btnSubirTrabajo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subirArchivoPDF(data.getData());
                    Log.d("Nombre uri", data.toString());
                }
            });

        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null && data.getData() != null){
            subirNoticia(data.getData());
        }

    }

    private void subirNoticia(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Subir noticia/comunicado");
        progressDialog.show();

        StorageReference reference = storageReferenceNews.child(System.currentTimeMillis() + ".jpg");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();

                        SliderData noticia = new SliderData(uri.toString());
                        String id = String.valueOf(System.currentTimeMillis());
                        db.collection("Slider").document(id).set(noticia);

                        Toast.makeText(MenuDocentesActivity.this, "Noticia subida correctamente", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("Subiendo noticia... " + (int) progress + "%");
                    }
                });
    }

    private void subirArchivoPDF(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Subir archivo");
        progressDialog.show();

        StorageReference reference = storageReference.child("materia/" + System.currentTimeMillis() + ".pdf");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();

                        Documentos doc = new Documentos("TP 2", "1", "Matemática", "27/05/2021", "Alejandra", uri.toString(), idProfesor);
                        String uid = String.valueOf(System.currentTimeMillis());
                        db.collection("Documentos").document(uid).set(doc);

                        Toast.makeText(MenuDocentesActivity.this, "Archivo subido correctamente", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("Subiendo archivo... " + (int) progress + "%");

            }
        });
    }
}
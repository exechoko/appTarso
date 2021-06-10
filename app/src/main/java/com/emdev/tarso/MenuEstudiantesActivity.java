package com.emdev.tarso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.emdev.tarso.Interface.ItemClickListener;
import com.emdev.tarso.ViewHolder.DocumentosViewHolder;
import com.emdev.tarso.model.Documentos;
import com.emdev.tarso.model.Usuarios;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuEstudiantesActivity extends AppCompatActivity {

    private static final int PERMISO_ALMACENAMIENTO = 1000;
    private static final int PICK_IMAGE = 100;
    boolean permisos_ok = false;

    Spinner spinCursos, spinAsignatura;
    Button btnVerTrabajos, btnVerMisTrabajos, btnSubirTrabajos;
    ImageButton btnFotoPerfilEstudiante, btnSalir;
    String c="";
    String a="";
    String id="";

    Usuarios usuario;
    TextView txtNombre;
    Documentos doc;

    RecyclerView recycler_trabajos;
    LinearLayoutManager layoutManager;
    FirebaseFirestore db;
    StorageReference storageReference;
    FirestoreRecyclerAdapter<Documentos, DocumentosViewHolder> adapter;

    private FirebaseAuth mAuth;
    EditText edtNombreCreador, edtNombreTrabajo;
    Spinner spin_curso_agregar_trab, spin_asig_agregar_trab;
    Button btnSelect, btnUpload;
    String cur="";
    String mat="";
    Documentos document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_estudiantes);

        txtNombre = findViewById(R.id.nombreEstudiante);
        btnVerTrabajos = findViewById(R.id.btnTrabajos);
        btnVerMisTrabajos = findViewById(R.id.btnVerMisTrabajos);
        btnSubirTrabajos = findViewById(R.id.btnSubirMisTrabajos);
        btnFotoPerfilEstudiante = findViewById(R.id.fotoPerfilEstudiante);
        btnSalir = findViewById(R.id.salir);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("documentos");

        if (getIntent()!=null){
            id = getIntent().getStringExtra("IDESTUDIANTE");
        } else {
            //finish();
        }

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
                    Toast.makeText(MenuEstudiantesActivity.this, "Seleccione un curso y materia", Toast.LENGTH_SHORT).show();
                } else {
                    buscarTrabajosDialog(c,a, id);
                    Toast.makeText(MenuEstudiantesActivity.this, "Curso: " + c + " " + "Asign: " + a , Toast.LENGTH_SHORT).show();
                    /*Intent irATrabajos = new Intent(MenuEstudiantesActivity.this, TrabajosActivity.class);
                    irATrabajos.putExtra("CURSO", c);
                    irATrabajos.putExtra("ASIGNATURA", a);
                    startActivity(irATrabajos);*/

                }

            }
        });

        btnVerMisTrabajos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verMisTrabajos(id);
                //adapter.startListening();

            }
        });
        
        btnSubirTrabajos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSubirTrabajo();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        cargarUsuario(id);
    }

    private void dialogSubirTrabajo() {
        //El usuario seleccionará subir el trabajo en imagen o PDF
        MaterialAlertDialogBuilder menu_contextual = new MaterialAlertDialogBuilder(MenuEstudiantesActivity.this)
                .setTitle("Elegi el formato del trabajo")
                .setMessage("Puede ser una imagen o un archivo PDF")
                .setCancelable(true);

        menu_contextual.setPositiveButton("SUBIR IMAGEN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogImagen();
            }
        }).setNeutralButton("SUBIR PDF", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogPDF();
            }
        });

        menu_contextual.show();

    }

    private void dialogImagen() {
        //Toast.makeText(this, "Subir imagen", Toast.LENGTH_SHORT).show();
        final AlertDialog.Builder alerta = new AlertDialog.Builder(MenuEstudiantesActivity.this)
                .setTitle("Subir trabajo en imagen ... ")
                .setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View agregar_trabajo = inflater.inflate(R.layout.agregar_trabajo, null);

        edtNombreTrabajo = agregar_trabajo.findViewById(R.id.edtNombreTrabajo);
        edtNombreCreador = agregar_trabajo.findViewById(R.id.edtNombreCreador);

        edtNombreCreador.setText(usuario.getNombre());
        edtNombreCreador.setEnabled(false);

        //Spinners
        spin_curso_agregar_trab = agregar_trabajo.findViewById(R.id.spinCurso);
        spin_asig_agregar_trab = agregar_trabajo.findViewById(R.id.spinMateria);
        ArrayAdapter<CharSequence> adapterCursos = ArrayAdapter.createFromResource(this,
                R.array.Cursos, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterAsignatura = ArrayAdapter.createFromResource(this,
                R.array.Asignaturas, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterCursos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterAsignatura.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spin_curso_agregar_trab.setAdapter(adapterCursos);
        spin_asig_agregar_trab.setAdapter(adapterAsignatura);

        spin_curso_agregar_trab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cur = String.valueOf(parent.getItemIdAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_asig_agregar_trab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mat = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSelect = agregar_trabajo.findViewById(R.id.btnSelect);
        btnUpload = agregar_trabajo.findViewById(R.id.btnUpload);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarImagen();
            }
        });

        alerta.setView(agregar_trabajo);
        alerta.setPositiveButton("Agregar archivo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (edtNombreCreador.getText().toString().equals("")
                        || edtNombreTrabajo.getText().toString().equals("")
                        || mat.equals("") || mat.equals("Seleccione") || cur.equals("") || cur.equals("0")) {
                    Toast.makeText(MenuEstudiantesActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                } else if (document != null){
                    String uid = String.valueOf(System.currentTimeMillis());
                    db.collection("Documentos").document(uid).set(document);
                }
            }
        });

        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerta.show();
    }

    private void seleccionarImagen() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void dialogPDF() {
        //agregar el dialog para subir un pdf del menuDocentes
        final AlertDialog.Builder alerta = new AlertDialog.Builder(MenuEstudiantesActivity.this)
                .setTitle("Subir trabajo en PDF ... ")
                .setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View agregar_trabajo = inflater.inflate(R.layout.agregar_trabajo, null);

        edtNombreTrabajo = agregar_trabajo.findViewById(R.id.edtNombreTrabajo);
        edtNombreCreador = agregar_trabajo.findViewById(R.id.edtNombreCreador);

        edtNombreCreador.setText(usuario.getNombre());
        edtNombreCreador.setEnabled(false);

        //Spinners
        spin_curso_agregar_trab = agregar_trabajo.findViewById(R.id.spinCurso);
        spin_asig_agregar_trab = agregar_trabajo.findViewById(R.id.spinMateria);
        ArrayAdapter<CharSequence> adapterCursos = ArrayAdapter.createFromResource(this,
                R.array.Cursos, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterAsignatura = ArrayAdapter.createFromResource(this,
                R.array.Asignaturas, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterCursos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterAsignatura.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spin_curso_agregar_trab.setAdapter(adapterCursos);
        spin_asig_agregar_trab.setAdapter(adapterAsignatura);

        spin_curso_agregar_trab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cur = String.valueOf(parent.getItemIdAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_asig_agregar_trab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mat = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSelect = agregar_trabajo.findViewById(R.id.btnSelect);
        btnUpload = agregar_trabajo.findViewById(R.id.btnUpload);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarPDF();
            }
        });

        alerta.setView(agregar_trabajo);
        alerta.setPositiveButton("Agregar archivo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (edtNombreCreador.getText().toString().equals("")
                        || edtNombreTrabajo.getText().toString().equals("")
                        || mat.equals("") || mat.equals("Seleccione") || cur.equals("") || cur.equals("0")) {
                    Toast.makeText(MenuEstudiantesActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                } else if (document != null){
                    String uid = String.valueOf(System.currentTimeMillis());
                    db.collection("Documentos").document(uid).set(document);
                }
            }
        });

        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerta.show();
    }

    private void seleccionarPDF() {
        Intent selecWork = new Intent();
        selecWork.setType("application/pdf");
        selecWork.setAction(selecWork.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(selecWork, "SELECCIONAR PDF"), 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null){
            btnUpload.setEnabled(true);
            btnSelect.setText("ARC. selec.");

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subirArchivoPDF(data.getData());
                    Log.d("Nombre uri", data.toString());
                }
            });
        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null && data.getData() != null){
            btnUpload.setEnabled(true);
            btnSelect.setText("IMG. selec.");

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subirImagen(data.getData());
                }
            });
        }


    }

    private void subirImagen(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Subir imagen");
        progressDialog.show();

        StorageReference reference = storageReference.child(mat +"/" + edtNombreTrabajo.getText().toString() + ".jpg");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();

                        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                        Date todayDate = new Date();
                        String thisDate = currentDate.format(todayDate);

                        document = new Documentos(edtNombreTrabajo.getText().toString(), cur, mat, thisDate, edtNombreCreador.getText().toString(), uri.toString(), id);

                        Toast.makeText(MenuEstudiantesActivity.this, "Imagen subida correctamente", Toast.LENGTH_SHORT).show();
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

    private void subirArchivoPDF(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Subir archivo");
        progressDialog.show();

        StorageReference reference = storageReference.child(mat +"/" + edtNombreTrabajo.getText().toString() + ".pdf");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();

                        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                        Date todayDate = new Date();
                        String thisDate = currentDate.format(todayDate);

                        document = new Documentos(edtNombreTrabajo.getText().toString(), cur, mat, thisDate, edtNombreCreador.getText().toString(), uri.toString(), id);

                        Toast.makeText(MenuEstudiantesActivity.this, "Archivo subido correctamente", Toast.LENGTH_SHORT).show();
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

    private void buscarTrabajosDialog(String c, String a, String id) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MenuEstudiantesActivity.this)
                .setTitle("Trabajos para entregar de \n" + a + " - " + c + "º año.")
                .setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View todosLosTrabajos = inflater.inflate(R.layout.datos_en_recycler, null);

        recycler_trabajos = todosLosTrabajos.findViewById(R.id.recycler_datos);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_trabajos.setLayoutManager(layoutManager);

        Query query = db.collection("Documentos")
                .whereEqualTo("curso", c)
                .whereEqualTo("materia", a)
                .whereNotEqualTo("id", id);
        //.orderBy("fecha", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Documentos> options = new FirestoreRecyclerOptions.Builder<Documentos>()
                .setQuery(query, Documentos.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Documentos, DocumentosViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DocumentosViewHolder holder, int i, @NonNull Documentos documentos) {

                doc = documentos;

                holder.doc_nombre.setText(documentos.getNombre());
                holder.doc_materia.setText(documentos.getMateria());
                holder.doc_nota.setText(documentos.getNota());
                holder.doc_fecha.setText(documentos.getFecha());
                holder.doc_concepto.setText(documentos.getConcepto());
                holder.doc_creador.setText(documentos.getCreador());

                if (doc.getUrl().contains(".pdf")){
                    Picasso.get().load(R.drawable.icon_pdf).into(holder.doc_imagen);
                } else if (doc.getUrl().contains(".jpg")){
                    Picasso.get().load(R.drawable.icon_imagen).into(holder.doc_imagen);
                }

                holder.doc_compartir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MenuEstudiantesActivity.this, "Compartir", Toast.LENGTH_SHORT).show();
                    }
                });

                //String destinoPath = Environment.getExternalStorageDirectory(Environment.DIRECTORY_DOWNLOADS);//Environment.DIRECTORY_DOWNLOADS;//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                holder.doc_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //VERIFICAR PERMISOS
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                                //Denegado, solicitarlo
                                String [] permisos = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                //Dialogo emergente
                                requestPermissions(permisos,PERMISO_ALMACENAMIENTO);

                            } else {
                                Toast.makeText(MenuEstudiantesActivity.this, "Espere mientras se descarga", Toast.LENGTH_SHORT).show();
                                downloadFile(doc);
                            }
                        } else {
                            Toast.makeText(MenuEstudiantesActivity.this, "Espere mientras se descarga", Toast.LENGTH_SHORT).show();
                            downloadFile(doc);
                        }

                        //Toast.makeText(MenuEstudiantesActivity.this, "Espere mientras se descarga", Toast.LENGTH_SHORT).show();
                        //downloadFile(getApplicationContext(), doc.getNombre(), "", destinoPath, doc.getUrl());
                        //downloadFile(doc);
                    }
                });

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(MenuEstudiantesActivity.this, "Creado por: " + doc.getCreador(), Toast.LENGTH_SHORT).show();
                        /*startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickEnDoc.getUrl())));*/
                    }
                });
            }

            @NonNull
            @Override
            public DocumentosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_doc,parent,false);
                return new DocumentosViewHolder(view);
            }
        };

        recycler_trabajos.setAdapter(adapter);
        adapter.startListening();

        alert.setView(todosLosTrabajos);

        alert.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapter.stopListening();
                dialogInterface.cancel();

            }
        });

        alert.show();
    }

    private void downloadFile(Documentos doc) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(doc.getUrl()));
        //Tipo de red
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Descarga");
        request.setDescription("Descargando archivo ... ");

        //request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"" + doc.getNombre());

        //Obtener el servicio
        DownloadManager manager = (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISO_ALMACENAMIENTO:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permiso otorgado
                    //permisos_ok = true;
                    downloadFile(doc);
                } else {
                    Toast.makeText(this, "PERMISO DENEGADO", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /*private void iniciarDescarga(String url) {
        //if (permisos_ok){
            //Solicitud de descarga
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            //Tipo de red
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle("Descarga");
            request.setDescription("Descargando archivo ... ");

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"" + System.currentTimeMillis());

            //Obtener el servicio
            DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);

        //} else {
        //    Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show();
        //}

    }*/

    private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setDescription("Descargando archivo...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }

    private void cargarUsuario(String id) {
        db.collection("Usuarios")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                usuario = document.toObject(Usuarios.class);
                                txtNombre.setText("Bienvenid@ Estudiante\n" + usuario.getNombre());
                                //esProfesor = user.getIsProfesor();
                                //Log.d(TAG, esProfesor);

                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        }
                    }
                });
    }

    private void logOut() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(MenuEstudiantesActivity.this, R.style.CustomDialogTheme);
        alerta.setTitle("CERRAR SESIÓN");
        alerta.setMessage("¿Desea cerrar la sesión?")
                .setCancelable(false)
                .setPositiveButton("SI, salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Toast.makeText(getApplicationContext(),"Hasta luego",Toast.LENGTH_LONG).show();
                        Intent irAlMenuPrincipal = new Intent(MenuEstudiantesActivity.this, MainActivity.class);
                        startActivity(irAlMenuPrincipal);
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alerta.show();
    }

    private void verMisTrabajos(String id) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(MenuEstudiantesActivity.this)
                .setTitle("Mis trabajos entregados")
                .setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View misTrabajos = inflater.inflate(R.layout.datos_en_recycler, null);

        recycler_trabajos = misTrabajos.findViewById(R.id.recycler_datos);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_trabajos.setLayoutManager(layoutManager);

        Query query = db.collection("Documentos")
                .whereEqualTo("id", id);
        //.orderBy("fecha", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Documentos> options = new FirestoreRecyclerOptions.Builder<Documentos>()
                .setQuery(query, Documentos.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Documentos, DocumentosViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DocumentosViewHolder holder, int i, @NonNull Documentos documentos) {

                doc = documentos;

                holder.doc_nombre.setText(documentos.getNombre());
                holder.doc_creador.setText(documentos.getCreador());
                holder.doc_materia.setText(documentos.getMateria());
                holder.doc_fecha.setText(documentos.getFecha());
                holder.doc_nota.setText(documentos.getNota());
                holder.doc_concepto.setText(documentos.getConcepto());

                if (doc.getUrl().contains(".pdf")){
                    Picasso.get().load(R.drawable.icon_pdf).into(holder.doc_imagen);
                } else if (doc.getUrl().contains(".jpg")){
                    Picasso.get().load(R.drawable.icon_imagen).into(holder.doc_imagen);
                }


                holder.doc_compartir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MenuEstudiantesActivity.this, "Compartir", Toast.LENGTH_SHORT).show();
                    }
                });

                //String destinoPath = Environment.DIRECTORY_DOWNLOADS;//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                holder.doc_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadFile(doc);
                        Toast.makeText(MenuEstudiantesActivity.this, "Espere mientras se descarga", Toast.LENGTH_SHORT).show();
                        //downloadFile(TrabajosActivity.this, doc.getNombre(), "", destinoPath, doc.getUrl());
                    }
                });


                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(MenuEstudiantesActivity.this, "Creado por: " + doc.getCreador(), Toast.LENGTH_SHORT).show();
                        /*startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickEnDoc.getUrl())));*/
                    }
                });
            }

            @NonNull
            @Override
            public DocumentosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_doc,parent,false);
                return new DocumentosViewHolder(view);
            }
        };

        recycler_trabajos.setAdapter(adapter);
        adapter.startListening();

        alert.setView(misTrabajos);


        alert.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapter.stopListening();
                dialogInterface.cancel();

            }
        });

        alert.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
        mAuth.signOut();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAuth.signOut();
    }
}
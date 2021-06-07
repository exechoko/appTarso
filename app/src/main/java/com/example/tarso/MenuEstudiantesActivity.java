package com.example.tarso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tarso.Adapter.SliderAdapter;
import com.example.tarso.Adapter.SliderData;
import com.example.tarso.Interface.ItemClickListener;
import com.example.tarso.ViewHolder.DocumentosViewHolder;
import com.example.tarso.model.Documentos;
import com.example.tarso.model.Usuarios;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class MenuEstudiantesActivity extends AppCompatActivity {

    private static final int PERMISO_ALMACENAMIENTO = 1000;
    boolean permisos_ok = false;

    Spinner spinCursos, spinAsignatura;
    Button btnVerTrabajos, btnVerMisTrabajos, btnSubirTrabajos, btnSalir;
    String c="";
    String a="";
    String id="";

    Usuarios usuario;
    TextView txtNombre;
    Documentos doc;

    RecyclerView recycler_trabajos;
    LinearLayoutManager layoutManager;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter<Documentos, DocumentosViewHolder> adapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_estudiantes);

        txtNombre = findViewById(R.id.nombrePersona);
        btnVerTrabajos = findViewById(R.id.btnTrabajos);
        btnVerMisTrabajos = findViewById(R.id.btnVerMisTrabajos);
        btnSubirTrabajos = findViewById(R.id.btnSubirTrabajo);
        btnSalir = findViewById(R.id.salir);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
        //agregar el dialog para subir un pdf del menuDocentes
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
                holder.doc_nota.setText(documentos.getNota());
                holder.doc_concepto.setText(documentos.getConcepto());
                holder.doc_creador.setText(documentos.getCreador());


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
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                                //Denegado, solicitarlo
                                String [] permisos = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                //Dialogo emergente
                                requestPermissions(permisos,PERMISO_ALMACENAMIENTO);

                            } else {
                                iniciarDescarga(doc.getUrl());
                            }
                        } else {
                            iniciarDescarga(doc.getUrl());
                        }*/

                        Toast.makeText(MenuEstudiantesActivity.this, "Espere mientras se descarga", Toast.LENGTH_SHORT).show();
                        //downloadFile(getApplicationContext(), doc.getNombre(), "", destinoPath, doc.getUrl());
                        downloadFile(doc);
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

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISO_ALMACENAMIENTO:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permiso otorgado
                    //permisos_ok = true;
                    iniciarDescarga(doc.getUrl());
                } else {
                    Toast.makeText(this, "PERMISO DENEGADO", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }*/

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
                                txtNombre.setText("Bienvenid@ " + usuario.getNombre());
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
                        startActivity(new Intent(MenuEstudiantesActivity.this,MainActivity.class));
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
                holder.doc_nota.setText(documentos.getNota());
                holder.doc_concepto.setText(documentos.getConcepto());
                holder.doc_creador.setText(documentos.getCreador());

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
    }
}
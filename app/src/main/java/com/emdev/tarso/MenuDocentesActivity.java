package com.emdev.tarso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.emdev.tarso.Adapter.SliderData;
import com.emdev.tarso.Interface.ItemClickListener;
import com.emdev.tarso.ViewHolder.AdminFotoViewHolder;
import com.emdev.tarso.ViewHolder.DocumentosViewHolder;
import com.emdev.tarso.ViewHolder.FotoViewHolder;
import com.emdev.tarso.ViewHolder.SliderDataViewHolder;
import com.emdev.tarso.model.Documentos;
import com.emdev.tarso.model.Foto;
import com.emdev.tarso.model.Usuarios;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MenuDocentesActivity extends AppCompatActivity {

    Spinner spinCursos, spinAsignatura;
    String c="";
    String a="";
    String idProfesor="";
    //Button btnVerTrabajos, btnVerMisTrabajos, btnSubirMisTrabajos;
    ImageButton /*btnVerTrabajos, btnVerMisTrabajos, btnSubirMisTrabajos, btnSubirNoticia, btnAdminNoticias, btnSubirFoto, btnAdmFotos,*/ btnFotoPerfilDocente, btnSalir;
    CardView btnVerTrabajos, btnVerMisTrabajos, btnSubirMisTrabajos, btnSubirNoticia, btnAdminNoticias, btnSubirFoto, btnAdmFotos;


    FirebaseFirestore db;
    StorageReference storageReference, storageReferenceNews, storageReferenceFotos;

    //Cargar usuario
    Usuarios usuario;
    TextView txtNombre;

    //Para descargar un trabajo
    private static final int PERMISO_ALMACENAMIENTO = 1000;
    //private static final int PERMISO_LECTURA = 1001;
    RecyclerView recycler_trabajos;
    LinearLayoutManager layoutManager;
    FirestoreRecyclerAdapter<Documentos, DocumentosViewHolder> adapter;
    //Documentos doc;
    String subPath = "DescargasPabloDeTarso/";

    //Para subir un trabajo
    EditText edtNombreCreador, edtNombreFotografia, edtNombreTrabajo, edtNota, edtConcepto;
    Spinner spin_curso_agregar_trab, spin_asig_agregar_trab;
    Button btnSelect, btnUpload;
    String cur="";
    String mat="";
    Documentos document;

    //Para ver las noticias
    FirestoreRecyclerAdapter<SliderData, SliderDataViewHolder> adapterNoticias;
    SliderData slider_noti;

    //Para ver las fotos
    FirestoreRecyclerAdapter<Foto, AdminFotoViewHolder> adapterFotos;
    Foto fotito;

    private FirebaseAuth mAuth;

    private static final int PICK_IMAGE = 100;
    private static final int PICK_IMAGE_FOTO = 101;
    String bandera = "";

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_docentes);
        MenuDocentesActivity.this.setTitle("Sección Docentes");
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0080B3"));
        Objects.requireNonNull(MenuDocentesActivity.this.getSupportActionBar()).setBackgroundDrawable(colorDrawable);
        Objects.requireNonNull(MenuDocentesActivity.this.getSupportActionBar()).setElevation(0f);

        btnSubirNoticia = findViewById(R.id.btnSubirNoticia);
        btnAdminNoticias = findViewById(R.id.btnAdmNoticia);
        btnSubirFoto = findViewById(R.id.btnSubirFoto);
        btnAdmFotos = findViewById(R.id.btnAdmFotos);

        btnVerTrabajos = findViewById(R.id.btnVerTrabajo);
//        btnSeleccionarTrabajo = findViewById(R.id.btnSeleccionarTrabajo);
//        btnSubirTrabajo = findViewById(R.id.btnSubirTrabajo);
        btnVerMisTrabajos = findViewById(R.id.btnVerMisTrabajos);
        btnSubirMisTrabajos = findViewById(R.id.btnSubirMisTrabajos);
        btnFotoPerfilDocente = findViewById(R.id.fotoPerfilDocente);

        btnSalir = findViewById(R.id.salir);

        txtNombre = findViewById(R.id.nombreDocente);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("documentos");
        storageReferenceNews = FirebaseStorage.getInstance().getReference("noticias");
        storageReferenceFotos = FirebaseStorage.getInstance().getReference("fotos");

        if (getIntent()!=null){
            idProfesor = getIntent().getStringExtra("IDDOCENTE");
        } else {
            //
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
                    Toast.makeText(MenuDocentesActivity.this, "Seleccione un curso y materia", Toast.LENGTH_SHORT).show();
                } else {
                    buscarTrabajosDialog(c,a, idProfesor);
                    //Toast.makeText(MenuDocentesActivity.this, "Curso: " + c + " " + "Asign: " + a , Toast.LENGTH_SHORT).show();
                    /*Intent irATrabajos = new Intent(MenuEstudiantesActivity.this, TrabajosActivity.class);
                    irATrabajos.putExtra("CURSO", c);
                    irATrabajos.putExtra("ASIGNATURA", a);
                    startActivity(irATrabajos);*/

                }

            }
        });
        
        btnSubirNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MenuDocentesActivity.this, "Subir una imagen con la noticia", Toast.LENGTH_SHORT).show();
                //openGallery();
                dialogSubirNoticia(usuario);

            }
        });

        btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSubirFoto(usuario);
            }
        });

        btnVerMisTrabajos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verMisTrabajos(idProfesor);
                //adapter.startListening();

            }
        });

        btnSubirMisTrabajos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSubirTrabajo(usuario);
            }
        });

        btnAdminNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //crear customdialog para cargar las noticias en un recyclerview y poder eliminarlas
                //Toast.makeText(MenuDocentesActivity.this, "Administrar noticias", Toast.LENGTH_SHORT).show();
                dialogAdministrarNoticias();
            }
        });

        btnAdmFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdministrarFotos();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        cargarUsuario(idProfesor);

        txtNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarNombre(usuario);
            }
        });
    }

    private void dialogAdministrarFotos() {
        final AlertDialog.Builder alertFotos = new AlertDialog.Builder(MenuDocentesActivity.this)
                .setTitle("Fotos")
                .setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View verFotos = inflater.inflate(R.layout.datos_en_recycler, null);

        recycler_trabajos = verFotos.findViewById(R.id.recycler_datos);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_trabajos.setLayoutManager(layoutManager);

        Query query = db.collection("Fotos");
        //.orderBy("fecha", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Foto> options = new FirestoreRecyclerOptions.Builder<Foto>()
                .setQuery(query, Foto.class)
                .build();

        adapterFotos = new FirestoreRecyclerAdapter<Foto, AdminFotoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminFotoViewHolder holder, int i, @NonNull Foto foto) {
                holder.foto_creador.setText("Subida por:\n" + foto.getCreador());

                Picasso.get().load(foto.getUrl()).into(holder.img_foto);

                holder.foto_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFoto(adapterFotos.getSnapshots().getSnapshot(holder.getAdapterPosition()).getId());

                    }
                });
            }

            @NonNull
            @Override
            public AdminFotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.admin_fotos,parent,false);
                return new AdminFotoViewHolder(view);
            }
        };

        recycler_trabajos.setAdapter(adapterFotos);
        adapterFotos.startListening();

        alertFotos.setView(verFotos);

        alertFotos.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapterFotos.stopListening();
                dialogInterface.cancel();

            }
        });

        alertFotos.show();
    }

    private void deleteFoto(String id) {
        AlertDialog.Builder alertBorrarFoto = new AlertDialog.Builder(MenuDocentesActivity.this)
                .setTitle("ELIMINAR FOTO")
                .setMessage("¿Está segur@ de eliminar la foto seleccionada?\nUna vez eliminada no se podrá recuperar")
                .setCancelable(true);
        alertBorrarFoto.setPositiveButton("SI, eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                db.collection("Fotos").document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("BORRAR FOTO", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("BORRAR FOTO", "Error deleting document", e);
                            }
                        });
                Toast.makeText(MenuDocentesActivity.this, "Foto eliminada", Toast.LENGTH_SHORT).show();
            }
        });
        alertBorrarFoto.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertBorrarFoto.show();

    }

    private void dialogSubirFoto(Usuarios usuario) {
        final AlertDialog.Builder alerta = new AlertDialog.Builder(MenuDocentesActivity.this)
                .setTitle("Subir foto ... ")
                .setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View agregar_foto = inflater.inflate(R.layout.agregar_foto, null);

        edtNombreCreador = agregar_foto.findViewById(R.id.edtNombreCreador);
        edtNombreFotografia = agregar_foto.findViewById(R.id.edtNombreFotografia);
        edtNombreCreador.setText(usuario.getNombre());
        edtNombreCreador.setEnabled(false);

        btnSelect = agregar_foto.findViewById(R.id.btnSelect);
        btnUpload = agregar_foto.findViewById(R.id.btnUpload);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery2();
                //CropImage.startPickImageActivity(MenuDocentesActivity.this);
            }
        });

        alerta.setView(agregar_foto);
        alerta.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*if (edtNombreCreador.getText().toString().equals("")) {
                    Toast.makeText(MenuDocentesActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                } else if (slider_noti != null){
                    String uid = String.valueOf(System.currentTimeMillis());
                    db.collection("Slider").document(uid).set(slider_noti);
                    Toast.makeText(MenuDocentesActivity.this, "Agregada correctamente", Toast.LENGTH_SHORT).show();
                }*/
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

    private void openGallery2() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE_FOTO);
    }

    private void cambiarNombre(Usuarios usuario) {
        final AlertDialog.Builder alerta = new AlertDialog.Builder(MenuDocentesActivity.this)
                .setTitle("Cambiar nombre de usuario")
                .setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View cambiar_nombre = inflater.inflate(R.layout.cambiar_nombre, null);
        EditText nombre = cambiar_nombre.findViewById(R.id.edtNombreUsuario);
        nombre.setText(usuario.getNombre());
        alerta.setView(cambiar_nombre);

        alerta.setPositiveButton("CAMBIAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                usuario.setNombre(nombre.getText().toString());
                //actualiza el nombre en Firestore
                db.collection("Usuarios").document(usuario.getId()).set(usuario);
                txtNombre.setText("Bienvenid@ Profe\n" + usuario.getNombre());

            }
        });
        alerta.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerta.show();

    }

    private void dialogSubirNoticia(Usuarios usuario) {
        final AlertDialog.Builder alerta = new AlertDialog.Builder(MenuDocentesActivity.this)
                .setTitle("Subir noticia ... ")
                .setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View agregar_noticia = inflater.inflate(R.layout.agregar_noticia, null);

        edtNombreCreador = agregar_noticia.findViewById(R.id.edtNombreCreador);
        edtNombreCreador.setText(usuario.getNombre());
        edtNombreCreador.setEnabled(false);

        btnSelect = agregar_noticia.findViewById(R.id.btnSelect);
        btnUpload = agregar_noticia.findViewById(R.id.btnUpload);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                //CropImage.startPickImageActivity(MenuDocentesActivity.this);
            }
        });

        alerta.setView(agregar_noticia);
        alerta.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*if (edtNombreCreador.getText().toString().equals("")) {
                    Toast.makeText(MenuDocentesActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                } else if (slider_noti != null){
                    String uid = String.valueOf(System.currentTimeMillis());
                    db.collection("Slider").document(uid).set(slider_noti);
                    Toast.makeText(MenuDocentesActivity.this, "Agregada correctamente", Toast.LENGTH_SHORT).show();
                }*/
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

    private void dialogAdministrarNoticias() {
        final AlertDialog.Builder alertNoticias = new AlertDialog.Builder(MenuDocentesActivity.this)
                .setTitle("Noticias y comunicados")
                .setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View verNoticias = inflater.inflate(R.layout.datos_en_recycler, null);

        recycler_trabajos = verNoticias.findViewById(R.id.recycler_datos);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_trabajos.setLayoutManager(layoutManager);

        Query query = db.collection("Slider");
        //.orderBy("fecha", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<SliderData> options = new FirestoreRecyclerOptions.Builder<SliderData>()
                .setQuery(query, SliderData.class)
                .build();

        adapterNoticias = new FirestoreRecyclerAdapter<SliderData, SliderDataViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SliderDataViewHolder holder, int i, @NonNull SliderData sliderData) {

                slider_noti = sliderData;

                holder.slider_creador.setText("Subida por:\n" + slider_noti.getCreador());

                Picasso.get().load(slider_noti.getImgUrl()).into(holder.slider_imagen);

                holder.slider_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteNoticia(adapterNoticias.getSnapshots().getSnapshot(holder.getAdapterPosition()).getId());

                        //Toast.makeText(MenuDocentesActivity.this, "Eliminar noticia", Toast.LENGTH_SHORT).show();
                        //downloadFile(TrabajosActivity.this, doc.getNombre(), "", destinoPath, doc.getUrl());
                    }
                });


                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(MenuDocentesActivity.this, "Creado por: " + slider_noti.getCreador(), Toast.LENGTH_SHORT).show();
                        /*startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickEnDoc.getUrl())));*/
                    }
                });
            }

            @NonNull
            @Override
            public SliderDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.admin_noticias,parent,false);
                return new SliderDataViewHolder(view);
            }
        };

        recycler_trabajos.setAdapter(adapterNoticias);
        adapterNoticias.startListening();

        alertNoticias.setView(verNoticias);

        alertNoticias.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapterNoticias.stopListening();
                dialogInterface.cancel();

            }
        });

        alertNoticias.show();
    }

    private void deleteNoticia(String id) {
        AlertDialog.Builder alertBorrarNoticia = new AlertDialog.Builder(MenuDocentesActivity.this)
                .setTitle("ELIMINAR NOTICIA")
                .setMessage("¿Está segur@ de eliminar la noticia seleccionada?\nUna vez eliminada no se podrá recuperar")
                .setCancelable(true);
        alertBorrarNoticia.setPositiveButton("SI, eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                db.collection("Slider").document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("BORRAR NOTICIA", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("BORRAR NOTICIA", "Error deleting document", e);
                            }
                        });
                Toast.makeText(MenuDocentesActivity.this, "Noticia eliminada", Toast.LENGTH_SHORT).show();
            }
        });
        alertBorrarNoticia.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertBorrarNoticia.show();
    }

    private void dialogSubirTrabajo(Usuarios usuario) {
        final AlertDialog.Builder alerta = new AlertDialog.Builder(MenuDocentesActivity.this)
                .setTitle("Subir trabajo ... ")
                .setMessage("Asegúrese que el archivo\nsea en formato PDF para que sea\ncorrecta su lectura.")
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
        alerta.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (edtNombreCreador.getText().toString().equals("")
                        || edtNombreTrabajo.getText().toString().equals("")
                        || mat.equals("") || mat.equals("Seleccione") || cur.equals("") || cur.equals("0")) {
                    Toast.makeText(MenuDocentesActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                } else if (document != null){
                    String uid = String.valueOf(System.currentTimeMillis());
                    db.collection("Documentos").document(uid).set(document);
                    Toast.makeText(MenuDocentesActivity.this, "Agregado correctamente", Toast.LENGTH_SHORT).show();
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

    private void verMisTrabajos(String idProfesor) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MenuDocentesActivity.this)
                .setTitle("Mis trabajos para que realicen")
                .setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View misTrabajos = inflater.inflate(R.layout.datos_en_recycler, null);

        recycler_trabajos = misTrabajos.findViewById(R.id.recycler_datos);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_trabajos.setLayoutManager(layoutManager);

        Query query = db.collection("Documentos")
                .whereEqualTo("id", idProfesor);
        //.orderBy("fecha", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Documentos> options = new FirestoreRecyclerOptions.Builder<Documentos>()
                .setQuery(query, Documentos.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Documentos, DocumentosViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DocumentosViewHolder holder, int i, @NonNull Documentos documentos) {

                //doc = documentos;

                holder.doc_nombre.setText(documentos.getNombre());
                holder.doc_creador.setText(documentos.getCreador());
                holder.doc_materia.setText(documentos.getMateria());
                holder.doc_fecha.setText(documentos.getFecha());
                holder.doc_nota.setText(documentos.getNota());
                holder.doc_concepto.setText(documentos.getConcepto());

                if (documentos.getUrl().contains(".pdf")){
                    Picasso.get().load(R.drawable.icon_pdf).into(holder.doc_imagen);
                } else if (documentos.getUrl().contains(".jpg")){
                    Picasso.get().load(R.drawable.icon_imagen).into(holder.doc_imagen);
                }

                holder.doc_compartir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MenuDocentesActivity.this, "Compartir", Toast.LENGTH_SHORT).show();

                        /*File outputFile = new File(Environment.getExternalStoragePublicDirectory
                                (Environment.DIRECTORY_DOWNLOADS), subPath + documentos.getNombre() + ".pdf");
                        Uri uri = Uri.fromFile(outputFile);

                        if (!outputFile.exists()){
                            Toast.makeText(MenuDocentesActivity.this, "No existe el archivo", Toast.LENGTH_SHORT).show();
                            downloadFile(documentos);
                        }

                        try {

                            Intent share = new Intent();
                            share.setAction(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            share.setPackage("com.whatsapp");
                            startActivity(share);
                        } catch (android.content.ActivityNotFoundException ex){
                            Toast.makeText(MenuDocentesActivity.this, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show();
                        }*/

                    }
                });

                //String destinoPath = Environment.DIRECTORY_DOWNLOADS;//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
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
                                Toast.makeText(MenuDocentesActivity.this, "Espere mientras se descarga", Toast.LENGTH_SHORT).show();
                                //downloadFile(documentos);
                                otroDownload(documentos);
                            }
                        } else {
                            Toast.makeText(MenuDocentesActivity.this, "Espere mientras se descarga", Toast.LENGTH_SHORT).show();
                            otroDownload(documentos);

                        }
                    }
                });

                holder.doc_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteMiTrabajo(adapter.getSnapshots().getSnapshot(holder.getAdapterPosition()).getId());
                    }
                });

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(MenuDocentesActivity.this, "Creado por: " + documentos.getCreador(), Toast.LENGTH_SHORT).show();
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

    private void deleteMiTrabajo(String idTrabajo) {
        AlertDialog.Builder alertBorrarTrabajo = new AlertDialog.Builder(MenuDocentesActivity.this)
                .setTitle("ELIMINAR TRABAJO")
                .setMessage("¿Está segur@ de eliminar el trabajo seleccionado?\nUna vez eliminado no se podrá recuperar")
                .setCancelable(true);
        alertBorrarTrabajo.setPositiveButton("SI, eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                db.collection("Documentos").document(idTrabajo)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("BORRAR TRABAJO", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("BORRAR TRABAJO", "Error deleting document", e);
                            }
                        });
                Toast.makeText(MenuDocentesActivity.this, "TRABAJO ELIMINADO", Toast.LENGTH_SHORT).show();
            }
        });
        alertBorrarTrabajo.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertBorrarTrabajo.show();
    }

    private void otroDownload(Documentos documentos) {
        //Extension
        String extension = "";
        if (documentos.getUrl().contentEquals(".pdf")){
            extension = ".pdf";
        } else if (documentos.getUrl().contentEquals(".jpg")){
            extension = ".jpg";
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(documentos.getUrl());
        //StorageReference storageRef = storage.getReference().child("documentos/" + mat + "/" + documentos.getNombre() + extension);

        pd = new ProgressDialog(this);
        pd.setTitle(documentos.getNombre() + extension);
        pd.setMessage("Descargando,\npor favor espere ...");
        /*pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);*/
        pd.show();

        String finalExtension = extension;
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                descargarArchivo(MenuDocentesActivity.this, documentos.getNombre(), finalExtension, DIRECTORY_DOWNLOADS, url);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pd.dismiss();
                Log.e("firebase ", ";local tem file not created  created " + exception.toString());
                Toast.makeText(MenuDocentesActivity.this, "Descarga Incompleta", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void descargarArchivo(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        DownloadManager.Request request1 = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory,fileName + fileExtension); //si le hago click se visualiza pero no se guarda
        request1.setDestinationInExternalPublicDir(destinationDirectory, fileName + fileExtension); //al reves del anterior

        downloadManager.enqueue(request);
        downloadManager.enqueue(request1);

        pd.dismiss();
    }

    private void buscarTrabajosDialog(String c, String a, String idProfesor) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MenuDocentesActivity.this)
                .setTitle("Trabajos que entregaron est. de \n" + a + " - " + c + "º año.")
                .setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View todosLosTrabajos = inflater.inflate(R.layout.datos_en_recycler, null);

        recycler_trabajos = todosLosTrabajos.findViewById(R.id.recycler_datos);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_trabajos.setLayoutManager(layoutManager);

        Query query = db.collection("Documentos")
                .whereEqualTo("curso", c)
                .whereEqualTo("materia", a)
                .whereEqualTo("creadorIsProfesor","NO")
                .whereNotEqualTo("id", idProfesor);
        //.orderBy("fecha", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Documentos> options = new FirestoreRecyclerOptions.Builder<Documentos>()
                .setQuery(query, Documentos.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Documentos, DocumentosViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DocumentosViewHolder holder, int i, @NonNull Documentos documentos) {

                //doc = documentos;

                holder.doc_nombre.setText(documentos.getNombre());
                holder.doc_nota.setText(documentos.getNota());
                holder.doc_materia.setText(documentos.getMateria());
                holder.doc_fecha.setText(documentos.getFecha());
                holder.doc_concepto.setText(documentos.getConcepto());
                holder.doc_creador.setText(documentos.getCreador());

                if (documentos.getUrl().contains(".pdf")){
                    Picasso.get().load(R.drawable.icon_pdf).into(holder.doc_imagen);
                } else if (documentos.getUrl().contains(".jpg")){
                    Picasso.get().load(R.drawable.icon_imagen).into(holder.doc_imagen);
                }

                holder.doc_compartir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MenuDocentesActivity.this, "Compartir", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MenuDocentesActivity.this, "Espere mientras se descarga", Toast.LENGTH_SHORT).show();
                                otroDownload(documentos);
                            }
                        } else {
                            Toast.makeText(MenuDocentesActivity.this, "Espere mientras se descarga", Toast.LENGTH_SHORT).show();
                            otroDownload(documentos);
                        }
                    }
                });

                holder.doc_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MenuDocentesActivity.this, "No puede eliminar un trabajo que no sea suyo", Toast.LENGTH_SHORT).show();
                    }
                });

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(MenuDocentesActivity.this, "Creado por: " + doc.getCreador(), Toast.LENGTH_SHORT).show();
                        Log.d("KeyDocumento", adapter.getSnapshots().getSnapshot(holder.getAdapterPosition()).getId());
                        agregarNota(adapter.getSnapshots().getSnapshot(holder.getAdapterPosition()).getId(), documentos);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISO_ALMACENAMIENTO:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "PERMISOS ACEPTADOS\nPruebe nuevamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "PERMISO DENEGADO", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void logOut() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(MenuDocentesActivity.this, R.style.CustomDialogTheme);
        alerta.setTitle("CERRAR SESIÓN");
        alerta.setMessage("¿Desea cerrar la sesión?")
                .setCancelable(false)
                .setPositiveButton("SI, salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Toast.makeText(getApplicationContext(),"Hasta luego",Toast.LENGTH_LONG).show();
                        Intent irAlMenuPrincipal = new Intent(MenuDocentesActivity.this, MainActivity.class);
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
                                txtNombre.setText("Bienvenid@ Profe\n" + usuario.getNombre());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null){
            btnUpload.setEnabled(true);
            btnSelect.setText("Archivo selec.");
            btnSelect.setEnabled(false);

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    subirArchivoPDF(data.getData());
                    btnUpload.setText("ARC. almac.");
                    btnUpload.setEnabled(false);
                    Log.d("Nombre uri", data.toString());

                }
            });

        }

        if (requestCode == PICK_IMAGE  && resultCode == -1) {
            CropImage.activity(CropImage.getPickImageResultUri(this, data)).setGuidelines(CropImageView.Guidelines.ON).setRequestedSize(400, 200).setAspectRatio(2, 1).start((Activity) this);
            bandera = "NOTICIA";
        }

        if (requestCode == PICK_IMAGE_FOTO  && resultCode == -1) {
            CropImage.activity(CropImage.getPickImageResultUri(this, data)).setGuidelines(CropImageView.Guidelines.ON).start((Activity) this);
            bandera = "FOTO";
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //Uri resultUri = result.getUri();
            if (resultCode == -1) {
                File file = new File(result.getUri().getPath());

                btnUpload.setEnabled(true);
                btnSelect.setText("IMG selec.");
                btnSelect.setEnabled(false);

                String finalBandera = bandera;
                btnUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (finalBandera.equals("NOTICIA")){
                            subirNoticia(result.getUri());
                            btnUpload.setText("IMG. almac.");
                            btnUpload.setEnabled(false);

                            Log.d("Nombre uri", data.toString());
                        } else if (finalBandera.equals("FOTO")){

                            if (edtNombreFotografia.getText().toString().equals("")){
                                Toast.makeText(MenuDocentesActivity.this, "Necesita ingresar\nun nombre a la foto", Toast.LENGTH_SHORT).show();
                            } else {
                                subirFoto(result.getUri());
                                btnUpload.setText("IMG. almac.");
                                btnUpload.setEnabled(false);
                                Log.d("Nombre uri", data.toString());
                            }
                        } else {
                            Toast.makeText(MenuDocentesActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }



    }

    private void subirFoto(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Subir foto ... ");
        progressDialog.show();

        //Ruta en el Storage
        StorageReference reference = storageReferenceFotos.child(System.currentTimeMillis() + ".jpg");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();

                        String nombreFoto = edtNombreFotografia.getText().toString();
                        Foto foto = new Foto(nombreFoto,usuario.getNombre(),uri.toString());
                        String id = String.valueOf(System.currentTimeMillis());
                        db.collection("Fotos").document(id).set(foto);

                        Toast.makeText(MenuDocentesActivity.this, "Foto subida correctamente", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("Subiendo foto ... " + (int) progress + "%");
                    }
                });
    }

    private void subirNoticia(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Subir noticia/comunicado");
        progressDialog.show();

        //Ruta en el Storage
        StorageReference reference = storageReferenceNews.child(System.currentTimeMillis() + ".jpg");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();

                        SliderData noticia = new SliderData(uri.toString(), usuario.getNombre());
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

        //Ruta en el Storage
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

                        document = new Documentos(edtNombreTrabajo.getText().toString(), cur, mat, thisDate, "Profe " + edtNombreCreador.getText().toString(), "YES",uri.toString(), idProfesor);

                        Toast.makeText(MenuDocentesActivity.this, "Archivo subido a la nube\nPresione CONFIRMAR", Toast.LENGTH_SHORT).show();
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

    /*@Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        //Log.d("KeyDocumento", adapter.getSnapshots().getSnapshot(holder.getAdapterPosition()).getId());
        if (item.getTitle().equals("AGREGAR NOTA")){
            agregarNota(adapter.getSnapshots().getSnapshot(item.getOrder()).getId(), adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals("AGREGAR CONCEPTO")){
            //agregarConcepto(adapter.getSnapshots().getSnapshot(item.getOrder()).getId(), adapter.getItem(item.getOrder()));
        }

        return super.onContextItemSelected(item);
    }*/

    private void agregarNota(String docID, Documentos item) {
        AlertDialog.Builder dialogoNota = new AlertDialog.Builder(MenuDocentesActivity.this)
                .setTitle("CALIFICAR")
                .setCancelable(false);

        LayoutInflater inflater2 = this.getLayoutInflater();
        View agregar_nota_concepto = inflater2.inflate(R.layout.agregar_nota_concepto, null);

        edtNombreTrabajo = agregar_nota_concepto.findViewById(R.id.edtNombreTrabajo);
        edtNombreCreador = agregar_nota_concepto.findViewById(R.id.edtNombreCreador);
        edtNota = agregar_nota_concepto.findViewById(R.id.edtNota);
        edtConcepto = agregar_nota_concepto.findViewById(R.id.edtConcepto);

        edtNombreCreador.setText(item.getCreador());
        edtNombreTrabajo.setEnabled(false);
        edtNombreTrabajo.setText(item.getNombre());
        edtNombreCreador.setEnabled(false);
        edtNota.setText(item.getNota());
        edtConcepto.setText(item.getConcepto());

        dialogoNota.setView(agregar_nota_concepto);

        dialogoNota.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (edtNombreCreador.getText().toString().equals("")
                        || edtNombreTrabajo.getText().toString().equals("")
                        || edtNota.getText().toString().equals("")
                        || edtConcepto.getText().toString().equals("")) {
                    Toast.makeText(MenuDocentesActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    item.setNombre(edtNombreTrabajo.getText().toString());
                    item.setCreador(edtNombreCreador.getText().toString());
                    item.setNota(edtNota.getText().toString());
                    item.setConcepto(edtConcepto.getText().toString());

                    //item es el documento que estamos actualizando
                    db.collection("Documentos").document(docID).set(item);

                    Toast.makeText(MenuDocentesActivity.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialogoNota.setNegativeButton("DESHACER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        dialogoNota.show();
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
package com.example.tarso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tarso.Interface.ItemClickListener;
import com.example.tarso.ViewHolder.DocumentosViewHolder;
import com.example.tarso.model.Documentos;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TrabajosActivity extends AppCompatActivity {

    RecyclerView recycler_trabajos;
    LinearLayoutManager layoutManager;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter<Documentos, DocumentosViewHolder> adapter;

    String curso = "";
    String asig = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajos);

        db = FirebaseFirestore.getInstance();

        if (getIntent() != null){
            curso = getIntent().getStringExtra("CURSO"); //Es un numero
            asig = getIntent().getStringExtra("ASIGNATURA");

        }

        //Load menu
        recycler_trabajos = (RecyclerView) findViewById(R.id.recyclerTrabajos);
        recycler_trabajos.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_trabajos.setLayoutManager(layoutManager);

        cargarTrabajos(curso, asig);
    }

    private void cargarTrabajos(String curso, String asig) {
        Query query = db.collection("Documentos")
                .whereEqualTo("curso", curso)
                .whereEqualTo("materia", asig);
                //.orderBy("fecha", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Documentos> options = new FirestoreRecyclerOptions.Builder<Documentos>()
                .setQuery(query, Documentos.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Documentos, DocumentosViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DocumentosViewHolder holder, int i, @NonNull Documentos documentos) {

                final Documentos doc = documentos;

                holder.doc_nombre.setText(documentos.getNombre());

                holder.doc_compartir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TrabajosActivity.this, "Compartir", Toast.LENGTH_SHORT).show();
                    }
                });

                String destinoPath = Environment.DIRECTORY_DOWNLOADS;//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                holder.doc_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TrabajosActivity.this, "Espere mientras se descarga", Toast.LENGTH_SHORT).show();
                        downloadFile(TrabajosActivity.this, doc.getNombre(), "", destinoPath, doc.getUrl());
                    }
                });


                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(TrabajosActivity.this, "Creado por: " + doc.getCreador(), Toast.LENGTH_SHORT).show();
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

    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setDescription("Descargando archivo...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
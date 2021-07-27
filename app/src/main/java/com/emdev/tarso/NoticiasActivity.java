package com.emdev.tarso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.emdev.tarso.Adapter.SliderAdapter;
import com.emdev.tarso.Adapter.SliderData;
import com.emdev.tarso.Interface.ItemClickListener;
import com.emdev.tarso.ViewHolder.FotoViewHolder;
import com.emdev.tarso.ViewHolder.SliderDataViewHolder;
import com.emdev.tarso.model.Foto;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class NoticiasActivity extends AppCompatActivity {

    // creating variables for our adapter, array list,
    // firebase firestore and our sliderview.
    private SliderAdapter adapter;
    private ArrayList<SliderData> sliderDataArrayList;
    FirebaseFirestore db;
    private SliderView sliderView;

    RecyclerView recyclerImagenes;
    GridLayoutManager gridLayoutManager;
    FirestoreRecyclerAdapter<Foto, FotoViewHolder> adapterFotos;
    ImageView img_detalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);
        NoticiasActivity.this.setTitle("Noticias");
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0080B3"));
        Objects.requireNonNull(NoticiasActivity.this.getSupportActionBar()).setBackgroundDrawable(colorDrawable);
        Objects.requireNonNull(NoticiasActivity.this.getSupportActionBar()).setElevation(0f);

        recyclerImagenes = findViewById(R.id.recyclerImagenes);
        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        recyclerImagenes.setLayoutManager(gridLayoutManager);

        //Sliders para noticias
        // creating a new array list fr our array list.
        sliderDataArrayList = new ArrayList<>();

        // initializing our slider view and
        // firebase firestore instance.
        sliderView = findViewById(R.id.slider);
        db = FirebaseFirestore.getInstance();

        // calling our method to load images.
        loadNoticias();
        loadFotos();
    }

    private void loadFotos() {

        Query query = db.collection("Fotos");
        //.orderBy("fecha", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Foto> options = new FirestoreRecyclerOptions.Builder<Foto>()
                .setQuery(query, Foto.class)
                .build();

        adapterFotos = new FirestoreRecyclerAdapter<Foto, FotoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FotoViewHolder holder, int i, @NonNull Foto foto) {
                holder.nombreFoto.setText(foto.getNombre());
                Picasso.get().load(foto.getUrl()).into(holder.foto);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(NoticiasActivity.this, "Subida por " + foto.getCreador(), Toast.LENGTH_SHORT).show();
                        verImagenAmpliada(foto);
                    }
                });


            }

            @NonNull
            @Override
            public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_foto,parent,false);
                return new FotoViewHolder(view);
            }
        };

        recyclerImagenes.setAdapter(adapterFotos);
    }

    private void verImagenAmpliada(Foto fotoModel) {

        AlertDialog.Builder alert = new AlertDialog.Builder(NoticiasActivity.this)
                .setTitle(fotoModel.getNombre())
                .setCancelable(true);

        LayoutInflater inflater = this.getLayoutInflater();
        View detalle_foto_layout = inflater.inflate(R.layout.detalle_foto, null);

        img_detalle = detalle_foto_layout.findViewById(R.id.img_detalle);

        //Picasso.with(getApplicationContext()).load(fotoModel.getUrl()).resize(800,800).centerCrop().into(img_detalle);
        Picasso.get().load(fotoModel.getUrl()).resize(800,800).centerCrop().into(img_detalle);


        alert.setView(detalle_foto_layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alert.show();

    }

    private void loadNoticias() {
        // getting data from our collection and after
        // that calling a method for on success listener.
        db.collection("Slider").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                // inside the on success method we are running a for loop
                // and we are getting the data from Firebase Firestore
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    // after we get the data we are passing inside our object class.
                    SliderData sliderData = documentSnapshot.toObject(SliderData.class);
                    SliderData model = new SliderData();

                    // below line is use for setting our
                    // image url for our modal class.
                    model.setImgUrl(sliderData.getImgUrl());

                    // after that we are adding that
                    // data inside our array list.
                    sliderDataArrayList.add(model);

                    // after adding data to our array list we are passing
                    // that array list inside our adapter class.
                    adapter = new SliderAdapter(NoticiasActivity.this, sliderDataArrayList);

                    // belows line is for setting adapter
                    // to our slider view
                    sliderView.setSliderAdapter(adapter);

                    // below line is for setting animation to our slider.
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

                    // below line is for setting auto cycle duration.
                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

                    // below line is for setting
                    // scroll time animation
                    sliderView.setScrollTimeInSec(3);

                    // below line is for setting auto
                    // cycle animation to our slider
                    sliderView.setAutoCycle(true);

                    // below line is use to start
                    // the animation of our slider view.
                    sliderView.startAutoCycle();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if we get any error from Firebase we are
                // displaying a toast message for failure
                Toast.makeText(NoticiasActivity.this, "Fail to load slider data..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterFotos.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterFotos.stopListening();
    }
}
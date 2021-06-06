package com.example.tarso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.tarso.Adapter.SliderAdapter;
import com.example.tarso.Adapter.SliderData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class NoticiasActivity extends AppCompatActivity {

    // creating variables for our adapter, array list,
    // firebase firestore and our sliderview.
    private SliderAdapter adapter;
    private ArrayList<SliderData> sliderDataArrayList;
    FirebaseFirestore db;
    private SliderView sliderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);

        //Sliders para noticias
        // creating a new array list fr our array list.
        sliderDataArrayList = new ArrayList<>();

        // initializing our slider view and
        // firebase firestore instance.
        sliderView = findViewById(R.id.slider);
        db = FirebaseFirestore.getInstance();

        // calling our method to load images.
        loadImages();
    }

    private void loadImages() {
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
}
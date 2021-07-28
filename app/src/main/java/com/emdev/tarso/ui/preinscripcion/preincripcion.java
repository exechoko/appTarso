package com.emdev.tarso.ui.preinscripcion;

import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.emdev.tarso.R;
import com.emdev.tarso.model.FechaPreinscripcion;
import com.emdev.tarso.model.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class preincripcion extends Fragment {

    private PreincripcionViewModel mViewModel;

    FirebaseFirestore db;
    FechaPreinscripcion fechaPreinsc;
    TextView fecha;
    WebView webView;
    long fI = 0;
    long fF = 0;

    public static preincripcion newInstance() {
        return new preincripcion();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.preincripcion_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        fecha = root.findViewById(R.id.fecha);
        webView = root.findViewById(R.id.webViewFormulario);

        cargarFecha();

        return root;
    }

    private void cargarFecha() {
        //Carga las fechas desde Firestore
        db.collection("FechaPreinscripcion")
                .document("Ts1mC4vlpEeod2IItrsm")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        fechaPreinsc = documentSnapshot.toObject(FechaPreinscripcion.class);
                        //convierte currentTimeMilis en dd/MM/yyyy
                        fI = Long.parseLong(fechaPreinsc.getInicio()); //fecha de inicio
                        fF = Long.parseLong(fechaPreinsc.getFin()); //fecha de Fin
                        SimpleDateFormat currentDate1 = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat currentDate2 = new SimpleDateFormat("dd/MM/yyyy");
                        Date diaInicio = new Date(fI);
                        Date diaFin = new Date(fF);
                        String fecha_de_inicio = currentDate1.format(diaInicio);
                        String fecha_de_fin = currentDate2.format(diaFin);
                        fecha.setText("Desde el " + fecha_de_inicio + "\nhasta el " + fecha_de_fin);
                        Log.d("Fecha", fechaPreinsc.getInicio());

                        //String primeroDeNoviembre = "1635735600000";
                        //long hoy = Long.parseLong(primeroDeNoviembre);
                        long hoy = System.currentTimeMillis();
                        if (hoy > fI && hoy < fF){
                            Log.d("Puede", "SI");
                            mostrarWebView(fechaPreinsc.getUrl());
                        } else {
                            Log.d("Puede", "NO");
                        }

                    }
                });

    }

    private void mostrarWebView(String url) {
        webView.setVisibility(View.VISIBLE);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }



}
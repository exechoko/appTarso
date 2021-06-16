package com.emdev.tarso.ui.utilidades;

import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.emdev.tarso.MapasActivity;
import com.emdev.tarso.QuimicaActivity;
import com.emdev.tarso.R;
import com.emdev.tarso.TraductorActivity;
import com.emdev.tarso.calculadoraActivity;
import com.github.barteksc.pdfviewer.PDFView;

import de.hdodenhof.circleimageview.CircleImageView;

public class UtilidadesFragment extends Fragment {

    private UtilidadesViewModel mViewModel;

    //CircleImageView btnCalc, btnQuimica, btnMapas, btnTraductor, btnAplicaciones, btn6;
    Button btnBuscar, cancelar;
    TextView titulo_visualizador;
    Spinner spinCursos, spinAsignatura;
    String C="";
    String A="";


    public static UtilidadesFragment newInstance() {
        return new UtilidadesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(UtilidadesViewModel.class);
        View root = inflater.inflate(R.layout.utilidades_fragment, container, false);

        /*btnCalc = root.findViewById(R.id.btnCalc);
        btnQuimica = root.findViewById(R.id.btnQuimica);
        btnMapas = root.findViewById(R.id.btnMapas);
        btnTraductor = root.findViewById(R.id.btnTrad);
        btnAplicaciones = root.findViewById(R.id.apps);
        btn6 = root.findViewById(R.id.b6);*/

//        btnCalc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent irACalcu = new Intent(getContext(), calculadoraActivity.class);
//                startActivity(irACalcu);
//
//                /*Intent irACalcu = new Intent();
//                irACalcu.makeMainSelectorActivity (Intent.ACTION_MAIN,
//                        Intent.CATEGORY_APP_CALCULATOR);
//                startActivity(irACalcu);*/
//            }
//        });

//        btnQuimica.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent irAQuimica = new Intent(getContext(), QuimicaActivity.class);
//                startActivity(irAQuimica);
//            }
//        });

//        btnMapas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent irAMapas = new Intent(getContext(), MapasActivity.class);
//                startActivity(irAMapas);
//            }
//        });

//        btnTraductor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent irATraductor = new Intent(getContext(), TraductorActivity.class);
//                startActivity(irATraductor);
//            }
//        });
//
//        btnAplicaciones.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        btn6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        btnBuscar = root.findViewById(R.id.verTrabajos);

        //Spinners
        spinCursos = root.findViewById(R.id.spinCurso);
        spinAsignatura = root.findViewById(R.id.spinMateria);
        ArrayAdapter<CharSequence> adapterCursos = ArrayAdapter.createFromResource(getActivity(),
                R.array.Cursos, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterAsignatura = ArrayAdapter.createFromResource(getActivity(),
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
                C = String.valueOf(parent.getItemIdAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinAsignatura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                A = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarVisualizador(C,A);
            }
        });

        return root;
    }

    private void cargarVisualizador(String c, String a) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        assert dialog.getWindow() != null;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.visualizador_pdf);

        titulo_visualizador = dialog.findViewById(R.id.titulo_visualizador);
        titulo_visualizador.setText(selectPDF(c,a));

        PDFView pdfView = dialog.findViewById(R.id.pdfVisualizador);
        //pdfView.fromAsset("1 - GRUPO 1.pdf")
        pdfView.fromAsset(selectPDF(c,a))
                //.pages() // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .load();

        cancelar = dialog.findViewById(R.id.cancel_action);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private String selectPDF(String c, String a) {
        String nombre = "";
        if (c.equals("1") && a.equals("Grupo 1")){
            nombre = "1 - GRUPO 1.pdf";
        } else if (c.equals("1") && a.equals("Grupo 2")){
            nombre = "1 - GRUPO 2.pdf";
        } else if (c.equals("2") && a.equals("Grupo 1")){
            nombre = "2 - GRUPO 1.pdf";
        } else if (c.equals("2") && a.equals("Grupo 2")){
            nombre = "2 - GRUPO 2.pdf";
        } else if (c.equals("3") && a.equals("Grupo 1")){
            nombre = "3 - GRUPO 1.pdf";
        } else if (c.equals("3") && a.equals("Grupo 2")){
            nombre = "3 - GRUPO 2.pdf";
        }
        return nombre;

    }


}
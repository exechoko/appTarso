package com.emdev.tarso.ui.utilidades;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    Button btnBuscar, btnVerBio, btnMateriaSola, btnVerProyecto, cancelar;
    TextView titulo_visualizador;
    Spinner spinCursos, spinBurbuja, spinMateriaSola;
    String C=""; //Curso
    String B=""; //Burbuja
    String MS=""; //Materia sola

    ConstraintLayout constraintLayout;
    String pass = "";
    Button NO, OK;
    EditText edtPass_apuntes;


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

        constraintLayout = root.findViewById(R.id.contraint);

        dialogDeAcceso();

        btnBuscar = root.findViewById(R.id.verTrabajos);
        btnVerBio = root.findViewById(R.id.verBio);
        btnMateriaSola = root.findViewById(R.id.verMaterial);
        btnVerProyecto = root.findViewById(R.id.verProyecto);

        //Spinners
        spinCursos = root.findViewById(R.id.spinCurso);
        spinBurbuja = root.findViewById(R.id.spinBurbuja);
        spinMateriaSola = root.findViewById(R.id.spinMateria2);
        ArrayAdapter<CharSequence> adapterCursos = ArrayAdapter.createFromResource(getActivity(),
                R.array.CursosSolos, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterAsignatura = ArrayAdapter.createFromResource(getActivity(),
                R.array.Burbujas, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterMateria = ArrayAdapter.createFromResource(getActivity(),
                R.array.Materias, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterCursos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterAsignatura.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMateria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinCursos.setAdapter(adapterCursos);
        spinBurbuja.setAdapter(adapterAsignatura);
        spinMateriaSola.setAdapter(adapterMateria);

        spinCursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                C = String.valueOf(parent.getItemIdAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinBurbuja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                B = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinMateriaSola.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MS = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (C.equals("")||C.equals("Seleccione") || B.equals("") || B.equals("Seleccione")){
                    Toast.makeText(getActivity(), "Debe seleccionar un Curso y una Burbuja", Toast.LENGTH_SHORT).show();
                } else {
                    cargarVisualizador(C,B);
                }
            }
        });
        
        btnMateriaSola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MS.equals("") || MS.equals("Seleccione")){
                    Toast.makeText(getActivity(), "Debe seleccionar una Asignatura", Toast.LENGTH_SHORT).show();
                } else {
                    cargarVisualizadorDeMateria(MS);
                }
            }
        });

        btnVerProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarVisualizarDeProyectos();
            }
        });

        btnVerBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarVisualizadorSimple();
            }
        });

        return root;
    }

    private void dialogDeAcceso() {
        Dialog alerta = new Dialog(getActivity());
        alerta.requestWindowFeature(Window.FEATURE_NO_TITLE);
        assert alerta.getWindow() != null;
        alerta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alerta.setTitle("INGRESE LA CLAVE PARA CONTINUAR");
        alerta.setCancelable(false);
        alerta.setContentView(R.layout.pass_apuntes);

        edtPass_apuntes = alerta.findViewById(R.id.edtPassApuntes);
        NO = alerta.findViewById(R.id.btnNo);
        OK = alerta.findViewById(R.id.btnOk);

        NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.dismiss();
            }
        });

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = edtPass_apuntes.getText().toString();

                if (pass.equals("")){
                    Toast.makeText(getContext(), "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals("852654")){
                    Toast.makeText(getContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                } else {
                    alerta.dismiss();
                    constraintLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        alerta.show();

    }

    private void cargarVisualizarDeProyectos() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        assert dialog.getWindow() != null;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.visualizador_pdf);

        titulo_visualizador = dialog.findViewById(R.id.titulo_visualizador);
        titulo_visualizador.setText("Proyecto Huerta");

        PDFView pdfView = dialog.findViewById(R.id.pdfVisualizador);
        //pdfView.fromAsset("1 - GRUPO 1.pdf")
        pdfView.fromAsset("ProyectoHuerta.pdf")
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

    private void cargarVisualizadorDeMateria(String ms) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        assert dialog.getWindow() != null;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.visualizador_pdf);

        titulo_visualizador = dialog.findViewById(R.id.titulo_visualizador);
        titulo_visualizador.setText(selectApuntePDF(ms));

        PDFView pdfView = dialog.findViewById(R.id.pdfVisualizador);
        //pdfView.fromAsset("1 - GRUPO 1.pdf")
        pdfView.fromAsset(selectApuntePDF(ms))
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

    private String selectApuntePDF(String ms) {
        String nombreApunte = "";

        switch (ms) {
            case "Matemática":
                nombreApunte = "Sin Contenido";
                break;
            case "Lengua y Literatura":
                nombreApunte = "Sin Contenido";
                break;
            case "Historia":
                nombreApunte = "Historia - Uso frecuente.pdf";
                break;
            case "Geografía":
                nombreApunte = "Sin Contenido";
                break;
            case "Ingles":
                nombreApunte = "Sin Contenido";
                break;
            case "Ética y ciudadana":
                nombreApunte = "Sin Contenido";
                break;
            case "Tecnología":
                nombreApunte = "Sin Contenido";
                break;
            case "Físicoquímica":
                nombreApunte = "Fisico - Quimica - Uso frecuente.pdf";
                break;
            case "Biología":
                nombreApunte = "Sin Contenido";
                break;
            case "TIC":
                nombreApunte = "Sin Contenido";
                break;
            case "Cs. de la Tierra":
                nombreApunte = "Sin Contenido";
                break;
            case "Lit. Latinoamericana":
                nombreApunte = "Sin Contenido";
                break;
            case "Bioética":
                nombreApunte = "Sin Contenido";
                break;
            case "Artes Visuales":
                nombreApunte = "Sin Contenido";
                break;
            case "Música":
                nombreApunte = "Sin Contenido";
                break;
            case "Educación Fisica":
                nombreApunte = "Sin Contenido";
                break;
            case "Intro. a la Investigación":
                nombreApunte = "Sin Contenido";
                break;
            case "Práctica Educativa":
                nombreApunte = "Sin Contenido";
                break;
            default:
                nombreApunte = "Sin Contenido";
                break;
        }
        return nombreApunte;
    }

    private void cargarVisualizadorSimple() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        assert dialog.getWindow() != null;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.visualizador_pdf);

        titulo_visualizador = dialog.findViewById(R.id.titulo_visualizador);
        titulo_visualizador.setText("Biografía Pablo de Tarso");

        PDFView pdfView = dialog.findViewById(R.id.pdfVisualizador);
        //pdfView.fromAsset("1 - GRUPO 1.pdf")
        pdfView.fromAsset("BIOGRAFÍA PABLO DE TARSO.pdf")
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

    private void cargarVisualizador(String c, String b) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        assert dialog.getWindow() != null;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.visualizador_pdf);

        titulo_visualizador = dialog.findViewById(R.id.titulo_visualizador);
        titulo_visualizador.setText(selectPDF(c,b));

        PDFView pdfView = dialog.findViewById(R.id.pdfVisualizador);
        //pdfView.fromAsset("1 - GRUPO 1.pdf")
        pdfView.fromAsset(selectPDF(c,b))
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

    private String selectPDF(String c, String b) {
        String nombre = "";
        if (c.equals("1") && b.equals("Burbuja 1")){
            nombre = "1 - GRUPO 1.pdf";
        } else if (c.equals("1") && b.equals("Burbuja 2")){
            nombre = "1 - GRUPO 2.pdf";
        } else if (c.equals("2") && b.equals("Burbuja 1")){
            nombre = "2 - GRUPO 1.pdf";
        } else if (c.equals("2") && b.equals("Burbuja 2")){
            nombre = "2 - GRUPO 2.pdf";
        } else if (c.equals("3") && b.equals("Burbuja 1")){
            nombre = "3 - GRUPO 1.pdf";
        } else if (c.equals("3") && b.equals("Burbuja 2")){
            nombre = "3 - GRUPO 2.pdf";
        } else {
            nombre = "sin Contenido";
        }
        return nombre;

    }


}
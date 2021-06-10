package com.emdev.tarso.ui.utilidades;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emdev.tarso.MapasActivity;
import com.emdev.tarso.QuimicaActivity;
import com.emdev.tarso.R;
import com.emdev.tarso.TraductorActivity;
import com.emdev.tarso.calculadoraActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class UtilidadesFragment extends Fragment {

    private UtilidadesViewModel mViewModel;

    CircleImageView btnCalc, btnQuimica, btnMapas, btnTraductor, btnAplicaciones, btn6;

    public static UtilidadesFragment newInstance() {
        return new UtilidadesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(UtilidadesViewModel.class);
        View root = inflater.inflate(R.layout.utilidades_fragment, container, false);

        btnCalc = root.findViewById(R.id.btnCalc);
        btnQuimica = root.findViewById(R.id.btnQuimica);
        btnMapas = root.findViewById(R.id.btnMapas);
        btnTraductor = root.findViewById(R.id.btnTrad);
        btnAplicaciones = root.findViewById(R.id.apps);
        btn6 = root.findViewById(R.id.b6);

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irACalcu = new Intent(getContext(), calculadoraActivity.class);
                startActivity(irACalcu);

                /*Intent irACalcu = new Intent();
                irACalcu.makeMainSelectorActivity (Intent.ACTION_MAIN,
                        Intent.CATEGORY_APP_CALCULATOR);
                startActivity(irACalcu);*/
            }
        });

        btnQuimica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irAQuimica = new Intent(getContext(), QuimicaActivity.class);
                startActivity(irAQuimica);
            }
        });

        btnMapas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irAMapas = new Intent(getContext(), MapasActivity.class);
                startActivity(irAMapas);
            }
        });

        btnTraductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irATraductor = new Intent(getContext(), TraductorActivity.class);
                startActivity(irATraductor);
            }
        });

        btnAplicaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return root;
    }



}
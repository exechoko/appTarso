package com.emdev.tarso.ui.biblioteca;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emdev.tarso.R;
import com.emdev.tarso.ui.gallery.GalleryViewModel;

public class BibliotecaFragment extends Fragment {

    private BibliotecaViewModel mViewModel;

    public static BibliotecaFragment newInstance() {
        return new BibliotecaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(BibliotecaViewModel.class);
        View root = inflater.inflate(R.layout.biblioteca_fragment, container, false);

        return root;
    }


}
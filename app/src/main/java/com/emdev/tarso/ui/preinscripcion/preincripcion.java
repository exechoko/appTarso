package com.emdev.tarso.ui.preinscripcion;

import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.emdev.tarso.R;

public class preincripcion extends Fragment {

    private PreincripcionViewModel mViewModel;

    public static preincripcion newInstance() {
        return new preincripcion();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.preincripcion_fragment, container, false);

        TextView fecha;
        fecha = root.findViewById(R.id.fecha);
        WebView webView = root.findViewById(R.id.webViewFormulario);

        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        //webView.loadUrl("https://fundacionpresenciapresente.org.ar/");

        return root;
    }



}
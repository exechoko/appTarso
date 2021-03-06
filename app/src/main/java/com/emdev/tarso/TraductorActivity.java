package com.emdev.tarso;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TraductorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traductor);

        WebView webViewTraductor = findViewById(R.id.webViewTraductor);

        if (Build.VERSION.SDK_INT >= 19) {
            webViewTraductor.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webViewTraductor.getSettings().setJavaScriptEnabled(true);
        webViewTraductor.setWebViewClient(new WebViewClient());
        webViewTraductor.loadUrl("https://translate.google.com.ar/");
    }
}
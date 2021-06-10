package com.emdev.tarso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MapasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);

        WebView webViewMapas = findViewById(R.id.webViewMapas);

        if (Build.VERSION.SDK_INT >= 19) {
            webViewMapas.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webViewMapas.getSettings().setJavaScriptEnabled(true);
        webViewMapas.setWebViewClient(new WebViewClient());
        webViewMapas.loadUrl("https://www.ign.gob.ar/AreaServicios/Descargas/MapasEscolares/");

        webViewMapas.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                /*DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setMimeType(mimetype);

                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));

                request.setDescription("Downloading file...");

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));

                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();

                BroadcastReceiver onComplete = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Toast.makeText(getApplicationContext(),"Downloading Complete",Toast.LENGTH_SHORT).show();
                    }
                };

                registerReceiver(onComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));*/

            }
        });
    }
}
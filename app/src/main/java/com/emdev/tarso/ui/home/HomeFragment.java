package com.emdev.tarso.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.emdev.tarso.MainActivity;
import com.emdev.tarso.MenuEstudiantesActivity;
import com.emdev.tarso.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    WebView webView;
    SwipeRefreshLayout swipe;

    String facebookId = "fb://page/111949687597169";
    String urlFacebookPage = "https://www.facebook.com/EMDevSoftware";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView emdev;
        emdev = root.findViewById(R.id.emdev);
        webView = root.findViewById(R.id.webView);

        swipe = root.findViewById(R.id.swipeContainer);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadWeb();
            }
        });
        LoadWeb();

        emdev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Click en el banner", Toast.LENGTH_SHORT).show();
                dialogEMDEV();
            }
        });

        return root;
    }

    private void LoadWeb() {
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //webView.getSettings().setJavaScriptEnabled(true);
        //webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://fundacionpresenciapresente.org.ar/");

        swipe.setRefreshing(true);
        webView.setWebViewClient(new WebViewClient()
        {
            public  void  onPageFinished(WebView view, String url){
                swipe.setRefreshing(false);
            }
        });
    }

    private void dialogEMDEV() {
//        AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
//        alerta.setTitle("IR A EMDev");
//        alerta.setMessage("¿Desea ir a ?")
//                .setCancelable(false)
//                .setPositiveButton("IR a EMDev", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId )));
                        } catch (Exception e) {
                            //Log.e(TAG, "Aplicación no instalada.");
                            //Abre url de pagina.
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlFacebookPage)));
                        }
//                    }
//                })
//                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        alerta.show();
    }
}
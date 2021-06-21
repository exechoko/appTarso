package com.emdev.tarso;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(Uri.parse(url).getHost().endsWith

                (/*"http://ia800303.us.archive.org/5/items/Nasser_Al_Qatami_1433/001.mp3"*/"http://node-11.zeno.fm:80/cc0tgb10yv8uv.mp3")){
            return false;        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        view.getContext().startActivity(intent);

        return true;    }
}

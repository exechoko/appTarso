package com.emdev.tarso;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emdev.tarso.Interface.Playable;
import com.emdev.tarso.Notifications.CreateNotification;
import com.emdev.tarso.Services.OnClearFromRecentService;
import com.emdev.tarso.model.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RadioActivity extends AppCompatActivity implements Playable {

    CircleImageView play;
    Button abrirNavegador;
    TextView title;
    WebView w;

    NotificationManager notificationManager;

    List<Track> tracks;

    int position = 0;
    boolean isPlaying = false;

    private MediaPlayer player;
    private String STREAM_URL = "http://node-11.zeno.fm:80/cc0tgb10yv8uv.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        RadioActivity.this.setTitle("FM Tarso");

        play = findViewById(R.id.play);
        title = findViewById(R.id.title);
        player = new MediaPlayer();
        abrirNavegador = findViewById(R.id.abrirEnElNavegador);

        //webAudio();
        /*w = findViewById(R.id.web);
        w.getSettings().setJavaScriptEnabled(true);
        w.getSettings().setAllowContentAccess(true);
        w.getSettings().setDomStorageEnabled(true);
        w.setWebViewClient(new WebViewClient());
        if (Build.VERSION.SDK_INT >= 19) {
            w.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            w.getSettings().setMediaPlaybackRequiresUserGesture(false);
            w.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        w.loadUrl("https://zeno.fm/fmTarso/");*/

        /*webLinkRadio.getSettings().setJavaScriptEnabled(true);
        webLinkRadio.getSettings().setAllowContentAccess(true);
        webLinkRadio.getSettings().setDomStorageEnabled(true);
        webLinkRadio.setWebViewClient(new WebViewClient());

        if (Build.VERSION.SDK_INT >= 19) {
            webLinkRadio.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webLinkRadio.getSettings().setMediaPlaybackRequiresUserGesture(false);
            webLinkRadio.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webLinkRadio.loadUrl("https://zeno.fm/fmTarso/");*/

        popluateTracks();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    onTrackPause();
                } else {
                    onTrackPlay();
                }
            }
        });

        abrirNavegador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri link = Uri.parse("https://zeno.fm/fmTarso/");
                Intent i = new Intent(Intent.ACTION_VIEW, link);
                startActivity(i);
            }
        });

        initializeMediaPlayer();
    }

    /*private void webAudio() {
        w=findViewById(R.id.web);

        String about=
                "<html>\n"+
                        "<body>\n" +
                        "\t<h6>PLAY</h6>\n" +
                        "\t<br>\n" +
                        "<audio " +
                        "class=\"audio\" style=\"width: 100%;display: block;height: auto !important;padding-bottom:0;\""+
                "controls=\"\" autoplay=\"\" name=\"media\">" +
                "<source src=\"http://node-11.zeno.fm:80/cc0tgb10yv8uv.mp3\" " +
                "type=\"audio/mpeg\">" +
                "</audio>" +
                "</body>" +
                "</html>";

        TypedArray ta = obtainStyledAttributes(new int[]{android.R.attr.textColorPrimary, R.attr.colorAccent});
        String textColor = String.format("#%06X", (0xFFFFFF & ta.getColor(1, Color.WHITE)));
        String accentColor = String.format("#%06X", (0xFFFFFF & ta.getColor(9, Color.BLUE)));
        ta.recycle();
        about = "<style media=\"screen\" type=\"text/css\">" +
                "body {\n" +
                "    color:" + textColor + ";\n" +
                "}\n" +
                "a:link {color:" + accentColor + "}\n" +
                "</style>" + about;
        w.setBackgroundColor(Color.BLACK);

        w.getSettings().setJavaScriptEnabled(true);

        w.setWebViewClient(new MyWebViewClient() {
            @Override

            public boolean shouldOverrideUrlLoading(WebView view, String Url) {
                return super.shouldOverrideUrlLoading(view, Url);            }
        });        w.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        w.getSettings().setAppCacheEnabled(true);

        w.getSettings().setDomStorageEnabled(true);

        w.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        w.getSettings().setUseWideViewPort(true);

        w.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        w.getSettings().setAllowUniversalAccessFromFileURLs(true);

        w.setFocusable(true);

        w.setScrollBarStyle(w.SCROLLBARS_OUTSIDE_OVERLAY);

        w.getSettings().getLoadWithOverviewMode();

        w.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        w.getSettings().setDisplayZoomControls(true);

        w.getSettings().setDatabaseEnabled(true);

        w.loadData(about, "text/html", "UTF-8");
    }*/

    private void startPlaying() {
        /*buttonStopPlay.setEnabled(true);
        buttonStopPlay.setVisibility(View.VISIBLE);
        buttonPlay.setEnabled(false);

        playSeekBar.setVisibility(View.VISIBLE);*/
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                //player.start();
                mp.start(); //antes player.start()
            }
        });
    }

    private void stopPlaying() {
        /*if (player.isPlaying()) {

        }*/
        player.stop();
        player.release();
        initializeMediaPlayer();

        //buttonPlay.setEnabled(true);
        //buttonStopPlay.setVisibility(View.GONE);
        //buttonStopPlay.setEnabled(false);
        //playSeekBar.setVisibility(View.INVISIBLE);
    }

    private void initializeMediaPlayer() {
        try {
            player.reset();
            player.setDataSource(STREAM_URL);
            //player.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                playSeekBar.setSecondaryProgress(percent);
                Log.i("Buffering", "" + percent);
            }
        });*/
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "EMDev", NotificationManager.IMPORTANCE_LOW);

            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    //populate list with tracks
    private void popluateTracks(){
        tracks = new ArrayList<>();

        //tracks.add(new Track("Track 1", "Artist 1", R.drawable.t1));
        tracks.add(new Track("FM Tarso", "Fund. Presencia Presente", R.drawable.t2));
        tracks.add(new Track("FM Tarso", "Fund. Presencia Presente", R.drawable.t3));
        //tracks.add(new Track("Track 4", "Artist 4", R.drawable.t4));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");

            switch (action){
                case CreateNotification.ACTION_PREVIUOS:
                    onTrackPrevious();
                    break;
                case CreateNotification.ACTION_PLAY:
                    if (isPlaying){
                        onTrackPause();
                    } else {
                        onTrackPlay();
                    }
                    break;
                case CreateNotification.ACTION_NEXT:
                    onTrackNext();
                    break;
            }
        }
    };

    @Override
    public void onTrackPrevious() {

        position--;
        CreateNotification.createNotification(RadioActivity.this, tracks.get(position),
                R.drawable.ic_pause, position, tracks.size()-1);
        title.setText(tracks.get(position).getTitulo());

    }

    @Override
    public void onTrackPlay() {

        CreateNotification.createNotification(RadioActivity.this, tracks.get(position),
                R.drawable.ic_pause, position, tracks.size()-1);
        play.setImageResource(R.drawable.pause);
        title.setText(tracks.get(position).getTitulo());
        isPlaying = true;
        startPlaying();

    }

    @Override
    public void onTrackPause() {

        CreateNotification.createNotification(RadioActivity.this, tracks.get(position),
                R.drawable.ic_play, position, tracks.size()-1);
        play.setImageResource(R.drawable.play);
        title.setText(tracks.get(position).getTitulo());
        isPlaying = false;
        stopPlaying();

    }

    @Override
    public void onTrackNext() {

        position++;
        CreateNotification.createNotification(RadioActivity.this, tracks.get(position),
                R.drawable.ic_pause, position, tracks.size()-1);
        title.setText(tracks.get(position).getTitulo());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.cancelAll();
        }

        unregisterReceiver(broadcastReceiver);
    }
}
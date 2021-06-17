package com.emdev.tarso;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    TextView title;

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

        play = findViewById(R.id.play);
        title = findViewById(R.id.title);

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

        initializeMediaPlayer();
    }

    private void startPlaying() {
        /*buttonStopPlay.setEnabled(true);
        buttonStopPlay.setVisibility(View.VISIBLE);
        buttonPlay.setEnabled(false);

        playSeekBar.setVisibility(View.VISIBLE);*/
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
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
        player = new MediaPlayer();
        try {
            player.reset();
            player.setDataSource(STREAM_URL);
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
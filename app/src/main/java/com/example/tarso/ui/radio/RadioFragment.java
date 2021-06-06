package com.example.tarso.ui.radio;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.tarso.R;
import com.example.tarso.ui.utilidades.UtilidadesViewModel;

import java.io.IOException;

public class RadioFragment extends Fragment {

    private RadioViewModel mViewModel;

    //agregar todos los botones por ejemplo
    /*Button btnAudioStream;
    ImageView logo;

    private boolean playPause;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;*/

    private ProgressBar playSeekBar;
    private Button buttonPlay;
    private Button buttonStopPlay;
    private MediaPlayer player;

    /*Button button_stop;
    Button button_play;*/
    //private String STREAM_URL ="https://www.ssaurel.com/tmp/mymusic.mp3";
    private String STREAM_URL = "http://node-11.zeno.fm:80/cc0tgb10yv8uv.mp3";
    private MediaPlayer mPlayer;

    public static RadioFragment newInstance() {
        return new RadioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(RadioViewModel.class);
        View root = inflater.inflate(R.layout.radio_fragment, container, false);

        /*btnAudioStream = root.findViewById(R.id.btnAudioStream);
        logo = root.findViewById(R.id.logo);*/

        /*
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(getContext()); //this
        btnAudioStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playPause) {
                    btnAudioStream.setText("Pausar Radio");

                    if (initialStage) {
                        new Player().execute("http://node-11.zeno.fm/cc0tgb10yv8uv");
                        //new Player().execute("http://node-11.zeno.fm:80/cc0tgb10yv8uv.mp3");
                    } else {
                        if (!mediaPlayer.isPlaying())
                            mediaPlayer.start();
                    }

                    playPause = true;

                } else {
                    btnAudioStream.setText("Escuchar Radio");

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }

                    playPause = false;
                }
            }
        });*/

        playSeekBar = (ProgressBar) root.findViewById(R.id.progressBar1);
        playSeekBar.setMax(100);
        playSeekBar.setVisibility(View.INVISIBLE);

        buttonPlay = (Button) root.findViewById(R.id.btnAudioStream);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaying();
            }
        });

        buttonStopPlay = (Button) root.findViewById(R.id.btnStopAudioStream);
        buttonStopPlay.setEnabled(false);
        buttonStopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
            }
        });

        initializeMediaPlayer();

        /*button_stop=(Button) root.findViewById(R.id.btnStopAudioStream);
        button_play=(Button) root.findViewById(R.id.btnAudioStream);
        mPlayer=new MediaPlayer();
        button_play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try{

                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    //mediaPlayer.prepare(); // might take long! (for buffering, etc)
                    //mediaPlayer.start();

                    //mPlayer.reset();
                    mPlayer.setDataSource(STREAM_URL);
                    mPlayer.prepareAsync();
                    mPlayer.setOnPreparedListener(new MediaPlayer.
                            OnPreparedListener(){
                        @Override
                        public void onPrepared(MediaPlayer mp){
                            mp.start();
                        }
                    });
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        button_stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mPlayer.stop();
            }
        });*/

        return root;
    }

    private void startPlaying() {
        buttonStopPlay.setEnabled(true);
        buttonStopPlay.setVisibility(View.VISIBLE);
        buttonPlay.setEnabled(false);

        playSeekBar.setVisibility(View.VISIBLE);
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start(); //antes player.start()
            }
        });
    }

    private void stopPlaying() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
            initializeMediaPlayer();
        }

        buttonPlay.setEnabled(true);
        buttonStopPlay.setVisibility(View.GONE);
        buttonStopPlay.setEnabled(false);
        playSeekBar.setVisibility(View.INVISIBLE);
    }

    private void initializeMediaPlayer() {
        player = new MediaPlayer();
        try {
            player.reset();
            player.setDataSource(STREAM_URL);
            //player.setDataSource("http://server.laradio.online:25224/live.mp3");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                playSeekBar.setSecondaryProgress(percent);
                Log.i("Buffering", "" + percent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player.isPlaying()) {
            //player.stop();
        }
    }
/*
    @Override
    public void onDestroy() {
        super.onDestroy();
    }*/

    /*class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        initialStage = true;
                        playPause = false;
                        btnAudioStream.setText("Escuchar Radio");
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                Log.e("MyAudioStreamingApp", e.getMessage());
                prepared = false;
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }

            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Cargando...");
            progressDialog.show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }*/
}
package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayer extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
    MediaPlayer mediaPlayer;
    ImageView pause,next,prev;
    SeekBar seekBar;
    TextView currentSong,currentDuration,totalDuration;
    ArrayList songsList;
    String [] songName;
    int [] position;
    Uri uri;
    int currentTime ,totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        Objects.requireNonNull(getSupportActionBar()).hide();
        pause = findViewById(R.id.pause);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        seekBar = findViewById(R.id.seekBar);
        currentSong = findViewById(R.id.currentSong);
        currentDuration = findViewById(R.id.currentDuration);
        totalDuration = findViewById(R.id.totalDuration);

        Intent intent = getIntent();
        position = new int[]{intent.getIntExtra("position", 0)};
        songName = intent.getStringArrayExtra("songName");
        Bundle bundle = intent.getExtras();
        songsList = bundle.getParcelableArrayList("songList");
        prepareSongForPlay();
        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            mediaPlayer.release();
            if(position[0] != songsList.size()-1){
                position[0] = position[0] + 1 ;
            }
            else{
                position[0] = 0;
            }
            prepareSongForPlay();
        });
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        },0,800);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                currentDuration.setText(i/60000 + " : " + (i%60000)/1000);
                if(b){
                    mediaPlayer.seekTo(i);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        pause.setOnClickListener(View ->{
            if(mediaPlayer.isPlaying()){
                pause.setImageResource(R.drawable.play);
                mediaPlayer.pause();
            }
            else{
                pause.setImageResource(R.drawable.pause);
                mediaPlayer.start();
            }
        });
        next.setOnClickListener(view -> {
            mediaPlayer.release();
            if(position[0] != songsList.size()-1){
                position[0] = position[0] + 1 ;
            }
            else{
                position[0] = 0;
            }
            prepareSongForPlay();
            Log.d("check", "onClick: " + position[0]);
            Log.d("check", "onClick: "+uri);
        });
        prev.setOnClickListener(view -> {
            mediaPlayer.release();
            if (position[0] != 0) {
                position[0] = position[0] - 1;
            }
            else{
                position[0] =songsList.size()-1;
            }
            prepareSongForPlay();
            Log.d("check", "onClick: " + position[0]);
            Log.d("check", "onClick: "+uri);
        });

    }
    @SuppressLint("SetTextI18n")
    public void prepareSongForPlay(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        );
        try {
            uri = Uri.parse(songsList.get(position[0]).toString());
            mediaPlayer.setDataSource(getApplicationContext(),uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            currentSong.setText(songName[position[0]]);
            currentSong.setSelected(true);
            seekBar.setMax(mediaPlayer.getDuration());
            totalDuration.setText(mediaPlayer.getDuration()/60000 + " : " + (mediaPlayer.getDuration()%60000)/1000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
package com.example.imusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class play extends AppCompatActivity {
ImageView play, next , previous;

TextView tx;
MediaPlayer mediaPlayer;
SeekBar seekBar;
int position;
Thread seek;
ArrayList<File> songs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
       play = findViewById(R.id.play);
       next = findViewById(R.id.next);
       previous = findViewById(R.id.previous);
       seekBar = findViewById(R.id.seekBar);
       tx = findViewById(R.id.textView3);
       Intent intent = getIntent();
       Bundle bundle = intent.getExtras();
       songs = (ArrayList) bundle.getParcelableArrayList("Songs");
       position = intent.getIntExtra("Position" , 0);
       String textContent = intent.getStringExtra("currentSong");
        playSong(position);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.play);
                }
                else{
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pause);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                play.setImageResource(R.drawable.pause);
                if(position== (songs.size()-1)){
                    position = 0;
                }
                else{
                    position++;
                }
                playSong(position);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                play.setImageResource(R.drawable.pause);
                if (position == 0){
                    position = songs.size() - 1 ;
                }
                else {
                    position --;
                }
                playSong(position);
            }
        });

    }

    public void playSong(int position){
         Uri uri = Uri.parse(songs.get(position).toString());
    mediaPlayer = MediaPlayer.create(play.this , uri);
    mediaPlayer.start();
    tx.setText(songs.get(position).getName());
    tx.setSelected(true);
    seekBar.setMax(mediaPlayer.getDuration());
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    });
    seek = new Thread(){

        @Override
        public void run() {
            int current =  0 ;
            try {
    while(current<mediaPlayer.getDuration()){
    current = mediaPlayer.getCurrentPosition();
    seekBar.setProgress(current);
    sleep(500);

}
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    };
    seek.start();
}


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        seek.interrupt();
    }
}
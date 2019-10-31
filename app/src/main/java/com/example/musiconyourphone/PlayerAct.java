package com.example.musiconyourphone;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class PlayerAct extends AppCompatActivity {

    Button pushBtn,nextBtn,prevBtn;
    TextView songtitle;
    SeekBar seekBar;

    MediaPlayer myMediaPlayer;
    int position;
    ArrayList<File> mysong;
    Thread updateseekber;

    String sname;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        pushBtn=findViewById(R.id.pussId);
        nextBtn=findViewById(R.id.nextId);
        prevBtn=findViewById(R.id.prevId);

        seekBar=findViewById(R.id.sekbarId);
        songtitle=findViewById(R.id.songTitle);

        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        updateseekber=new Thread(){
            @Override
            public void run() {
                int totalduration=myMediaPlayer.getDuration();
                int currentposition=0;

                while (currentposition<totalduration)
                {
                    try
                    {

                        sleep(500);
                        currentposition=myMediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentposition);

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        if (myMediaPlayer!=null)
        {
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mysong=(ArrayList) bundle.getParcelableArrayList("songs");

        sname=mysong.get(position).getName().toString();

        String songnames=i.getStringExtra("songName");

        songtitle.setText(songnames);
        songtitle.setSelected(true);

        position=bundle.getInt("pos",0);

        Uri uri=Uri.parse(mysong.get(position).toString());

        myMediaPlayer=MediaPlayer.create(this,uri);
        myMediaPlayer.start();
        seekBar.setMax(myMediaPlayer.getDuration());
        updateseekber.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

               myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        pushBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setMax(myMediaPlayer.getDuration());

                if (myMediaPlayer.isPlaying())
                {
                    pushBtn.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();
                }else
                {
                    pushBtn.setBackgroundResource(R.drawable.icon_pause);
                    myMediaPlayer.start();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.start();
                myMediaPlayer.release();
                position=((position+1)%mysong.size());

                Uri uri=Uri.parse(mysong.get(position).toString());
                myMediaPlayer= (MediaPlayer) MediaPlayer.create(getApplicationContext(),uri);
                sname=mysong.get(position).getName().toString();
                songtitle.setText(sname);
                myMediaPlayer.start();
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.start();
                myMediaPlayer.release();
                position=((position-1)<0)?(mysong.size()-1):(position-1);

                Uri uri=Uri.parse(mysong.get(position).toString());
                myMediaPlayer= (MediaPlayer) MediaPlayer.create(getApplicationContext(),uri);
                sname=mysong.get(position).getName().toString();
                songtitle.setText(sname);
                myMediaPlayer.start();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

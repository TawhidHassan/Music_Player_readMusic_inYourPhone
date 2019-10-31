package com.example.musiconyourphone;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView mysongList;
    String[] items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mysongList=findViewById(R.id.mysongListviewId);
        runtimepermetion();
    }


    //permition for storgae use  "Dexter" github
    public void runtimepermetion()
    {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        token.continuePermissionRequest();
                    }
                }).check();
    }

    //find song
    public ArrayList<File> findsong(File file){

        ArrayList<File>arrayList=new ArrayList<>();
        File[] files=file.listFiles();

        for (File singlefile:files)
        {
            if (singlefile.isDirectory() && !singlefile.isHidden())
            {
                arrayList.addAll(findsong(singlefile));
            }
            else if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav"))
            {
                arrayList.add(singlefile);
            }
        }

        return arrayList;
    }

    //display song

    void display()
    {
        final ArrayList<File> mysongs=findsong(Environment.getExternalStorageDirectory());
        items=new String[mysongs.size()];

        int i;
        for (i=0;i<mysongs.size();i++)
        {
            items[i]=mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }

        ArrayAdapter<String> myAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,items);
        mysongList.setAdapter(myAdapter);

        mysongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String songname=mysongList.getItemAtPosition(position).toString();


                startActivity(new Intent(getApplicationContext(),PlayerAct.class)
                                    .putExtra("songs",mysongs).putExtra("songName",songname).putExtra("pos",position));
            }
        });
    }


}

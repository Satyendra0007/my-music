package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.Manifest.permission;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        recyclerView = findViewById(R.id.recyclerView);
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ArrayList<File> songList = getSongFromStorage(Environment.getExternalStorageDirectory());
                        String [] songName = new String[songList.size()];
                        for (int i=0; i<songList.size(); i++){
                            songName[i] = songList.get(i).getName().replace(".mp3","");
                        }
                        MyAdapter adapter = new MyAdapter(getApplicationContext(),songName,songList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .check();
    }
    public ArrayList<File> getSongFromStorage(File file) {
        ArrayList<File> songs = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null) {
            for (File givenFile : files) {
                if (givenFile.isDirectory() && !givenFile.isHidden()) {
                    songs.addAll(getSongFromStorage(givenFile));
                } else {
                    if (givenFile.getName().endsWith(".mp3")) {
                        songs.add(givenFile);
                    }
                }
            }
        }
        return songs;
    }
}
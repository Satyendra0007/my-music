package com.example.mymusic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    String [] songNameList ;
    Context context;
    ArrayList<File> songList;

    MyAdapter(Context context ,String [] songNameList,ArrayList<File> songList){
        this.context =context;
        this.songNameList = songNameList;
        this.songList = songList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.song_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.songName.setText(songNameList[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MusicPlayer.class);
                String songName = songNameList[position];
                intent.putExtra("songList",songList);
                intent.putExtra("position",position);
                intent.putExtra("songName", songNameList);
                holder.itemView.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return songNameList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView songBanner;
        TextView songName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songBanner = itemView.findViewById(R.id.songBanner);
            songName = itemView.findViewById(R.id.songName);
        }
    }
}

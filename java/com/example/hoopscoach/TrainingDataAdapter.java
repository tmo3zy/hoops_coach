package com.example.hoopscoach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrainingDataAdapter extends RecyclerView.Adapter<TrainingDataAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<PlayerData> data;

    TrainingDataAdapter(Context context, List<PlayerData> data){
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TrainingDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayerData playerData = data.get(position);
        holder.playerName.setText(playerData.playerName);
        holder.isCompleteImg.setImageResource(playerData.imgResource);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView isCompleteImg;
        final TextView playerName;

        ViewHolder(View v){
            super(v);
            isCompleteImg = v.findViewById(R.id.imageView3);
            playerName = v.findViewById(R.id.tv_playerDataName);
        }
    }

}

package com.example.hoopscoach;

import android.widget.ImageView;

public class PlayerData {
    public String playerName;
    public int imgResource;

    public PlayerData(){

    }

    public PlayerData(String playerName, int imgResource) {
        this.playerName = playerName;
        this.imgResource = imgResource;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }
}

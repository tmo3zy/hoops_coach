package com.example.hoopscoach;

import java.util.ArrayList;

public class Team {
    public String teamName, coach_id;
    public ArrayList<User> players = new ArrayList<>();
    public ArrayList<Training> trainings = new ArrayList<>();


    public Team(){

    }
    public Team(String teamName, String coach_id){
        this.teamName = teamName;
        this.coach_id = coach_id;
    }
}

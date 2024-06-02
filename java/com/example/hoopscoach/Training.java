package com.example.hoopscoach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Training {
    public String title, type, specialization;
    public ArrayList<String> exercises = new ArrayList<>();
    public Map<String, Object> execution = new HashMap<>();

    public Training(){

    }

    public Training(String title, String type, String specialization) {
        this.title = title;
        this.type = type;
        this.specialization = specialization;
    }

}

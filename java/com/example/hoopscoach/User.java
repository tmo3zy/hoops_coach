package com.example.hoopscoach;

public class User {
    public String id, name, last_name, email, role, teamName;

    public User(){

    }

    public User(String id, String name, String last_name, String email, String role, String teamName) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.role = role;
        this.teamName = teamName;
    }

    public User(String id, String name, String last_name, String email,  String teamName) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.teamName = teamName;
    }
}

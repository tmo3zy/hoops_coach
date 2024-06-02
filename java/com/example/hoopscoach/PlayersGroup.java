package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.groupsRef;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PlayersGroup {
    public String name;
    ArrayList<String> membersNames, membersId;

    public PlayersGroup(String name) {
        this.name = name;
        this.membersNames = new ArrayList<>();
        this.membersId = new ArrayList<>();
    }

    public ArrayList<String> getMembersId() {
        return membersId;
    }
}
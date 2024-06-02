package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.coachRef;
import static com.example.hoopscoach.MainActivity.exercisesRef;
import static com.example.hoopscoach.MainActivity.groupsRef;
import static com.example.hoopscoach.MainActivity.playersRef;
import static com.example.hoopscoach.MainActivity.team;
import static com.example.hoopscoach.MainActivity.teamRef;
import static com.example.hoopscoach.MainActivity.trainingsRef;
import static com.example.hoopscoach.MainActivity.user;
import static com.example.hoopscoach.MainActivity.userRef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {

    private EditText team_name;
    private Button add_team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        team_name = findViewById(R.id.et_teamName);
        add_team = findViewById(R.id.btn_addTeam);
        add_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(team_name.getText().toString())){
                    teamRef.document(team_name.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot ds = task.getResult();
                                if(ds.exists()){
                                    Toast.makeText(CreateActivity.this, "Это название уже занято", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    team = new Team(team_name.getText().toString(), user.getUid());
                                    Map<String, Object> teamMap = new HashMap<>();
                                    teamMap.put("team_name", team.teamName);
                                    teamMap.put("coachA_id", team.coach_id);
                                    teamRef.document(team.teamName).set(teamMap);
                                    coachRef.document(user.getEmail()).update("teamName", team.teamName);
                                    finish();
                                    playersRef = teamRef.document(team.teamName).collection("Players");
                                    trainingsRef = teamRef.document(team.teamName).collection("Trainings");
                                    exercisesRef = teamRef.document(team.teamName).collection("Exercises");
                                    groupsRef = teamRef.document(team.teamName).collection("Groups");
                                }
                            }
                        }
                    });
                }
            }
        });

    }
}
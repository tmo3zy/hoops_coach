package com.example.hoopscoach;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Path;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    public static FirebaseAuth auth;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static CollectionReference userRef, coachRef, playerRef, teamRef, playersRef, trainingsRef, exercisesRef, groupsRef;

    public static FirebaseUser user;

    private String USER_KEY = "User";
    private String COACH_KEY = "Coach";
    private String PLAYER_KEY = "Player";
    private String TEAM_KEY = "Team";
    private String PLAYERS_KEY = "Players";
    private String TRAININGS_KEY = "Trainings";
    private String EXERCISES_KEY = "Exercises";
    private String GROUPS_KEY = "Groups";
    public static String authName, authLastName;
    public static Team team;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = auth.getCurrentUser();
        if(user != null){
            linearLayout.setVisibility(View.GONE);
            userRef.document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot ds = task.getResult();
                        if (ds.exists() && ds.getString("email").equals(user.getEmail())){
                            authName = ds.getString("name");
                            authLastName = ds.getString("last_name");
                            Log.d(TAG, "Document snapshot data: " + ds.getData());
                        }else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            playerRef.document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot ds = task.getResult();
                        if(ds.exists() && ds.getString("email").equals(user.getEmail())){
                            Intent i = new Intent(MainActivity.this, PlayerNavActivity.class);

                            if(!ds.getString("teamName").equals("None")){
                                if(! ds.getString("teamName").equals("None")){

                                    teamRef.document(ds.getString("teamName")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot doc = task.getResult();
                                            team = new Team(doc.getString("team_name"), doc.getString("coach_id"));
                                            playersRef = teamRef.document(team.teamName).collection(PLAYERS_KEY);
                                            trainingsRef = teamRef.document(team.teamName).collection(TRAININGS_KEY);
                                            exercisesRef = teamRef.document(team.teamName).collection(EXERCISES_KEY);
                                            groupsRef = teamRef.document(team.teamName).collection(GROUPS_KEY);

                                            playersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        for (QueryDocumentSnapshot doc: task.getResult()) {
                                                            team.players.add(new Player(doc.getString("id"), doc.getString("name"), doc.getString("last_name"), doc.getString("email"), team.teamName));
                                                        }
                                                    }
                                                }
                                            });
                                            trainingsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        for(QueryDocumentSnapshot doc: task.getResult()){
                                                            team.trainings.add(new Training(doc.getString("title"), doc.getString("type"), doc.getString("specialization")));
                                                        }
                                                    }
                                                }
                                            });

                                            startActivity(i);
                                        }
                                    });


                                }
                            }else{
                                team = null;
                                startActivity(i);
                            }
                        }else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            coachRef.document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot ds = task.getResult();
                        if(ds.exists() && ds.getString("email").equals(user.getEmail())){
                            Intent i = new Intent(MainActivity.this, CoachNavActivity.class);
                            if(! ds.getString("teamName").equals("None")){
                                team = new Team(ds.getString("teamName"), ds.getString("id"));

                                playersRef = teamRef.document(team.teamName).collection(PLAYERS_KEY);
                                trainingsRef = teamRef.document(team.teamName).collection(TRAININGS_KEY);
                                exercisesRef = teamRef.document(team.teamName).collection(EXERCISES_KEY);
                                groupsRef = teamRef.document(team.teamName).collection(GROUPS_KEY);

                                playersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for (QueryDocumentSnapshot doc: task.getResult()) {
                                                team.players.add(new Player(doc.getString("id"), doc.getString("name"), doc.getString("last_name"), doc.getString("email"), team.teamName));
                                                startActivity(i);
                                            }
                                        }
                                    }
                                });
                                trainingsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(QueryDocumentSnapshot doc: task.getResult()){
                                                team.trainings.add(new Training(doc.getString("title"), doc.getString("type"), doc.getString("specialization")));
                                            }
                                        }
                                    }
                                });
                            }else startActivity(i);
                        }else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    private void init(){
        auth = FirebaseAuth.getInstance();
        userRef = db.collection(USER_KEY);
        coachRef = db.collection(COACH_KEY);
        playerRef = db.collection(PLAYER_KEY);
        teamRef = db.collection(TEAM_KEY);
        linearLayout = findViewById(R.id.linlayout);
    }

    public void goToRegister(View view){
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    public void goToLogin(View view){
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }

}
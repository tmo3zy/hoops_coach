package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.groupsRef;
import static com.example.hoopscoach.MainActivity.playersRef;
import static com.example.hoopscoach.MainActivity.trainingsRef;
import static com.example.hoopscoach.MainActivity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowTrainingDataActivity extends AppCompatActivity {

    private TextView type, specialization, type1, specialization1, type2, specialization2, type3, specialization3, members;
    private ConstraintLayout  playerGroupTraining;
    private RelativeLayout personalOrTeamTraining, groupTraining, playerPersonalTeamLayout;
    private RecyclerView dataView;
    private ListView groupList, exercisesList;
    private ArrayList<String> groups = new ArrayList<>();
    private ArrayList<String> playersId = new ArrayList<>();
    private ArrayList<String> exercises = new ArrayList<>();
    private ArrayList<PlayerData> data = new ArrayList<>();
    private Button finishTraining, finishTraining1, completeTraining, completetraining1, back, back1, back2, back3, toExercises;
    private TrainingDataAdapter adapter;
    private ArrayAdapter<String> adapter1, adapter2;
    private String coachTrainingTitle, playerTrainingTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_training_data);
        Bundle extras = getIntent().getExtras();
        type = findViewById(R.id.tv_trainingType);
        specialization = findViewById(R.id.tv_training_specialization);
        dataView = findViewById(R.id.dataView);
        personalOrTeamTraining = findViewById(R.id.layout_personalTeamTrainingData);
        groupTraining = findViewById(R.id.layout_groupTrainingData);
        groupList = findViewById(R.id.groupList);
        type1 = findViewById(R.id.tv_trainingType2);
        specialization1 = findViewById(R.id.tv_training_specialization2);
        finishTraining = findViewById(R.id.btn_finishTraining);
        back = findViewById(R.id.btn_backToTrainings);
        type2 = findViewById(R.id.tv_playerTrainingType);
        specialization2 = findViewById(R.id.tv_playerTrainingSpecialization);
        type3 = findViewById(R.id.tv_playerTrainingType1);
        specialization3 = findViewById(R.id.tv_playerTrainingSpecialization1);
        members = findViewById(R.id.tv_members);
        exercisesList = findViewById(R.id.playerExercisesList);
        playerPersonalTeamLayout = findViewById(R.id.playerPersonalLayout);
        back1 = findViewById(R.id.btn_backToTrainings1);
        back2 = findViewById(R.id.btn_backToTrainings2);
        back3 = findViewById(R.id.btn_backToTrainings3);
        completeTraining = findViewById(R.id.btn_completeTraining);
        completetraining1 = findViewById(R.id.btn_completeTraining1);
        playerGroupTraining = findViewById(R.id.playerGroupTrainingLayout);
        toExercises = findViewById(R.id.btn_toTrainingExercises);
        completeTraining = findViewById(R.id.btn_completeTraining);
        completetraining1 = findViewById(R.id.btn_completeTraining1);
        finishTraining1 = findViewById(R.id.btn_finishTraining1);
        coachTrainingTitle = extras.getString("COACH_TRAINING_TITLE");
        playerTrainingTitle = extras.getString("playerTrainingTitle");
        members.setText("");


        if(coachTrainingTitle != null){
            setCoachInititalData(coachTrainingTitle);
            adapter = new TrainingDataAdapter(this, data);
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groups);
            dataView.setAdapter(adapter);
            groupList.setAdapter(adapter1);
        } else if (playerTrainingTitle != null) {
            setPlayerInitialData(playerTrainingTitle);
            adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercises);
            exercisesList.setAdapter(adapter2);
        }

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ShowTrainingDataActivity.this, ShowGroupExecutionActivity.class);
                i.putExtra("groupName", (String) groupList.getItemAtPosition(position));
                i.putExtra("trainingName", extras.getString("COACH_TRAINING_TITLE"));
                startActivity(i);
            }
        });


        View.OnClickListener finishListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        back.setOnClickListener(finishListener);
        back1.setOnClickListener(finishListener);
        back2.setOnClickListener(finishListener);
        back3.setOnClickListener(finishListener);

        toExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowTrainingDataActivity.this, ShowExercisesActivity.class);
                i.putExtra("trainingTitle", playerTrainingTitle);
                startActivity(i);
            }
        });
    }
    
    private void setCoachInititalData(String title){
        trainingsRef.document(title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if(doc.getString("type").equals("Group")){
                    personalOrTeamTraining.setVisibility(View.GONE);
                    playerPersonalTeamLayout.setVisibility(View.GONE);
                    playerGroupTraining.setVisibility(View.GONE);
                    groupTraining.setVisibility(View.VISIBLE);

                    type1.setText("Тип тренировки: " + doc.getString("type"));
                    specialization1.setText("Специализация: " +  doc.getString("specialization"));

                    Map<String, Object> groupsData = (HashMap)doc.get("execution");
                    for (Map.Entry<String, Object> entry: groupsData.entrySet()) {
                        groups.add(entry.getKey());
                        adapter1.notifyDataSetChanged();
                    }

                    finishTraining1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (String group: groups) {
                                groupsRef.document(group).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot ds = task.getResult();
                                        for (String member: (ArrayList<String>) ds.get("membersId")) {
                                            playersRef.document(member).update("current_trainings", FieldValue.arrayRemove(title));
                                        }
                                    }
                                });
                            }
                            trainingsRef.document(title).delete();
                            finish();
                        }
                    });

                }
                else{
                    groupTraining.setVisibility(View.GONE);
                    playerPersonalTeamLayout.setVisibility(View.GONE);
                    playerGroupTraining.setVisibility(View.GONE);
                    personalOrTeamTraining.setVisibility(View.VISIBLE);

                    type.setText("Тип тренировки: " + doc.getString("type"));
                    specialization.setText("Специализация: " + doc.getString("specialization"));
                    Map<String, Object> dataMap = (HashMap) doc.get("execution");
                    for(Map.Entry<String, Object> entry: dataMap.entrySet()){
                        PlayerData playerData = new PlayerData();
                        String id = entry.getKey();
                        playersId.add(id);
                        boolean value = (boolean) entry.getValue();
                        playersRef.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot ds = task.getResult();
                                String name = ds.getString("name") + " " + ds.getString("last_name") ;
                                playerData.setPlayerName(name);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        if(value){
                            playerData.setImgResource(R.drawable.baseline_check_24);
                        }else{
                            playerData.setImgResource(R.drawable.baseline_clear_24);
                        }
                        data.add(playerData);
                        adapter.notifyDataSetChanged();
                    }

                    finishTraining.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(String id: playersId){
                                playersRef.document(id).update("current_trainings", FieldValue.arrayRemove(coachTrainingTitle));
                                trainingsRef.document(coachTrainingTitle).delete();
                            }
                            finish();
                        }
                    });

                }

            }
        });
    }

    private void setPlayerInitialData(String title){
        trainingsRef.document(title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                if(ds.getString("type").equals("Personal") || ds.getString("type").equals("Team")){
                    groupTraining.setVisibility(View.GONE);
                    playerPersonalTeamLayout.setVisibility(View.VISIBLE);
                    playerGroupTraining.setVisibility(View.GONE);
                    personalOrTeamTraining.setVisibility(View.GONE);

                    type2.setText("Тип тренировки: " +  ds.getString("type"));
                    specialization2.setText("Специализация: " +  ds.getString( "specialization"));

                    exercises.addAll((ArrayList<String>) ds.get("exercises"));
                    adapter2.notifyDataSetChanged();

                    exercisesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent i = new Intent(ShowTrainingDataActivity.this, ShowExerciseDataActivity.class);
                            i.putExtra("title", (String) exercisesList.getItemAtPosition(position));
                            startActivity(i);
                        }
                    });

                    completeTraining.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            playersRef.document(user.getEmail()).update("current_trainings", FieldValue.arrayRemove(playerTrainingTitle));
                            playersRef.document(user.getEmail()).update("completed_trainings", FieldValue.increment(1));
                            playersRef.document(user.getEmail()).update("completed_" + ds.getString("specialization").toLowerCase() + "_trainings", FieldValue.increment(1));
                            trainingsRef.document(playerTrainingTitle).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot doc = task.getResult();
                                    Map<String, Object> execution = (HashMap) doc.get("execution");
                                    execution.put(user.getEmail(), true);
                                    trainingsRef.document(playerTrainingTitle).update("execution", execution);
                                }
                            });
                            finish();
                        }
                    });

                } else if (ds.getString("type").equals("Group")) {
                    groupTraining.setVisibility(View.GONE);
                    playerPersonalTeamLayout.setVisibility(View.GONE);
                    playerGroupTraining.setVisibility(View.VISIBLE);
                    personalOrTeamTraining.setVisibility(View.GONE);
                    type3.setText("Тип тренировки: " +  ds.getString("type"));
                    specialization3.setText("Специализация: " +  ds.getString( "specialization"));

                    trainingsRef.document(title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot ds = task.getResult();

                            Map<String, Object> groupsMap = (HashMap) ds.get("execution");
                            ArrayList<String> membersList = new ArrayList<>();
                            for(Map.Entry<String, Object> entry: groupsMap.entrySet()){
                                groupsRef.document(entry.getKey()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot doc = task.getResult();
                                        for (String id: (ArrayList<String>) doc.get("membersId")) {
                                            if(id.equals(user.getEmail())){
                                                membersList.addAll((ArrayList<String>) doc.get("members"));
                                                for (int i = 0; i < membersList.size(); i++) {
                                                    members.append(membersList.get(i) + "\n");
                                                }

                                                completetraining1.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Map<String, Object> groupExecutionMap = (HashMap) entry.getValue();
                                                        groupExecutionMap.put(user.getEmail(), true);
                                                        groupsMap.put(entry.getKey(), groupExecutionMap);
                                                        trainingsRef.document(title).update("execution", groupsMap);
                                                        playersRef.document(user.getEmail()).update("current_trainings", FieldValue.arrayRemove(playerTrainingTitle));
                                                        playersRef.document(user.getEmail()).update("completed_trainings", FieldValue.increment(1));
                                                        playersRef.document(user.getEmail()).update("completed_" + ds.getString("specialization").toLowerCase() + "_trainings", FieldValue.increment(1));
                                                        finish();
                                                    }
                                                });

                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}
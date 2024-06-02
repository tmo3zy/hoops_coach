package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.exercisesRef;
import static com.example.hoopscoach.MainActivity.groupsRef;
import  static com.example.hoopscoach.MainActivity.playersRef;
import static com.example.hoopscoach.MainActivity.team;
import static com.example.hoopscoach.MainActivity.trainingsRef;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddTrainingActivity extends AppCompatActivity {

    private ConstraintLayout chooseType;
    private RelativeLayout chooseExercises, addPlayers;
    private RadioGroup type_group, specialization_group;
    private EditText training_name;
    private String name, type, specialization;
    private Button to_players,to_groups, to_exercises, to_finish, toExerciseCreate;
    private TextView err, choosingTitle, noExercises;
    private ChipGroup players_group, exercises_group;
    private ListView players_list, exercises_list;
    private ArrayList<String> playerList, groupList, selectedPlayers, selectedGroups, playerEmails, exercisesList, selectedExercises, groupMembersId;
    private ArrayAdapter<String> adapter, adapter1, adapter2;
    private Training training;
    private Map<String, Object> groupMembersMap;
    static final String NEW_GROUP_TITLE = "NEW_GROUP_TITLE";
    static final String NEW_EXERCISE_TITLE = "NEW_EXERCISE_TITLE";

//    private RadioButton personal_training, group_training, team_training, shooting_training, dribbling_training, play_training, physical_training;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);

        training_name = findViewById(R.id.et_training_name);
        type_group = findViewById(R.id.radios);
        addPlayers = findViewById(R.id.add_players_layout);
        specialization_group = findViewById(R.id.radios2);
        chooseType = findViewById(R.id.layout_choose_type);
        to_players = findViewById(R.id.button4);
        to_groups = findViewById(R.id.btn_to_addGroup);
        players_list = findViewById(R.id.team_list);
        players_group = findViewById(R.id.player_chips);
        to_exercises = findViewById(R.id.btn_to_exercises);
        chooseExercises = findViewById(R.id.exercises_layout);
        toExerciseCreate = findViewById(R.id.button5);
        exercises_group = findViewById(R.id.chipGroup);
        to_finish = findViewById(R.id.button7);
        err = findViewById(R.id.tv_no_groups);
        choosingTitle = findViewById(R.id.tv);
        exercises_list = findViewById(R.id.exercises_list);
        noExercises = findViewById(R.id.tv_no_exercises);

        chooseType.setVisibility(View.VISIBLE);
        addPlayers.setVisibility(View.GONE);
        chooseExercises.setVisibility(View.GONE);

        training = new Training();

        playerList = new ArrayList<>();
        groupList = new ArrayList<>();
        selectedPlayers = new ArrayList<>();
        selectedGroups = new ArrayList<>();
        playerEmails = new ArrayList<>();
        selectedExercises = new ArrayList<>();
        exercisesList = new ArrayList<>();
        groupMembersId = new ArrayList<>();
        groupMembersMap = new HashMap<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerList);
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groupList);
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercisesList);

        exercises_list.setAdapter(adapter2);

        type_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.personal_training) {
                    type = "Personal";
                } else if (checkedId == R.id.group_training) {
                    type = "Group";
                } else if (checkedId == R.id.team_training) {
                    type = "Team";
                }
            }
        });

        specialization_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.shooting_training){
                    specialization = "Shooting";
                } else if (checkedId == R.id.dribbling_training) {
                    specialization = "Dribbling";
                }else if(checkedId == R.id.play_training){
                    specialization = "Play";
                } else if (checkedId == R.id.physical_training) {
                    specialization = "Physical";
                }
            }
        });

        to_players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = training_name.getText().toString();
                chooseType.setVisibility(View.GONE);
                addPlayers.setVisibility(View.VISIBLE);

                //Групповая тренировка
                if(type.equals("Group")){
                    to_groups.setVisibility(View.VISIBLE);
                    players_list.setAdapter(adapter1);
                    choosingTitle.setText("Выберите группы игроков");

                    groupsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                players_list.setVisibility(View.VISIBLE);
                                err.setVisibility(View.GONE);
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    groupList.add(doc.getString("name"));
                                }
                                adapter1.notifyDataSetChanged();
                                if(groupList.size() == 0){
                                    players_list.setVisibility(View.GONE);
                                    err.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });

                    players_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            SparseBooleanArray selected = players_list.getCheckedItemPositions();

                            addGroupChip((String) players_list.getItemAtPosition(position));


                            if(selected.size() != 0){
                                to_exercises.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        addPlayers.setVisibility(View.GONE);
                                        chooseExercises.setVisibility(View.VISIBLE);
                                        showExercises();
                                    }
                                });
                            }

                        }
                    });
                }

                //Персональная трениоовка
                else if(type.equals("Personal")){
                    players_list.setVisibility(View.VISIBLE);
                    to_groups.setVisibility(View.GONE);
                    players_list.setAdapter(adapter);
                    choosingTitle.setText("Выберите игроков");

                    playersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot doc: task.getResult()){
                                    String playerName = doc.getString("name") + " " + doc.getString("last_name");
                                    playerList.add(playerName);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });

                    players_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            SparseBooleanArray selected = players_list.getCheckedItemPositions();
                            selectedPlayers.add((String) players_list.getItemAtPosition(position));
                            addPlayerChip((String) players_list.getItemAtPosition(position));
                            playerList.remove(position);
                            adapter.notifyDataSetChanged();

                            if(selected.size() != 0){
                                to_exercises.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        addPlayers.setVisibility(View.GONE);
                                        chooseExercises.setVisibility(View.VISIBLE);
                                        showExercises();
                                        playersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    for(QueryDocumentSnapshot doc: task.getResult()){
                                                        for (int i = 0; i < selectedPlayers.size(); i++) {
                                                            if((doc.getString("name") + " " + doc.getString("last_name")).equals(selectedPlayers.get(i))){
                                                                playerEmails.add(doc.getString("email"));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
                //Командная тренировка
                else if (type.equals("Team")) {
                    chooseType.setVisibility(View.GONE);
                    chooseExercises.setVisibility(View.VISIBLE);

                    playersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot doc: task.getResult()){
                                    selectedPlayers.add(doc.getString("name") + " " + doc.getString("last_name"));
                                    playerEmails.add(doc.getString("email"));
                                }
                            }
                        }
                    });
                    showExercises();
                }


                exercises_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedExercises.add((String) exercises_list.getItemAtPosition(position));
                        addExerciseChip((String) exercises_list.getItemAtPosition(position));
                        exercisesList.remove(position);
                        adapter2.notifyDataSetChanged();

                            //Завершение создания тренировки
                            to_finish.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    training.title = name;
                                    training.type = type;
                                    training.specialization = specialization;
                                    training.exercises = selectedExercises;


                                    Map<String, Object> trainingMap = new HashMap<>();

                                    if (training.type.equals("Personal") || training.type.equals("Team")){
                                        for (String email: playerEmails) {
                                            training.execution.put(email, false);
                                        }
                                        trainingMap.put("type", training.type);
                                        trainingMap.put("specialization", training.specialization);
                                        trainingMap.put("exercises", training.exercises);
                                        trainingMap.put("execution", training.execution);

                                        trainingsRef.document(training.title).set(trainingMap);

                                        team.trainings.add(training);

                                        playersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                for (QueryDocumentSnapshot doc: task.getResult()) {
                                                    for (int i = 0; i < playerEmails.size(); i++) {
                                                        if(doc.getString("email").equals(playerEmails.get(i))){
                                                            playersRef.document(playerEmails.get(i)).update("current_trainings", FieldValue.arrayUnion(training.title));
                                                            playersRef.document(playerEmails.get(i)).update("assigned_trainings", FieldValue.increment(1));
                                                        }
                                                    }
                                                }
                                            }
                                        });

                                    } else if (training.type.equals("Group")) {
                                        for(String group: selectedGroups){
                                            Map<String, Object> groupExecution = new HashMap<>();
                                            ArrayList<String> members = new ArrayList<>();
                                            groupsRef.document(group).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    DocumentSnapshot ds = task.getResult();
                                                    members.addAll((ArrayList<String>) ds.get("membersId"));
                                                    for (String member: members) {
                                                        groupExecution.put(member, false);
                                                        playersRef.document(member).update("current_trainings", FieldValue.arrayUnion(training.title));
                                                        playersRef.document(member).update("assigned_trainings", FieldValue.increment(1));
                                                    }
                                                    training.execution.put(group, groupExecution);

                                                    trainingMap.put("type", training.type);
                                                    trainingMap.put("specialization", training.specialization);
                                                    trainingMap.put("exercises", training.exercises);
                                                    trainingMap.put("execution", training.execution);



                                                    trainingsRef.document(training.title).set(trainingMap);

                                                    team.trainings.add(training);
                                                }
                                            });
                                        }
                                    }
                                    finish();
                                }
                            });
                        }
                });

            }
        });


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {

                if(o.getResultCode() == Activity.RESULT_OK){
                    Intent i = new Intent(o.getData());
                    String newGroupTitle = i.getStringExtra(NEW_GROUP_TITLE);
                    groupList.add(newGroupTitle);
                    adapter1.notifyDataSetChanged();
                    err.setVisibility(View.GONE);
                    players_list.setVisibility(View.VISIBLE);
                }

            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher1 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {

                if(o.getResultCode() == Activity.RESULT_OK){
                    Intent i = new Intent(o.getData());
                    String newExerciseTitle = i.getStringExtra(NEW_EXERCISE_TITLE);
                    exercisesList.add(newExerciseTitle);
                    adapter2.notifyDataSetChanged();
                    noExercises.setVisibility(View.GONE);
                    exercises_list.setVisibility(View.VISIBLE);
                }

            }
        });


        to_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddTrainingActivity.this, CreateGroupActivity.class);
                activityResultLauncher.launch(i);
            }
        });

        toExerciseCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddTrainingActivity.this, CreateExercisesActivity.class);
                i.putExtra("specialization", specialization);
                activityResultLauncher1.launch(i);
            }
        });

    }

    private void addPlayerChip(String title){
        LayoutInflater inflater = LayoutInflater.from(this);

        Chip newChip = (Chip) inflater.inflate(R.layout.layout_chip_entry, this.players_group, false);

        newChip.setText(title);

        this.players_group.addView(newChip);

        newChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlayerChip((Chip) v);
            }
        });
    }

    private void deletePlayerChip(Chip chip){
        ChipGroup parent = (ChipGroup) chip.getParent();
        parent.removeView(chip);
        this.playerList.add(chip.getText().toString());
        this.adapter.notifyDataSetChanged();
//        training.exercises.remove(new Exercise(chip.getText().toString()));
    }

    private void addGroupChip(String title){
        LayoutInflater inflater = LayoutInflater.from(this);

        Chip newChip = (Chip) inflater.inflate(R.layout.layout_chip_entry, this.players_group, false);

        newChip.setText(title);

        groupsRef.document(title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                ArrayList<String> members = (ArrayList<String>) ds.get("membersId");
                boolean isSelected = false;
                for (String member: members) {
                    if(isSelected) break;
                    for (String selected: groupMembersId) {
                        if(member.equals(selected)){
                            isSelected = true;
                            Toast.makeText(AddTrainingActivity.this, "Игрок(и) из этой группы уже задействованы в тренировке", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
                if(!isSelected){
                    groupList.remove(title);
                    adapter1.notifyDataSetChanged();
                    selectedGroups.add(title);
                    groupMembersId.addAll(members);
                    players_group.addView(newChip);
                }
            }
        });

        newChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGroupChip((Chip) v);
            }
        });
    }

    private void  deleteGroupChip(Chip chip){
        ChipGroup parent = (ChipGroup) chip.getParent();
        parent.removeView(chip);
        groupsRef.document(chip.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                for (String member: (ArrayList<String>) ds.get("members")) {
                    groupMembersId.remove(member);
                }
            }
        });
        this.groupList.add(chip.getText().toString());
        this.adapter1.notifyDataSetChanged();
    }

    private void addExerciseChip(String title){
        LayoutInflater inflater = LayoutInflater.from(this);

        Chip newChip = (Chip) inflater.inflate(R.layout.layout_chip_entry, this.exercises_group, false);

        newChip.setText(title);

        this.exercises_group.addView(newChip);
        newChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteExerciseChip((Chip) v);
            }
        });
    }

    private void deleteExerciseChip(Chip chip){
        ChipGroup parent = (ChipGroup) chip.getParent();
        parent.removeView(chip);
        this.exercisesList.add(chip.getText().toString());
        this.adapter2.notifyDataSetChanged();
        this.selectedExercises.remove(chip.getText().toString());
    }

    private void showExercises(){
        exercisesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc: task.getResult()){
                        if(doc.getString("training_type").equals(specialization)){
                            exercisesList.add(doc.getId());
                        }
                    }
                    adapter2.notifyDataSetChanged();
                    if(exercisesList.size() == 0){
                        noExercises.setVisibility(View.VISIBLE);
                        exercises_list.setVisibility(View.GONE);
                    } else {
                        noExercises.setVisibility(View.GONE);
                        exercises_list.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

}
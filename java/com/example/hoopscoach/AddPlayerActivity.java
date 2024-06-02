package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.playerRef;
import static com.example.hoopscoach.MainActivity.playersRef;
import static com.example.hoopscoach.MainActivity.team;

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
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddPlayerActivity extends AppCompatActivity {

    private EditText player_id;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        player_id = findViewById(R.id.et_playerId);
        add = findViewById(R.id.btn_addPlayer);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty((player_id.getText().toString()))){
                    playerRef.document(player_id.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot ds = task.getResult();
                                if(ds.exists()){
                                    if (ds.getString("teamName").equals("None")){
                                        team.players.add(new Player(ds.getString("id"), ds.getString("name"), ds.getString("last_name"), ds.getString("email"), team.teamName));
                                        playerRef.document(player_id.getText().toString()).update("teamName", team.teamName);
                                        Map<String, Object> playerMap = new HashMap<>();
                                        playerMap.put("id", ds.getString("id"));
                                        playerMap.put("name", ds.getString("name"));
                                        playerMap.put("last_name", ds.getString("last_name"));
                                        playerMap.put("email", ds.getString("email"));
                                        playerMap.put("assigned_trainings", 0);
                                        playerMap.put("completed_trainings", 0);
                                        playerMap.put("completed_shooting_trainings", 0);
                                        playerMap.put("completed_dribbling_trainings", 0);
                                        playerMap.put("completed_physical_trainings", 0);
                                        playerMap.put("completed_play_trainings", 0);
                                        playersRef.document(player_id.getText().toString()).set(playerMap);
                                        finish();
                                    }else Toast.makeText(AddPlayerActivity.this, "This user is already has a team", Toast.LENGTH_SHORT).show();
                                }else Toast.makeText(AddPlayerActivity.this, "No such player", Toast.LENGTH_SHORT).show();
                            }else Toast.makeText(AddPlayerActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else Toast.makeText(AddPlayerActivity.this, "Введите данные", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
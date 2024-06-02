package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.groupsRef;
import static com.example.hoopscoach.MainActivity.playersRef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateGroupActivity extends AppCompatActivity {

    private EditText groupName;
    private PlayersGroup group;
    private ListView playerList;
    private ChipGroup selectedGroup;
    private Button add;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> players, selectedPlayers, selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);


        groupName = findViewById(R.id.et_groupName);
        playerList = findViewById(R.id.playerForGroupsList);
        selectedGroup = findViewById(R.id.selectedPlayersGroup);
        add = findViewById(R.id.btn_addGroup);


        players = new ArrayList<>();
        selectedId = new ArrayList<>();
        selectedPlayers = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, players);

        playerList.setAdapter(adapter);

        playersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc: task.getResult()){
                        players.add(doc.getString("name") + " " + doc.getString("last_name"));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray selected = playerList.getCheckedItemPositions();

                selectedPlayers.add((String) playerList.getItemAtPosition(position));
                addChip((String) playerList.getItemAtPosition(position));
                players.remove(position);
                adapter.notifyDataSetChanged();

                if(selected.size() != 0 && !TextUtils.isEmpty(groupName.getText().toString())){
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Object> groupMap = new HashMap<>();

                            group = new PlayersGroup(groupName.getText().toString());

                            group.membersNames = selectedPlayers;
                            playersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot doc: task.getResult()){
                                            for (int i = 0; i < selectedPlayers.size(); i++) {
                                                if((doc.getString("name") + " " + doc.getString("last_name")).equals(selectedPlayers.get(i))){
                                                   group.membersId.add(doc.getString("email"));
                                                }
                                            }
                                        }
                                        groupMap.put("name", group.name);
                                        groupMap.put("members", group.membersNames);
                                        groupMap.put("membersId", group.membersId);
                                        groupsRef.document(group.name).set(groupMap);
                                    }
                                }
                            });

                            Intent i = new Intent();
                            i.putExtra(AddTrainingActivity.NEW_GROUP_TITLE, group.name);
                            setResult(RESULT_OK, i);
                            finish();
                        }
                    });
                }
            }
        });


    }

    private void addChip(String title){
        LayoutInflater inflater =  LayoutInflater.from(this);

        Chip newChip = (Chip) inflater.inflate(R.layout.layout_chip_entry, this.selectedGroup, false);

        newChip.setText(title);

        this.selectedGroup.addView(newChip);

        newChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteChip((Chip) v);
            }
        });

    }

    private void deleteChip(Chip chip){
        ChipGroup parent = (ChipGroup) chip.getParent();
        parent.removeView(chip);
        this.players.add(chip.getText().toString());
        this.selectedPlayers.remove(chip.getText().toString());
        this.adapter.notifyDataSetChanged();
    }

}
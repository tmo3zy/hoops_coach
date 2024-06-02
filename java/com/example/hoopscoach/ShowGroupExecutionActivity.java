package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.groupsRef;
import static com.example.hoopscoach.MainActivity.playersRef;
import static com.example.hoopscoach.MainActivity.trainingsRef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowGroupExecutionActivity extends AppCompatActivity {

    private TextView title;
    private RecyclerView dataView;
    private Button back;
    private TrainingDataAdapter adapter;
    private ArrayList<PlayerData> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_group_execution);
        Bundle extras = getIntent().getExtras();

        title = findViewById(R.id.tv_showingGroup);
        dataView = findViewById(R.id.groupDataView);
        back = findViewById(R.id.btn_backToGroups);

        adapter = new TrainingDataAdapter(this, data);
        dataView.setAdapter(adapter);

        String groupName = extras.getString("groupName");
        String trainingTitle = extras.getString("trainingName");

        title.setText("Группа: " + groupName);


        trainingsRef.document(trainingTitle).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                Map<String, Object> groupsMap = (HashMap) doc.get("execution");
                Map<String, Object> playersMap = (HashMap) groupsMap.get(groupName);

                for(Map.Entry<String, Object> entry: playersMap.entrySet()){
                    PlayerData playerData = new PlayerData();
                    String id = entry.getKey();
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

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.trainingsRef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class ShowExercisesActivity extends AppCompatActivity {

    private TextView title;
    private Button back;
    private ListView exercisesList;
    private ArrayList<String> exercises;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exercises);
        Bundle extras = getIntent().getExtras();
        String trainingTitle = extras.getString("trainingTitle");

        title = findViewById(R.id.tv_trainingTitle);
        back = findViewById(R.id.btn_backToTraining2);
        exercisesList = findViewById(R.id.trainingExercisesList);

        title.setText(trainingTitle);

        exercises = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercises);
        exercisesList.setAdapter(adapter);

        trainingsRef.document(trainingTitle).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                exercises.addAll((ArrayList<String>) ds.get("exercises"));
                adapter.notifyDataSetChanged();
            }
        });

        exercisesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ShowExercisesActivity.this, ShowExerciseDataActivity.class);
                i.putExtra("title", (String) exercisesList.getItemAtPosition(position));
                startActivity(i);
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
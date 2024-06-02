package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.exercisesRef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class ShowExerciseDataActivity extends AppCompatActivity {

    private TextView title, description;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exercise_data);
        Bundle extras = getIntent().getExtras();
        String exerciseTitle = extras.getString("title");

        title = findViewById(R.id.tv_exerciseTitle);
        description = findViewById(R.id.tv_exerciseDescription);
        back = findViewById(R.id.btn_backToTraining);

        title.setText(exerciseTitle);

        exercisesRef.document(exerciseTitle).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                String exerciseDescription = ds.getString("description");
                description.setText(exerciseDescription);
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
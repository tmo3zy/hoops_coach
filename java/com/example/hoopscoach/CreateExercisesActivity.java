package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.exercisesRef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class CreateExercisesActivity extends AppCompatActivity {

    private EditText title, description;
    private Button addExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercises);
        Bundle extras = getIntent().getExtras();

        title = findViewById(R.id.et_exerciseTitle);
        description = findViewById(R.id.et_exerciseDescription);
        addExercise = findViewById(R.id.btn_createExercise);

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString())){

                    Map<String, Object> exerciseMap = new HashMap<>();
                    exerciseMap.put("description", description.getText().toString());
                    exerciseMap.put("training_type", extras.getString("specialization"));
                    exercisesRef.document(title.getText().toString()).set(exerciseMap);

                    Intent i = new Intent();
                    i.putExtra(AddTrainingActivity.NEW_EXERCISE_TITLE, title.getText().toString());
                    setResult(RESULT_OK, i);
                    finish();

                }

            }
        });


    }
}
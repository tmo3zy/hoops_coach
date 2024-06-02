package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.playerRef;
import static com.example.hoopscoach.MainActivity.playersRef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class ShowStatsActivity extends AppCompatActivity {

    private TextView playerName, playerName2, shooting, dribbling, physical, playing, total;
    private double completedShooting, completedDribbling, completedPhysical, completedPlaying, totalCompleted;
    private RelativeLayout statsLayout;
    private ConstraintLayout noDataLayout;
    private PieChart pieChart;
    private CardView cardView;
    private Button back1, back2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_stats);
        Bundle extras = getIntent().getExtras();

        String name = extras.getString("playerName");


        playerName = findViewById(R.id.tv_name);
        shooting = findViewById(R.id.tv_shooting);
        dribbling = findViewById(R.id.tv_dribbling);
        physical = findViewById(R.id.tv_physical);
        playing = findViewById(R.id.tv_playing);
        total = findViewById(R.id.tv_total);
        statsLayout = findViewById(R.id.stats_layout);
        pieChart = findViewById(R.id.piechart);
        cardView = findViewById(R.id.cardViewGraph);
        back1 = findViewById(R.id.btn_backToPlayers);
        back2 = findViewById(R.id.btn_backToPlayers2);
        noDataLayout = findViewById(R.id.noDataLayout);
        playerName2 = findViewById(R.id.tv_name2);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        back1.setOnClickListener(listener);
        back2.setOnClickListener(listener);
        playerName.setText(name);
        playerName2.setText(name);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("playerId");
        playersRef.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                if((long) ds.get("assigned_trainings") != 0){
                    statsLayout.setVisibility(View.VISIBLE);
                    noDataLayout.setVisibility(View.GONE);
                    completedShooting =  ds.getDouble("completed_shooting_trainings");
                    completedDribbling =  ds.getDouble("completed_dribbling_trainings");
                    completedPhysical =  ds.getDouble("completed_physical_trainings");
                    completedPlaying =  ds.getDouble("completed_play_trainings");
                    totalCompleted =  ds.getDouble("completed_trainings");
                    if(totalCompleted == 0){
                        cardView.setVisibility(View.GONE);
                    }else{
                        cardView.setVisibility(View.VISIBLE);
                        completedShooting = Math.round(completedShooting / totalCompleted * 1000.0) / 10.0;
                        completedDribbling = Math.round(completedDribbling / totalCompleted * 1000.0) / 10.0;
                        completedPhysical = Math.round(completedPhysical / totalCompleted * 1000.0) / 10.0;
                        completedPlaying = Math.round(completedPlaying / totalCompleted * 1000.0) / 10.0;
                        totalCompleted = Math.round(totalCompleted / ds.getDouble("assigned_trainings") * 1000.0) / 10.0;
                        pieChart.addPieSlice(new PieModel("Shooting", (float) completedShooting, Color.parseColor("#FFA726")));
                        pieChart.addPieSlice(new PieModel("Dribbling", (float) completedDribbling, Color.parseColor("#66BB6A")));
                        pieChart.addPieSlice(new PieModel("Physical", (float) completedPhysical, Color.parseColor("#EF5350")));
                        pieChart.addPieSlice(new PieModel("Playing", (float) completedPlaying, Color.parseColor("#29B6F6")));
                        pieChart.startAnimation();
                    }
                    shooting.setText(String.valueOf(completedShooting));
                    dribbling.setText(String.valueOf(completedDribbling));
                    physical.setText(String.valueOf(completedPhysical));
                    playing.setText(String.valueOf(completedPlaying));
                    total.setText(String.valueOf(totalCompleted));
                }else{
                    statsLayout.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
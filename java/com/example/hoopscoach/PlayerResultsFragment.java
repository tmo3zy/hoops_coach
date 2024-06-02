package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.playersRef;
import static com.example.hoopscoach.MainActivity.team;
import static com.example.hoopscoach.MainActivity.user;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerResultsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PieChart pieChart;
    private TextView shooting,dribbling, physical, playing, total;
    private double completedShooting, completedDribbling, completedPhysical, completedPlaying, totalCompleted;
    private CardView cardView;
    private ConstraintLayout noDataLayout;
    private RelativeLayout statsLayout;

    public PlayerResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayerResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerResultsFragment newInstance(String param1, String param2) {
        PlayerResultsFragment fragment = new PlayerResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        shooting = (TextView) getActivity().findViewById(R.id.tv_shooting1);
        dribbling = (TextView) getActivity().findViewById(R.id.tv_dribbling1);
        physical = (TextView) getActivity().findViewById(R.id.tv_physical1);
        playing = (TextView) getActivity().findViewById(R.id.tv_playing1);
        total = (TextView) getActivity().findViewById(R.id.tv_total1);
        statsLayout = (RelativeLayout) getActivity().findViewById(R.id.stats_layout1);
        pieChart = (PieChart) getActivity().findViewById(R.id.pieChart1);
        cardView = (CardView) getActivity().findViewById(R.id.cardViewGraph1);
        noDataLayout = (ConstraintLayout) getActivity().findViewById(R.id.noResultsLayout);

        if(team != null){
            playersRef.document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
        }else{
            statsLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_results, container, false);
    }
}
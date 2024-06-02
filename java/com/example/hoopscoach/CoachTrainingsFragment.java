package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.coachRef;
import static com.example.hoopscoach.MainActivity.team;
import static com.example.hoopscoach.MainActivity.teamRef;
import static com.example.hoopscoach.MainActivity.trainingsRef;
import static com.example.hoopscoach.MainActivity.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoachTrainingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoachTrainingsFragment extends Fragment {

    private GridView gv_trainings;
    private ImageButton add_trainings, add_trainings2;
    private TextView textView;
    private ConstraintLayout noTeam, noTrainings;
    private RelativeLayout trainingsLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<String> trainings_list;
    private ArrayAdapter<String> adapter;

    public CoachTrainingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoachTrainingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoachTrainingsFragment newInstance(String param1, String param2) {
        CoachTrainingsFragment fragment = new CoachTrainingsFragment();
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

        gv_trainings = (GridView)getActivity().findViewById(R.id.gridView1);
        add_trainings = (ImageButton)getActivity().findViewById(R.id.btn_addTraining);
        add_trainings2 = (ImageButton)getActivity().findViewById(R.id.btn_addTraining2);
        textView = (TextView)getActivity().findViewById(R.id.tv_trainings_info);
        noTeam = (ConstraintLayout)getActivity().findViewById(R.id.layout_no_team);
        noTrainings = (ConstraintLayout)getActivity().findViewById(R.id.layout_no_trainings);
        trainingsLayout = (RelativeLayout)getActivity().findViewById(R.id.trainings_layout);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddTrainingActivity.class);
                startActivity(i);
            }
        };

        coachRef.document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    if(ds.exists()){
                        if(ds.getString("teamName").equals("None")){
                            noTeam.setVisibility(View.VISIBLE);
                            noTrainings.setVisibility(View.GONE);
                            trainingsLayout.setVisibility(View.GONE);

                        } else if (!ds.getString("teamName").equals("None") && team.players.size() == 0) {
                            textView.setText("Добавьте игроков, чтобы назначать тренировки");

                        } else if (!ds.getString("teamName").equals("None") && team.players.size() != 0 && team.trainings.size() == 0) {
                            noTrainings.setVisibility(View.VISIBLE);
                            noTeam.setVisibility(View.GONE);
                            trainingsLayout.setVisibility(View.GONE);

                            add_trainings2.setOnClickListener(listener);

                        } else if (!ds.getString("teamName").equals("None") && team.players.size() != 0 && team.trainings.size() != 0) {
                            trainingsLayout.setVisibility(View.VISIBLE);
                            noTeam.setVisibility(View.GONE);
                            noTrainings.setVisibility(View.GONE);

                            add_trainings.setOnClickListener(listener);

                            trainings_list = new ArrayList<>();
                            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, trainings_list);
                            gv_trainings.setAdapter(adapter);

                            trainingsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for (QueryDocumentSnapshot doc: task.getResult()){
                                            trainings_list.add(doc.getId());
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        gv_trainings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ShowTrainingDataActivity.class);
                i.putExtra("COACH_TRAINING_TITLE", (String) gv_trainings.getItemAtPosition(position));
                startActivity(i);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coach_trainings, container, false);
    }
}
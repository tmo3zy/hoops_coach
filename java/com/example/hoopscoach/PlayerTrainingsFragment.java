package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.playersRef;
import static com.example.hoopscoach.MainActivity.team;
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
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerTrainingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerTrainingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ConstraintLayout noDataLayout;
    private RelativeLayout trainingsLayout;
    private ArrayList<String> trainings;
    private GridView gvTrainings;
    private ArrayAdapter<String> adapter;

    public PlayerTrainingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayerTrainingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerTrainingsFragment newInstance(String param1, String param2) {
        PlayerTrainingsFragment fragment = new PlayerTrainingsFragment();
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
        noDataLayout = (ConstraintLayout) getActivity().findViewById(R.id.playerNoTeamLayout);
        trainingsLayout = (RelativeLayout) getActivity().findViewById(R.id.playerTrainings_layout);
        gvTrainings = (GridView) getActivity().findViewById(R.id.gridView2);
        trainings = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, trainings);

        if(team == null){
            noDataLayout.setVisibility(View.VISIBLE);
            trainingsLayout.setVisibility(View.GONE);
        }else{
            playersRef.document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot ds = task.getResult();
                    if(ds.get("current_trainings") != null){
                        trainings = (ArrayList<String>) ds.get("current_trainings");
                        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, trainings);
                        noDataLayout.setVisibility(View.GONE);
                        trainingsLayout.setVisibility(View.VISIBLE);
                        gvTrainings.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        gvTrainings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent i = new Intent(getActivity(), ShowTrainingDataActivity.class);
                                i.putExtra("playerTrainingTitle", (String) gvTrainings.getItemAtPosition(position));
                                startActivity(i);
                            }
                        });
                    }else{
                        noDataLayout.setVisibility(View.VISIBLE);
                        trainingsLayout.setVisibility(View.GONE);
                    }
                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_trainings, container, false);
    }
}
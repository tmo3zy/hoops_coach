package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.coachRef;
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
 * Use the {@link CoachTeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoachTeamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView teamList;
    private ConstraintLayout layout, layout2;
    private RelativeLayout layout3;
    private ImageButton create, add_player, add_player2;
    private TextView players_msg;
    private ArrayAdapter<String> adapter;
    private List<String> playerList;
    private List<Player> listTemp;

    public CoachTeamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoachTeamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoachTeamFragment newInstance(String param1, String param2) {
        CoachTeamFragment fragment = new CoachTeamFragment();
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
        layout = (ConstraintLayout)getActivity().findViewById(R.id.notHaveTeam);
        teamList = (ListView)getActivity().findViewById(R.id.teamlist);
        create = (ImageButton)getActivity().findViewById(R.id.imageButton);
        add_player = (ImageButton)getActivity().findViewById(R.id.imageButton2);
        add_player2 = (ImageButton)getActivity().findViewById(R.id.imageButton1);
        layout2 = (ConstraintLayout)getActivity().findViewById(R.id.notHavePlayers);
        layout3 = (RelativeLayout)getActivity().findViewById(R.id.has_team_layout);
        players_msg = (TextView)getActivity().findViewById(R.id.textView2);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateActivity.class);
                startActivity(i);
            }
        });

        add_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddPlayerActivity.class);
                startActivity(i);
            }
        });

        add_player2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddPlayerActivity.class);
                startActivity(i);
            }
        });

        coachRef.document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    if(ds.exists() && ds.getString("teamName").equals("None")){
                        layout3.setVisibility(View.GONE);
                        layout2.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                    } else if (ds.exists() && ! ds.getString("teamName").equals("None") && team.players.size() == 0) {
                        layout3.setVisibility(View.GONE);
                        layout.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        players_msg.setText("В команде " + team.teamName + " еще нет игроков");
                    } else if (ds.exists() && ! ds.getString("teamName").equals("None") && team.players.size() != 0) {
                        layout3.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.GONE);
                        layout2.setVisibility(View.GONE);
                        playerList = new ArrayList<>();
                        listTemp = new ArrayList<>();
                        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, playerList);
                        teamList.setAdapter(adapter);
                        playersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot doc: task.getResult()) {
                                        playerList.add(doc.getString("name") + " " + doc.getString("last_name"));
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });

                        teamList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent i = new Intent(getActivity(), ShowStatsActivity.class);
                                i.putExtra("playerName", (String) teamList.getItemAtPosition(position));
                                playersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(QueryDocumentSnapshot doc: task.getResult()){
                                            if((doc.getString("name") + " " + doc.getString("last_name")).equals(teamList.getItemAtPosition(position))){
                                                i.putExtra("playerId", doc.getString("email"));
                                            }
                                        }
                                        startActivity(i);
                                    }
                                });
                            }
                        });

                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coach_team, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
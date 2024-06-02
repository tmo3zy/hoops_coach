package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.user;
import static com.example.hoopscoach.MainActivity.userRef;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.widget.TextView;

import com.example.hoopscoach.databinding.ActivityCoachNavBinding;
import com.example.hoopscoach.databinding.FragmentCoachProfileBinding;

public class CoachNavActivity extends AppCompatActivity {
    
    ActivityCoachNavBinding binding;
    public CoachTeamFragment teamFragment = new CoachTeamFragment();
    public CoachProfileFragment profileFragment = new CoachProfileFragment();
    public CoachTrainingsFragment trainingsFragment = new CoachTrainingsFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoachNavBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        
        binding.coachBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.team){
                replaceFragment(teamFragment);
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(profileFragment);
            } else if (item.getItemId() == R.id.trainings) {
                replaceFragment(trainingsFragment);
            }
            return true;
        });
        binding.coachBottomNavigationView.setSelectedItemId(R.id.team);
    }
    
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction .commit();
    }
}
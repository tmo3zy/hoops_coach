package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

import com.example.hoopscoach.databinding.ActivityPlayerNavBinding;

public class PlayerNavActivity extends AppCompatActivity {
    ActivityPlayerNavBinding binding;
    public PlayerProfileFragment profileFragment = new PlayerProfileFragment();
    public PlayerTrainingsFragment trainingsFragment = new PlayerTrainingsFragment();
    public PlayerResultsFragment resultsFragment = new PlayerResultsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.playerBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.profile2){
                replaceFragment(profileFragment);
            }else if(item.getItemId() == R.id.trainings2){
                replaceFragment(trainingsFragment);
            }else if(item.getItemId() == R.id.results2){
                replaceFragment(resultsFragment);
            }
            return true;
        });
        binding.playerBottomNavigationView.setSelectedItemId(R.id.trainings2);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout2, fragment);
        fragmentTransaction .commit();
    }
}
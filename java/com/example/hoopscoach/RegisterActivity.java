package com.example.hoopscoach;

import static com.example.hoopscoach.MainActivity.auth;
import static com.example.hoopscoach.MainActivity.coachRef;
import static com.example.hoopscoach.MainActivity.playerRef;
import static com.example.hoopscoach.MainActivity.userRef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    ConstraintLayout constraintLayout;


    EditText name, last_name, email, password;
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        relativeLayout.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.VISIBLE);
    }

    private void init(){
        name = findViewById(R.id.name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.signup);
        relativeLayout = findViewById(R.id.rellayot);
        constraintLayout = findViewById(R.id.containermin);
    }

    public void onClickSignUp(View view) {
        if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
            auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
//                        sendEmailVer();
                        Toast.makeText(RegisterActivity.this, "User SignUp Successeful", Toast.LENGTH_SHORT).show();
                        relativeLayout.setVisibility(View.VISIBLE);
                        constraintLayout.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(this, "Enter Email and password", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickback2(View view) {
        finish();
    }

    public void onClickAddPlayer(View view) {
        String id = auth.getCurrentUser().getUid();
        String name1 = name.getText().toString();
        String lastname1 = last_name.getText().toString();
        String email1 = email.getText().toString();
        String role = "Player";
        String teamName = "None";
        User user = new User(id, name1, lastname1, email1, role, teamName);
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.id);
        userMap.put("name", user.name);
        userMap.put("last_name", user.last_name);
        userMap.put("email", user.email);
        Player player = new Player(id, name1, lastname1, email1, teamName);
        Map<String, Object> playerMap = new HashMap<>();
        playerMap.put("id", player.id);
        playerMap.put("name", player.name);
        playerMap.put("last_name", player.last_name);
        playerMap.put("email", player.email);
        playerMap.put("teamName", player.teamName);
        userRef.document(user.email).set(userMap);
        playerRef.document(player.email).set(playerMap);
        finish();
    }


    public void onClickAddCoach(View view) {
        String id = auth.getCurrentUser().getUid();
        String name1 = name.getText().toString();
        String lastname1 = last_name.getText().toString();
        String email1 = email.getText().toString();
        String role = "Coach";
        String teamName = "None";
        User user = new User(id, name1, lastname1, email1, role, teamName);
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.id);
        userMap.put("name", user.name);
        userMap.put("last_name", user.last_name);
        userMap.put("email", user.email);
        Coach coach = new Coach(id, name1, lastname1, email1, teamName);
        Map<String, Object> coachMap = new HashMap<>();
        coachMap.put("id", coach.id);
        coachMap.put("name", coach.name);
        coachMap.put("last_name", coach.last_name);
        coachMap.put("email", coach.email);
        coachMap.put("teamName", coach.teamName);
        userRef.document(user.email).set(userMap);
        coachRef.document(coach.email).set(coachMap);
        finish();
    }



//    private void sendEmailVer(){
//        FirebaseUser user = auth.getCurrentUser();
//        assert user != null;
//        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(RegisterActivity.this, "Check your emai;", Toast.LENGTH_SHORT).show();
//                }else Toast.makeText(RegisterActivity.this, "Task failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}
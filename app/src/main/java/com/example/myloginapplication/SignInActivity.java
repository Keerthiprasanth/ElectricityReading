package com.example.myloginapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myloginapplication.Model.Member;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends AppCompatActivity {
    Member member;
    private static final List<Member> membersList = new ArrayList<>();
    String name,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);

        name = username.getText().toString();
        this.member = new Member();
        member.setUsername("Raj");
        membersList.add(member);
        Toast.makeText(SignInActivity.this,"Welcome "+name ,Toast.LENGTH_LONG).show();
        member.setPassword("admin");



        Button signinbtn = findViewById(R.id.signinbtn);

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Member member: membersList) {
                    if (username.getText().toString().equals(member.getUsername()) && password.getText().toString().equals(member.getPassword())) {
                        Toast.makeText(SignInActivity.this, "Welcome to Break Up! "+member.getUsername(), Toast.LENGTH_LONG).show();
                        openhome();
                    } else {
                        Toast.makeText(SignInActivity.this, "Enter valid credentials", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

//        signinbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {

//                name = username.getText().toString();
//                if(username.getText().toString().equals("ram") && password.getText().toString().equals("ram")){
//                    Toast.makeText(MainActivity.this,"Welcome "+name ,Toast.LENGTH_LONG).show();
//                }
//                else{
//                    Toast.makeText(MainActivity.this,"Bye "+username.getText().toString(),Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }
    public void openhome(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
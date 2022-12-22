package com.example.myloginapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myloginapplication.Model.Member;

public class MainActivity extends AppCompatActivity {
    Member member;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        member.setUsername(String.valueOf(R.id.username));

        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);

        Button signinbtn = findViewById(R.id.signinbtn);

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    Toast.makeText(MainActivity.this,"Welcome to Break Up!",Toast.LENGTH_LONG).show();
                }else if(!username.getText().toString().equals("admin"))
                {
                    Toast.makeText(MainActivity.this,"Enter valid username",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Enter valid credentials",Toast.LENGTH_LONG).show();
                }
            }
        });

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = username.getText().toString();
                if(username.getText().toString().equals("ram") && password.getText().toString().equals("ram")){
                    Toast.makeText(MainActivity.this,"Welcome "+name ,Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Bye "+username.getText().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
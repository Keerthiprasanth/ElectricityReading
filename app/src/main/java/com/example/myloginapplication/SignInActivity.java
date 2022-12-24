package com.example.myloginapplication;

import static com.example.myloginapplication.RegisterActivity.memberList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myloginapplication.Model.Member;

public class SignInActivity extends AppCompatActivity {
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        TextView email = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);
        TextView btn = findViewById(R.id.textnoacc);

        Button signinbtn = findViewById(R.id.loginbtn);

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                name = email.getText().toString();
//                pswrd = password.getText().toString();
                Member member1 = new Member();
                for (Member member: memberList) {
                    if (email.getText().toString().equals(member.getEmailId()) && password.getText().toString().equals(member.getPassword())) {
                        flag=1;
                        member1 = member;
                    }
                }
                if(flag==1){
                    Toast.makeText(SignInActivity.this, "Welcome to Break Up! "+member1.getUsername(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(SignInActivity.this, "Enter valid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openregister();
            }
        });
    }

    public void openregister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
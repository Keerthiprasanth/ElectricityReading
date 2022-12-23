package com.example.myloginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myloginapplication.Model.Member;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    Member member;
    static final List<Member> memberList = new ArrayList<>();
    String name,email,password,confirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView registrationname = findViewById(R.id.registrationname);
        TextView registermail = findViewById(R.id.registermail);
        TextView registerpassword = findViewById(R.id.registerpassword);
        TextView registerconfirmpassword = findViewById(R.id.registerconfirmpassword);

        Button registerbtn = findViewById(R.id.registerbtn);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = registrationname.getText().toString();
                email = registermail.getText().toString();
                password = registerpassword.getText().toString();
                confirmpassword = registerconfirmpassword.getText().toString();
                Member member = new Member();
                member.setUsername(name);
                memberList.add(member);
                member.setPassword(password);
                member.setEmailId(email);
                member.setConfirmpassword(confirmpassword);
                Toast.makeText(RegisterActivity.this, "Welcome "+ member.getUsername()+" you're a registered user!", Toast.LENGTH_LONG).show();
                opensignin();
            }
        });
    }
    public void opensignin(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}

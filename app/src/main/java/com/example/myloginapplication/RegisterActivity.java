package com.example.myloginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myloginapplication.Model.Member;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    Member member;
    private static final List<Member> memberList = new ArrayList<>();
    String name,username,password,confirmpassword;
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
                opensignin();
            }
        });
    }
    public void opensignin(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}

package com.example.myloginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myloginapplication.Model.Member;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    Member member;
    static final List<Member> memberList = new ArrayList<>();
    String name,email,password,address,type,rooms,evc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView registerName = findViewById(R.id.registrationname);
        TextView registerMail = findViewById(R.id.registermail);
        TextView registerPassword = findViewById(R.id.registerpassword);
        TextView registerAddress = findViewById(R.id.registeraddress);
        TextView registernoofrooms = findViewById(R.id.registernoofrooms);
        TextView registerevc = findViewById(R.id.registerevc);
        TextView btn = findViewById(R.id.texthaveacc);

        Spinner propertyType = findViewById(R.id.propertytype);
//        AutoCompleteTextView spinType = findViewById(R.id.spin);

        String[] propertyTypes = getResources().getStringArray(R.array.property_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.property_file, propertyTypes);
        adapter.setDropDownViewResource(R.layout.property_file);
        propertyType.setAdapter(adapter);

//        ArrayAdapter adapterSpin = new ArrayAdapter(this, android.R.layout.simple_spinner_item, propertyTypes);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinType.setAdapter(adapterSpin);

        propertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(RegisterActivity.this, "Select your property type", Toast.LENGTH_SHORT).show();
            }
        });
        Button registerbtn = findViewById(R.id.registerbtn);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = registerName.getText().toString();
                email = registerMail.getText().toString();
                password = registerPassword.getText().toString();
                address = registerAddress.getText().toString();
                rooms = registernoofrooms.getText().toString();
                evc = registerevc.getText().toString();
                Member member = new Member();
                member.setUsername(name);
                memberList.add(member);
                member.setPassword(password);
                member.setEmailId(email);
                member.setAddress(address);
                member.setPropertytype(type);
                member.setEvc(evc);
                member.setNoofrooms(rooms);
                Toast.makeText(RegisterActivity.this, "Welcome "+ member.getUsername()
                        +member.getAddress()+member.getEvc()+member.getPropertytype()+member.getNoofrooms()
                        +member.getEmailId(), Toast.LENGTH_LONG).show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
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

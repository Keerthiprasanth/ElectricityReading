package com.example.myloginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myloginapplication.Model.Member;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    Member member;
    static final List<Member> memberList = new ArrayList<>();
    String name, email, password, address, type, evc;
    int rooms;

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
        TextView scanner = findViewById(R.id.scanner);

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

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrscanner();
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
                rooms = Integer.parseInt(registernoofrooms.getText().toString());
                evc = registerevc.getText().toString();
                if (!validatefields(registerName, registerAddress, registerPassword, registerMail, registernoofrooms)) {
                    return;
                }
                Member member = new Member();
                member.setUsername(name);
                memberList.add(member);
                member.setPassword(password);
                member.setEmailId(email);
                member.setAddress(address);
                member.setPropertytype(type);
                member.setEvc(evc);
                member.setNoofrooms(rooms);
                opendashboard();
                Toast.makeText(RegisterActivity.this, "Welcome " + member.getUsername()
                        + member.getAddress() + member.getEvc() + member.getPropertytype() + member.getNoofrooms()
                        + member.getEmailId(), Toast.LENGTH_LONG).show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensignin();
            }
        });
    }

    private void qrscanner() {
        ScanOptions options = new ScanOptions();
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            TextView registerevc = findViewById(R.id.registerevc);
            registerevc.setText(result.getContents());
//            evcset(result.getContents());
//            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//            builder.setTitle("Result");
//            builder.setMessage(result.getContents());
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    evcset(result.getContents());
//                    dialogInterface.dismiss();
//                }
//            }).show();
//            member.setEvc(evcscan);
//            Toast.makeText( RegisterActivity.this, member.getEvc(), Toast.LENGTH_LONG).show();
        }
    });

    private boolean validatefields(TextView registerName, TextView registerAddress, TextView registerPassword, TextView registerMail, TextView registernoofrooms) {
        if (name.isEmpty()) {
            registerName.setError("Please enter your name");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerMail.setError("Please enter a valid Email ID");
        }
        if (!passwordValidation(password)) {
            registerPassword.setError("Please create valid password (Eg: Abcd@123");
        }
        if (address.isEmpty()) {
            registerAddress.setError("Please enter your address");
        }
        if (rooms < 1) {
            registernoofrooms.setError("This is a required field");
        }
        return !name.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && passwordValidation(password) && !address.isEmpty() && !(rooms < 1);
    }

//    public void evcset(String evcset){
//        TextView registerevc = findViewById(R.id.registerevc);
//        registerevc.setText(evcset);
//    }

    public void opensignin(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void opendashboard(){
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public boolean passwordValidation(String password) {
        String passwordRegex = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
        return Pattern.matches(passwordRegex, password);
    }
}

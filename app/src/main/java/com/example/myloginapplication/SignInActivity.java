package com.example.myloginapplication;

import static com.example.myloginapplication.MainActivity.mongoCollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myloginapplication.Model.Member;

import org.bson.Document;

import java.security.MessageDigest;
import java.util.Locale;

public class SignInActivity extends AppCompatActivity {
    static Member loggedMember;
    static Member mem;
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
                String mail = email.getText().toString().toLowerCase(Locale.ROOT);
                String pswrd = password.getText().toString();
                Document queryFilter  = new Document("emailId", mail);
                String hash = hashing(pswrd);
                mongoCollection.findOne(queryFilter).getAsync(task -> {
                    if (task.isSuccess()) {
                        mem = (Member) task.get();
                        if(mem != null) {
                            if (mem.isAdmin()) {
                                if (hash.equals(mem.getPassword())) {
                                    loggedMember = mem;
                                    openAdmindashboard();
                                } else {
                                    password.setError("Enter a valid password");
                                }
                            } else {
                                if (hash.equals(mem.getPassword())) {
                                    loggedMember = mem;
                                    opendashboard();
                                } else {
                                    password.setError("Enter a valid password");
                                }
                            }
                        }else{
                            email.setError("User not found");
                        }
                    }
                });
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openregister();
            }
        });
    }

    private void openAdmindashboard() {
        Intent intent = new Intent(this, AdmindashboardActivity.class);
        startActivity(intent);
    }

    public void openregister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void opendashboard(){
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public static String hashing(String password) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    private static String bytesToHex(byte[] hash) {
        final StringBuilder builder=new StringBuilder();
        for(byte b:hash) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
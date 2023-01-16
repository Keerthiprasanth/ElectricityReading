package com.example.myloginapplication;

import static com.example.myloginapplication.MainActivity.pojoCodecRegistry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.myloginapplication.Model.Voucher;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;

public class RegisterActivity extends AppCompatActivity {
    static Member member;
    String name, email, password, address, type, evc;
    int rooms;
    static Member loggedMember;
    MongoDatabase mongoDatabase = MainActivity.mongoDatabase;
    MongoCollection<Member> mongoCollection = MainActivity.mongoCollection;
    CodecRegistry pojoCodecRegistry = MainActivity.pojoCodecRegistry;
    Double balance;
    TextView registernoofrooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView registerName = findViewById(R.id.registrationname);
        TextView registerMail = findViewById(R.id.registermail);
        TextView registerPassword = findViewById(R.id.registerpassword);
        TextView registerAddress = findViewById(R.id.registeraddress);
        registernoofrooms = findViewById(R.id.registernoofrooms);
        TextView registerevc = findViewById(R.id.registerevc);
        TextView btn = findViewById(R.id.texthaveacc);
        TextView scanner = findViewById(R.id.scanner);

        Spinner propertyType = findViewById(R.id.propertytype);

        String[] propertyTypes = getResources().getStringArray(R.array.property_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.property_file, propertyTypes);
        adapter.setDropDownViewResource(R.layout.property_file);
        propertyType.setAdapter(adapter);

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
                member = new Member();
                Log.v("BTN","Entered BTN");
                name = registerName.getText().toString();
                email = registerMail.getText().toString().toLowerCase(Locale.ROOT);
                password = registerPassword.getText().toString();
                address = registerAddress.getText().toString();
                evc = registerevc.getText().toString();

                if (!validatefields(registerName, registerAddress, registerPassword, registerMail, registernoofrooms, registerevc)) {
                    return;
                }

                member.setName(name);
                member.setPassword(hashing(password));
                member.setEmailId(email);
                member.setAddress(address);
                member.setPropertytype(type);
                member.setEvc(evc);

                try {
                    rooms = Integer.parseInt(registernoofrooms.getText().toString());
                }catch (NumberFormatException ex){
                    registernoofrooms.setText("1");
                    rooms = Integer.parseInt(registernoofrooms.getText().toString());
                    registernoofrooms.setError("Rooms should not be less than 1");
                }
                member.setNoofrooms(rooms);

                try {
                    Document queryFilter = new Document("emailId", email);
                    mongoCollection.findOne(queryFilter).getAsync(task -> {
                        if (task.isSuccess()) {
                        Member mem = (Member) task.get();
                        if(mem != null) {
                            if (email.equals(task.get().getEmailId())) {
                                Log.v("User", "User Exists");
                                Toast.makeText(RegisterActivity.this, "User already exists! Try signing in", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            MongoCollection<Voucher> Collection = mongoDatabase.getCollection("voucher",Voucher.class).withCodecRegistry(pojoCodecRegistry);
                            Document queryEvc = new Document("evc", evc);
                            Collection.findOne(queryEvc).getAsync(value -> {
                                if (value.isSuccess()) {
                                    Voucher voucher = (Voucher) value.get();
                                    if(voucher != null){
                                        if (evc.equals(voucher.getEvc())){
                                            registerevc.setError("Voucher already exists");
//                                            balance = 200.0;
//                                            member.setBalance(balance);
//                                            mongoCollection.insertOne(member).getAsync(result -> {
//                                                if (result.isSuccess()) {
//                                                    loggedMember = member;
//                                                    Log.v("Data", "Data inserted");
//                                                    Toast.makeText(RegisterActivity.this, "Registered Successfully! Try signing in", Toast.LENGTH_LONG).show();
//                                                    opensignin();
//                                                } else {
//                                                    Log.v("Data", "Error:" + result.getError().toString());
//                                                }
//                                            });
                                        }
                                    }
                                    else{
                                        Voucher newVoucher = new Voucher();
                                        newVoucher.setEvc(evc);
                                        Collection.insertOne(newVoucher).getAsync(evc -> {
                                            if(evc.isSuccess()){
                                                Log.v("NewEVC","EVC added");
                                            }else{
                                                Log.v("NewEVC",evc.getError().toString());
                                            }
                                        });
                                        balance = 200.0;
                                        member.setBalance(balance);
                                        mongoCollection.insertOne(member).getAsync(result -> {
                                            if (result.isSuccess()) {
                                                loggedMember = member;
                                                Log.v("Data", "Data inserted");
                                                Toast.makeText(RegisterActivity.this, "Registered Successfully! Try signing in", Toast.LENGTH_LONG).show();
                                                opensignin();
                                            } else {
                                                Log.v("Data", "Error:" + result.getError().toString());
                                            }
                                        });
//                                        registerevc.setError("Voucher is invalid");
                                    }
                                }else{
                                    Voucher voucher = new Voucher();
                                    voucher.setEvc(evc);
                                    Collection.insertOne(voucher).getAsync(evc -> {
                                        if(evc.isSuccess()){
                                            Log.v("NewEVC","EVC added");
                                        }else{
                                            Log.v("NewEVC",evc.getError().toString());
                                        }
                                    });
                                    balance = 200.0;
                                    member.setBalance(balance);
                                    mongoCollection.insertOne(member).getAsync(result -> {
                                        if (result.isSuccess()) {
                                            loggedMember = member;
                                            Log.v("Data", "Data inserted");
                                            Toast.makeText(RegisterActivity.this, "Registered Successfully! Try signing in", Toast.LENGTH_LONG).show();
                                            opensignin();
                                        } else {
                                            Log.v("Data", "Error:" + result.getError().toString());
                                        }
                                    });
                                }
                            });
                            }
                        }
                    });
                }catch (NullPointerException ignored){
                    Log.v("Exception","Null Pointer Exception");
                    Toast.makeText(RegisterActivity.this,"Enter all the fields properly",Toast.LENGTH_LONG).show();
                }
                Log.v("BTN","Passed collection");
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
        }
    });

    private boolean validatefields(TextView registerName, TextView registerAddress, TextView registerPassword, TextView registerMail, TextView registernoofrooms, TextView registerevc) {
        if (name.isEmpty()) {
            registerName.setError("Please enter your name");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerMail.setError("Please enter a valid Email ID");
        }
        if (!passwordValidation(password)) {
            registerPassword.setError("Your password length should be 8 and contain " +
                    "\n1) Uppercase Letter"+"\n2) Lowercase Letter"+"\n3) Number"+"\n4) Symbol"+
                    "\n(Eg: Abcd@123)");
        }
        if (address.isEmpty()) {
            registerAddress.setError("Please enter your address");
        }
        if (evc.isEmpty()){
            registerevc.setError("Please enter a valid EVC");
        }
        return !name.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && passwordValidation(password) && !address.isEmpty() && !evc.isEmpty();
    }

    public void opensignin(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public boolean passwordValidation(String password) {
        String passwordRegex = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
        return Pattern.matches(passwordRegex, password);
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

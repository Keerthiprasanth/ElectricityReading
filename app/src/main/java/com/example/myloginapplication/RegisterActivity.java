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

public class RegisterActivity extends AppCompatActivity {
    Member member;
    static final List<Member> memberList = new ArrayList<>();
    String name, email, password, address, type, evc;
    int rooms;
    int flag;
    MongoDatabase mongoDatabase = MainActivity.mongoDatabase;
    MongoClient mongoClient = MainActivity.mongoClient;
    MongoCollection<Member> mongoCollection = MainActivity.mongoCollection;
    User user;
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
                member = new Member();
                List<Member> memberList = new ArrayList<>();
                Log.v("BTN","Entered BTN");
                name = registerName.getText().toString();
                email = registerMail.getText().toString().toLowerCase(Locale.ROOT);
                password = registerPassword.getText().toString();
                address = registerAddress.getText().toString();
//                rooms = Integer.parseInt(registernoofrooms.getText().toString());
                evc = registerevc.getText().toString();

//                validaterooms(registernoofrooms);
                if (!validatefields(registerName, registerAddress, registerPassword, registerMail, registernoofrooms, registerevc)) {
                    return;
                }

                checkUser();

                member.setName(name);
                memberList.add(member);
                member.setPassword(hashing(password));
                member.setEmailId(email);
                member.setAddress(address);
                member.setPropertytype(type);
                member.setEvc(evc);
//                member.setNoofrooms(rooms);

//                Toast.makeText(RegisterActivity.this, "Welcome " + member.getName()
//                        + member.getAddress() + member.getEvc() + member.getPropertytype() + member.getNoofrooms()
//                        + member.getEmailId(), Toast.LENGTH_LONG).show();

//                CodecRegistry pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
//                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
//                MongoCollection<Member> mongoCollection = mongoDatabase.getCollection("member",Member.class).withCodecRegistry(pojoCodecRegistry);
//                if(flag == 0) {
                    mongoCollection.insertOne(member).getAsync(result -> {
                        if (result.isSuccess()) {
                            Log.v("Data", "Data inserted");
                            Toast.makeText(RegisterActivity.this,"Welcome",Toast.LENGTH_LONG).show();
                            opendashboard();
                        } else {
                            Log.v("Data", "Error:" + result.getError().toString());
                        }
                    });
//                }
                Log.v("BTN","Passed collection");

//                Document queryFilter  = new Document("emailId", "steve@gmail.com");
//                mongoCollection.findOne(queryFilter).getAsync(task -> {
//                    if (task.isSuccess()) {
//                        Member result = (Member) task.get();
//                        Log.v("EXAMPLE", "successfully found a document: " + result + result.getPassword()+result.getName());
//                    } else {
//                        Log.e("EXAMPLE", "failed to find document with: ", task.getError());
//                    }
//                });
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensignin();
            }
        });
    }

    private void validateEvc(TextView registerEvc) {
        MongoCollection<Document> Collection = mongoDatabase.getCollection("voucher").withCodecRegistry(pojoCodecRegistry);
        Document queryFilter  = new Document("evc", this.evc);
        Collection.findOne(queryFilter).getAsync(task -> {
            if (task.isSuccess()) {
                member.setEvc(evc);
            }else{
                registerEvc.setError("Voucher is invalid");
            }
        });
    }

    private void checkUser() {
        Document queryFilter  = new Document("emailId", this.email);
        mongoCollection.findOne(queryFilter).getAsync(task -> {
            if (task.isSuccess()) {
                Member member = (Member) task.get();
                if(member != null) {
                    Log.v("User", "User Exists");
                    Toast.makeText(RegisterActivity.this, "User already exists! Try signing in", Toast.LENGTH_LONG).show();
                    opensignin();
                }
                else{
                    Log.v("User","User does not exist");
                }
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
        validateEvc(registerevc);
        validaterooms(registernoofrooms);
        return !name.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && passwordValidation(password) && !address.isEmpty()
                && !validaterooms(registernoofrooms);
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

    public boolean validaterooms(TextView registernoofrooms){
        try {
            if (Integer.parseInt(registernoofrooms.getText().toString()) < 1) {
//                Log.v("Result","It is a number");
                rooms = Integer.parseInt(registernoofrooms.getText().toString());
                member.setNoofrooms(rooms);
                return true;
            }
        }catch (NumberFormatException ex){
//            Log.v("Result","Not a number");
            registernoofrooms.setText("1");
//            rooms = Integer.parseInt(registernoofrooms.getText().toString());
            registernoofrooms.setError("Rooms should not be less than 1");
            validaterooms(registernoofrooms);
        }
        return false;
    }
    //////////////////////
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
// return DatatypeConverter.printHexBinary(hash);
        final StringBuilder builder=new StringBuilder();
        for(byte b:hash) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
    //////////////////
}

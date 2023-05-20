package com.example.myloginapplication;

import static com.example.myloginapplication.MainActivity.mongoCollection;
import static com.example.myloginapplication.MainActivity.mongoDatabase;
import static com.example.myloginapplication.MainActivity.pojoCodecRegistry;
import static com.example.myloginapplication.RegisterActivity.member;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myloginapplication.Model.Admin;
import com.example.myloginapplication.Model.Member;
import com.example.myloginapplication.Model.Readings;
import com.example.myloginapplication.Model.Voucher;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.bson.Document;

import java.util.Collections;

import io.realm.mongodb.mongo.MongoCollection;

public class PaymentActivity extends AppCompatActivity {
    Double balance = SignInActivity.mem.getBalance();
    String evc;
    String name = SignInActivity.mem.getName();
    Member loggedmember = DashboardActivity.dashBoardMember;
    Member member;
    Double bill = loggedmember.getBill();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        TextView textbalance = findViewById(R.id.balance);
        TextView textbill = findViewById(R.id.bill);
        TextView textevc = findViewById(R.id.evc);
        TextView scanner = findViewById(R.id.scanner);

        Button pay = findViewById(R.id.paybtn);
        Button submit = findViewById(R.id.voucherbtn);

        MongoCollection<Member> memberMongoCollection = mongoDatabase.getCollection("member",Member.class).withCodecRegistry(pojoCodecRegistry);
        Document queryMember = new Document("emailId",loggedmember.getEmailId());
        memberMongoCollection.findOne(queryMember).getAsync(task -> {
            if(task.isSuccess()){
                member = (Member) task.get();
                if(member!=null){
                    Log.v("Member","Member");
                }
            }
        });

        textbalance.setText("Welcome "+name+" Your balance is "+loggedmember.getBalance());
        textbill.setText("Your current bill value is "+loggedmember.getBill());

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrscanner();
            }
        });
//        calculateBill();
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(balance>=bill) {
                    double currentBalance = balance - bill;
                    member.setBalance(currentBalance);
                    member.setBill(0.0);
                    textbalance.setText("Your balance is " + currentBalance);
                    textbill.setText("Your current bill value is "+ 0.0);
                    MongoCollection<Member> memberMongoCollection = mongoDatabase.getCollection("member",Member.class).withCodecRegistry(pojoCodecRegistry);
                    Document queryMember = new Document("emailId",member.getEmailId());
                    Document updateDocument = new Document().append("$set", new Document().append("bill",bill).append("balance",currentBalance));

                    memberMongoCollection.updateOne(queryMember,updateDocument).getAsync(result -> {
                        if(result.isSuccess()){
                            Log.v("Bill","Bill set successfully");
                        }else{
                            Log.v("Bill",result.getError().toString());
                        }
                    });
                }else{
                    Toast.makeText(PaymentActivity.this,"Insufficient Balance",Toast.LENGTH_LONG).show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evc = textevc.getText().toString();
                MongoCollection<Voucher> Collection = mongoDatabase.getCollection("voucher",Voucher.class).withCodecRegistry(pojoCodecRegistry);
                Document queryEvc = new Document("evc", evc);
                Collection.findOne(queryEvc).getAsync(value -> {
                    if (value.isSuccess()) {
                        Voucher voucher = (Voucher) value.get();
                        if(voucher != null){
                            if (evc.equals(voucher.getEvc())){
                                balance = 200.0;
                                member.setBalance(member.getBalance()+balance);
                                textbalance.setText("Your balance is "+member.getBalance());
                            }
                        }else {
                            textevc.setError("Voucher is invalid");
                        }
                    }
                });
            }
        });
    }

//    private void calculateBill() {
//        MongoCollection<Readings> readingsMongoCollection = mongoDatabase.getCollection("readings",Readings.class).withCodecRegistry(pojoCodecRegistry);
//        Document queryReading = new Document("userEmail",member.getEmailId());
//        readingsMongoCollection.findOne(queryReading).getAsync(task -> {
//            if(task.isSuccess()){
//                Readings readings = (Readings) task.get();
//                if(readings != null){
//                    MongoCollection<Admin> adminMongoCollection = mongoDatabase.getCollection("admin",Admin.class).withCodecRegistry(pojoCodecRegistry);
//                    Document queryAdmin = new Document("loggedEmail","gse@shangrila.gov.un");
//                    adminMongoCollection.findOne(queryAdmin).getAsync(value -> {
//                        if(value.isSuccess()) {
//                            Admin admin = (Admin) value.get();
//                            if (admin != null) {
//                                double bill = readings.getElecDay() * admin.getPriceDay() + readings.getElecNight() * admin.getPriceNight()
//                                        + readings.getGas() * admin.getPriceGas() + 0.74 * 30;
//                                Log.v("Bill", String.valueOf(bill));
////                                member.setBill(bill);
//                                MongoCollection<Member> memberMongoCollection = mongoDatabase.getCollection("member",Member.class).withCodecRegistry(pojoCodecRegistry);
//                                Document queryMember = new Document("emailId",member.getEmailId());
//                                memberMongoCollection.updateOne(queryMember,member.setBill(bill)).getAsync(result -> {
//                                    if(result.isSuccess()){
//                                        Log.v("Bill","Bill set successfully");
//                                    }else{
//                                        Log.v("Bill",result.getError().toString());
//                                    }
//                                });
//                                Toast.makeText(PaymentActivity.this,"Bill is "+bill,Toast.LENGTH_LONG).show();
//                            }else{
//                                Log.v("Admin","Admin is Null");
//                            }
//                        }else{
//                            Log.v("Admin",value.getError().toString());
//                        }
//                    });
//                }
//            }
//        });
//    }

    private void qrscanner() {
        ScanOptions options = new ScanOptions();
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barlauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barlauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            TextView evc = findViewById(R.id.evc);
            evc.setText(result.getContents());
        }
    });

}

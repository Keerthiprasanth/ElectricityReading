package com.example.myloginapplication;

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

import com.example.myloginapplication.Model.Member;
import com.example.myloginapplication.Model.Voucher;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.bson.Document;
import io.realm.mongodb.mongo.MongoCollection;

public class PaymentActivity extends AppCompatActivity {
    Double balance = SignInActivity.mem.getBalance();
    Double bill = SignInActivity.mem.getBill();
    String evc;
    String name = SignInActivity.mem.getName();
    Member member = SignInActivity.mem;
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

        textbalance.setText("Welcome "+name+" Your balance is "+balance);
        textbill.setText("Your current bill value is "+bill);

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrscanner();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(balance>=bill) {
                    double currentBalance = balance - bill;
                    member.setBalance(currentBalance);
                    member.setBill(0.0);
                    textbalance.setText("Your balance is " + currentBalance);
                    textbill.setText("Your current bill value is 0.0");
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

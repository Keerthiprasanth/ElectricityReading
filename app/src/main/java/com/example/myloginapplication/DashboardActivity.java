package com.example.myloginapplication;

import static com.example.myloginapplication.MainActivity.pojoCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myloginapplication.Model.Admin;
import com.example.myloginapplication.Model.Member;
import com.example.myloginapplication.Model.Readings;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class DashboardActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    double dayreading,nightreading,gasreading;
    static final List<Readings> readingList = new ArrayList<>();
    MongoDatabase mongoDatabase = MainActivity.mongoDatabase;
    private DatePickerDialog datePickerDialog;
    Readings readings;
    Member member = SignInActivity.mem;
    Member mem = SignInActivity.loggedMember;
    Member loggedMember;
    int flag = 0;
    Double day,night,gas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView datebtn = findViewById(R.id.datebtn);
        TextView textday = findViewById(R.id.textday);
        TextView textnight = findViewById(R.id.textnight);
        TextView textgas = findViewById(R.id.textgas);
        Button submitbtn = findViewById(R.id.customersubmit);
        Button paymentbtn = findViewById(R.id.paymentpage);

        if(member == null){
            loggedMember = mem;
        }else{
            loggedMember = member;
        }

        MongoCollection<Readings> readingsCollection = mongoDatabase.getCollection("readings",Readings.class).withCodecRegistry(pojoCodecRegistry);
        Document lastAddedReading= new Document(new Document("elecDay", 34));
        readingsCollection.find().sort(lastAddedReading).first().getAsync(last -> {
            if(last.isSuccess()){
                Readings data = last.get();
                if(data!=null){
                    Log.v("Data",data.getDate());
                }
            }
            else{
                Log.v("Data",last.getError().toString());
            }
        });

        calculateBill();

        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.fragment.app.DialogFragment submission = new DatePickerFragment();
                submission.show(getSupportFragmentManager(), "Date");
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readings = new Readings();
                if(!validatefields(datebtn,textday,textnight,textgas)){
                    return;
                }
                CodecRegistry pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
                MongoCollection<Readings> mongoCollection = mongoDatabase.getCollection("readings",Readings.class).withCodecRegistry(pojoCodecRegistry);
                mongoCollection.insertOne(readings).getAsync(result -> {
                    if(result.isSuccess()){
                        Log.v("Data","Data inserted");
                    }else{
                        Log.v("Data","Error:"+result.getError().toString());
                    }
                });
                Toast.makeText( DashboardActivity.this, "Readings submitted", Toast.LENGTH_LONG).show();
            }
        });

        paymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateBill();
                if(flag == 1) {
                    openPayment();
                }
            }
        });
    }

    private boolean validatefields(TextView datebtn,TextView textday, TextView textnight, TextView textgas) {
        try {
            if(loggedMember != null) {
                readings.setUserEmail(member.getEmailId());
                readings.setDate(datebtn.getText().toString());
                dayreading = Double.parseDouble(textday.getText().toString());
                readings.setElecDay(dayreading);
                nightreading = Double.parseDouble(textnight.getText().toString());
                readings.setElecNight(nightreading);
                gasreading = Double.parseDouble(textgas.getText().toString());
                readings.setGas(gasreading);
                day = Double.parseDouble(textday.getText().toString());
                night = Double.parseDouble(textnight.getText().toString());
                gas = Double.parseDouble(textgas.getText().toString());
                flag=0;
                return true;
            }
        }catch (NumberFormatException ex){
            Toast.makeText(DashboardActivity.this,"All the fields are required",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void calculateBill() {
        MongoCollection<Readings> readingsMongoCollection = mongoDatabase.getCollection("readings",Readings.class).withCodecRegistry(pojoCodecRegistry);
        Document queryReading = new Document("userEmail",member.getEmailId());
        readingsMongoCollection.findOne(queryReading).getAsync(task -> {
            if(task.isSuccess()){
                Readings readings = (Readings) task.get();
                if(readings != null){
                    MongoCollection<Admin> adminMongoCollection = mongoDatabase.getCollection("admin",Admin.class).withCodecRegistry(pojoCodecRegistry);
                    Document lastAddedObject= new Document(new Document("_id", -1));
                    adminMongoCollection.find().sort(lastAddedObject).first().getAsync(last -> {
                        if(last.isSuccess()){
                            Admin admin = (Admin) last.get();
                            if(admin!=null){
                                double bill = (day - readings.getElecDay()) * admin.getPriceDay() + (readings.getElecNight()) * admin.getPriceNight()
                                        + (gas - readings.getGas()) * admin.getPriceGas() + 0.74 * 30;
                                Log.v("Bill", String.valueOf(bill));
                                MongoCollection<Member> memberMongoCollection = mongoDatabase.getCollection("member",Member.class).withCodecRegistry(pojoCodecRegistry);
                                Document queryMember = new Document("emailId",member.getEmailId());
                                Document updateDocument = new Document("$set", new Document("bill",bill));
                                memberMongoCollection.updateOne(queryMember,updateDocument).getAsync(result -> {
                                    if(result.isSuccess()){
                                        Log.v("Bill","Bill set successfully");
                                        flag=1;
                                    }else{
                                        Log.v("Bill",result.getError().toString());
                                    }
                                });
                                Toast.makeText(DashboardActivity.this,"Bill is "+bill,Toast.LENGTH_LONG).show();
                            }else{
                                Log.v("Admin","Admin is Null");
                            }
                        }
                    });
                }else{
                    flag = 1;
                }
            }else{
                flag = 1;
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        Toast.makeText(DashboardActivity.this,"Date - "+day+(month+1)+year,Toast.LENGTH_LONG).show();
        String datefinal = String.valueOf(day)+" "+String.valueOf(month+1)+" "+String.valueOf(year);
        TextView datebtn = findViewById(R.id.datebtn);
        datebtn.setText(datefinal);
    }

    public void openPayment(){
        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }
}

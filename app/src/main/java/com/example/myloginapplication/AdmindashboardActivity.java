package com.example.myloginapplication;

import static com.example.myloginapplication.MainActivity.mongoDatabase;
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

import org.bson.codecs.configuration.CodecRegistry;

import java.util.Calendar;

import io.realm.mongodb.mongo.MongoCollection;

public class AdmindashboardActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    double dayprize,nightprize,gasprize,standardprize;
    CodecRegistry pojoCodecRegistry = MainActivity.pojoCodecRegistry;
    Admin admin;
    Member loggedMember = SignInActivity.loggedMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindashboard);

        TextView datebtn = findViewById(R.id.datebtn);
        TextView priceday = findViewById(R.id.prizeday);
        TextView pricenight = findViewById(R.id.prizenight);
        TextView pricegas = findViewById(R.id.prizegas);
        TextView pricestandard = findViewById(R.id.prizestandard);
        Button adminsubmit = findViewById(R.id.adminsubmit);
        Button viewReadings = findViewById(R.id.readingsbtn);

        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.fragment.app.DialogFragment submission = new DatePickerFragment();
                submission.show(getSupportFragmentManager(), "Date");
            }
        });

        adminsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin = new Admin();
                if(!validatefields(datebtn,priceday,pricenight,pricegas,pricestandard)){
                    return;
                }
//                if(!validateday(priceday)){
//                    return;
//                }
//                if(!validatenight(pricenight)){
//                    return;
//                }
//                if(!validategas(pricegas)){
//                    return;
//                }
//                if(!validateday(priceday) && !validatenight(pricenight) && !validategas(pricegas)){
//                    return;
//                }
//                CodecRegistry pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
//                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
                MongoCollection<Admin> mongoCollection = mongoDatabase.getCollection("admin",Admin.class).withCodecRegistry(pojoCodecRegistry);
                mongoCollection.insertOne(admin).getAsync(result -> {
                    if(result.isSuccess()){
                        Log.v("Data","Data inserted");
                    }else{
                        Log.v("Data","Error:"+result.getError().toString());
                    }
                });
                Toast.makeText( AdmindashboardActivity.this, "Prize submitted", Toast.LENGTH_LONG).show();
            }
        });

        viewReadings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReadings();
            }
        });
    }
    private boolean validateday(TextView priceday) {
        try {
            dayprize = Double.parseDouble(priceday.getText().toString());
            admin.setPriceDay(dayprize);
            return true;
        }catch (NumberFormatException ex){
            priceday.setError("Required");
        }
        return false;
    }
    private boolean validatenight(TextView pricenight) {
        try {
            nightprize = Double.parseDouble(pricenight.getText().toString());
            admin.setPriceNight(nightprize);
            return true;
        }catch (NumberFormatException ex){
            pricenight.setError("Required");
        }
        return false;
    }
    private boolean validategas(TextView pricegas) {
        try {
            gasprize = Double.parseDouble(pricegas.getText().toString());
            admin.setPriceGas(gasprize);
            return true;
        }catch (NumberFormatException ex){
            pricegas.setError("Required");
        }
        return false;
    }
    private boolean validatefields(TextView datebtn, TextView priceday, TextView pricenight, TextView pricegas, TextView pricestandard) {
        try {
            if (loggedMember != null) {
                admin.setLoggedEmail(loggedMember.getEmailId());
                admin.setDate(datebtn.getText().toString());
                dayprize = Double.parseDouble(priceday.getText().toString());
                admin.setPriceDay(dayprize);
                nightprize = Double.parseDouble(pricenight.getText().toString());
                admin.setPriceNight(nightprize);
                gasprize = Double.parseDouble(pricegas.getText().toString());
                admin.setPriceGas(gasprize);
                standardprize = Double.parseDouble(pricestandard.getText().toString());
                admin.setPrizeStandard(standardprize);
                return true;
            }
        }catch (NumberFormatException ex){
            Toast.makeText(AdmindashboardActivity.this,"All the fields are required",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        Toast.makeText(AdmindashboardActivity.this,"Date - "+day+(month+1)+year,Toast.LENGTH_LONG).show();
        String datefinal = String.valueOf(day)+" "+String.valueOf(month+1)+" "+String.valueOf(year);
//        String datefinal = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
        TextView datebtn = findViewById(R.id.datebtn);
        datebtn.setText(datefinal);
//        datebtn.setText(day+"/"+(Integer.parseInt(String.valueOf(month))+1)+"/"+year);
    }

    public void openReadings(){
        Intent intent = new Intent(this, ReadingsActivity.class);
        startActivity(intent);
    }
}

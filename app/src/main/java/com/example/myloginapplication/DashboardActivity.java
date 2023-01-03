package com.example.myloginapplication;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myloginapplication.Model.Readings;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.text.DateFormat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView datebtn = findViewById(R.id.datebtn);
        TextView textday = findViewById(R.id.textday);
        TextView textnight = findViewById(R.id.textnight);
        TextView textgas = findViewById(R.id.textgas);
        Button submitbtn = findViewById(R.id.customersubmit);

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
                readings.setDate(datebtn.getText().toString());
//                readings.setElecDay(validateday(textday));
//                nightreading = Double.parseDouble(textnight.getText().toString());
//                readings.setElecNight(nightreading);
//                gasreading = Double.parseDouble(textgas.getText().toString());
//                readings.setGas(gasreading);
//                readingList.add(readings);
                if(!validateday(textday) && !validatenight(textnight) && !validategas(textgas)){
                    return;
                }
//                if (!validatefields(textday, textnight, textgas, datebtn)) {
//                    return;
//                }
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
                Toast.makeText( DashboardActivity.this, "Reading values are "+String.valueOf(dayreading)
                        +String.valueOf(nightreading)+String.valueOf(gasreading), Toast.LENGTH_LONG).show();
//                Toast.makeText( DashboardActivity.this, "Not set submit button yet", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateday(TextView textday) {
        try {
            dayreading = Double.parseDouble(textday.getText().toString());
            readings.setElecDay(dayreading);
            return true;
        }catch (NumberFormatException ex){
            textday.setError("Required");
        }
        return false;
    }
    private boolean validatenight(TextView textnight) {
        try {
            nightreading = Double.parseDouble(textnight.getText().toString());
            readings.setElecDay(nightreading);
            return true;
        }catch (NumberFormatException ex){
            textnight.setError("Required");
        }
        return false;
    }
    private boolean validategas(TextView textgas) {
        try {
            gasreading = Double.parseDouble(textgas.getText().toString());
            readings.setGas(gasreading);
            return true;
        }catch (NumberFormatException ex){
            textgas.setError("Required");
        }
        return false;
    }

//    private boolean validatefields(TextView textday, TextView textnight, TextView textgas, TextView datebtn) {
//            try {
//                if (Double.parseDouble(textday.getText().toString()) < 1) {
//                    dayreading = Double.parseDouble(textday.getText().toString());
//                    readings.setElecDay(dayreading);
//                    return true;
//                }
//            }catch (NumberFormatException ex){
////            Log.v("Result","Not a number");
//                registernoofrooms.setText("1");
//                rooms = Integer.parseInt(registernoofrooms.getText().toString());
//                registernoofrooms.setError("Rooms should not be less than 1");
//            }
//        return false;
//    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        String today = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
        TextView datebtn = findViewById(R.id.datebtn);
        datebtn.setText(today);
//        datebtn.setText(day+"/"+(Integer.parseInt(String.valueOf(month))+1)+"/"+year);
    }
}

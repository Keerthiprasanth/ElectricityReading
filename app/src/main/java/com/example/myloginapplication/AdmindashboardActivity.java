package com.example.myloginapplication;

import static com.example.myloginapplication.MainActivity.mongoDatabase;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myloginapplication.Model.Admin;
import com.example.myloginapplication.Model.Readings;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.mongo.MongoCollection;

public class AdmindashboardActivity extends AppCompatActivity {
    double dayprize,nightprize,gasprize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindashboard);

        TextView priceday = findViewById(R.id.prizeday);
        TextView pricenight = findViewById(R.id.prizenight);
        TextView pricegas = findViewById(R.id.prizegas);
        Button adminsubmit = findViewById(R.id.adminsubmit);

        adminsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Admin admin = new Admin();
                dayprize= Double.parseDouble(priceday.getText().toString());
                admin.setPriceDay(dayprize);
                nightprize= Double.parseDouble(pricenight.getText().toString());
                admin.setPriceNight(nightprize);
                gasprize= Double.parseDouble(pricegas.getText().toString());
                admin.setPriceGas(gasprize);
                CodecRegistry pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
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
    }
}

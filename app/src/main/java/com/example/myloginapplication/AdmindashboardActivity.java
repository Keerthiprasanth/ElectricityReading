package com.example.myloginapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myloginapplication.Model.Admin;

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
                Toast.makeText( AdmindashboardActivity.this, "Prize submitted", Toast.LENGTH_LONG).show();
            }
        });
    }
}
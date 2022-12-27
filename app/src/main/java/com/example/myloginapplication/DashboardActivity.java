package com.example.myloginapplication;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;

public class DashboardActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    double dayreading,nightreading,gasreading;
    String dayread,nightread,gasread;

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
//                dayread = textday.getText().toString();
//                dayreading = Double.parseDouble(dayread);
//                Toast.makeText( DashboardActivity.this, (int) dayreading, Toast.LENGTH_LONG).show();
                Toast.makeText( DashboardActivity.this, "Not set submit button yet", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        String today = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());

        TextView datebtn = (TextView) findViewById(R.id.datebtn);
        datebtn.setText(today);
    }
}

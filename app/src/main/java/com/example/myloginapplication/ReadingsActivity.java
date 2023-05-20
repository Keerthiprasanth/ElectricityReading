package com.example.myloginapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myloginapplication.Model.Readings;

import java.util.ArrayList;
import java.util.List;

public class ReadingsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ReadingsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        recyclerView = findViewById(R.id.tablerecycler);

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReadingsAdapter(this,getList());
        recyclerView.setAdapter(adapter);
    }

    private List<Readings> getList(){
        List<Readings> readingsList=new ArrayList<>();
        readingsList.add(new Readings());
        return readingsList;
    }
}

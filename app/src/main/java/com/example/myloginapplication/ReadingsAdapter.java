package com.example.myloginapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myloginapplication.Model.Member;
import com.example.myloginapplication.Model.Readings;

import org.bson.codecs.configuration.CodecRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.FindIterable;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class ReadingsAdapter extends RecyclerView.Adapter<ReadingsAdapter.ViewHolder>{
    Context context;
    List<Readings> readingsList = new ArrayList<>();
    List<MongoCursor<Readings>> readList = new ArrayList<MongoCursor<Readings>>();
    MongoDatabase mongoDatabase = MainActivity.mongoDatabase;
    MongoClient mongoClient = MainActivity.mongoClient;
    MongoCollection<Member> mongoCollection = MainActivity.mongoCollection;
    CodecRegistry pojoCodecRegistry = MainActivity.pojoCodecRegistry;
    FindIterable<Readings> reading;

    private boolean dataChanged = false;

    public ReadingsAdapter(Context context,List<Readings> readingsList) {
        this.context = context;
        this.readingsList = readingsList;
    }

    @NonNull
    @Override
    public ReadingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReadingsAdapter.ViewHolder holder, int position) {

        MongoCollection<Readings> collection = mongoDatabase.getCollection("readings", Readings.class).withCodecRegistry(pojoCodecRegistry);
        collection.find().iterator().getAsync(task -> {
            if (task.isSuccess()) {
                readList.add(task.get());
                MongoCursor<Readings> read = task.get();
                read.forEachRemaining(readingsList::add);
                read.close();
                dataChanged = true;
            }
        });

//        FindIterable<Readings> iterDoc = collection.find();
//        iterDoc.forEach(new Block<Readings>() {
//            @Override
//            public void apply(Readings reading) {
//                System.out.println(reading);
//            }
//        });
//        Iterator<Readings> it = iterDoc.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next());
//        }

//        for (int j=0;j<=2;j++) {
//            reading = collection.find();
//        }
//        for(int j=0;j<readList.size();j++){
//            Readings read = readList(j);
//        }

//        for(int i=0;i<readingsList.size();i++){
        if (dataChanged && readingsList != null && readingsList.size() > 0) {
            dataChanged = false;
            notifyDataSetChanged();
            for (int i = 0; i < readingsList.size(); i++) {
                if (position < readingsList.size()) {
                    Readings readings = readingsList.get(position);
//            Readings model = readingsList.get(position);
                    holder.customerID.setText(readings.getUserEmail());
                    holder.date.setText(readings.getDate());
                    holder.dayReading.setText(String.valueOf(readings.getElecDay()));
                    holder.nightReading.setText(String.valueOf(readings.getElecNight()));
                    holder.gasReading.setText(String.valueOf(readings.getGas()));
                }
            }
        } else {
            holder.customerID.setText("Null");
            holder.date.setText("Null");
            holder.dayReading.setText(String.valueOf("Null"));
            holder.nightReading.setText(String.valueOf("Null"));
            holder.gasReading.setText(String.valueOf("Null"));
        }
//    }

//        reading.setDate("2 2 2");
//        readingsList.add(reading);
//        reading.setUserEmail("email");
//        reading.setElecDay(12);
//        reading.setElecNight(23);
//        reading.setGas(34);
//
//        for(int i=0;i<=20;i++){
//            readingsList.add(reading);
//        }
    }

    @Override
    public int getItemCount() {
        return readingsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerID,date,dayReading,nightReading,gasReading;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            customerID = itemView.findViewById(R.id.cutomerId);
            date = itemView.findViewById(R.id.date);
            dayReading = itemView.findViewById(R.id.day);
            nightReading = itemView.findViewById(R.id.night);
            gasReading = itemView.findViewById(R.id.gas);

        }
    }
}

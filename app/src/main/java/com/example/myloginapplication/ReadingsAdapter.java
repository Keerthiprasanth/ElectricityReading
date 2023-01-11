package com.example.myloginapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myloginapplication.Model.Readings;

import java.util.List;

public class ReadingsAdapter extends RecyclerView.Adapter<ReadingsAdapter.ViewHolder>{
    Context context;
    List<Readings> readingsList;

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
        if(readingsList != null && readingsList.size() > 0){
            Readings model = readingsList.get(position);
            holder.customerID.setText(model.getUserEmail());
            holder.date.setText(model.getDate());
            holder.dayReading.setText(String.valueOf(model.getElecDay()));
            holder.nightReading.setText(String.valueOf(model.getElecNight()));
            holder.gasReading.setText(String.valueOf(model.getGas()));
        }else {
            return;
        }

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

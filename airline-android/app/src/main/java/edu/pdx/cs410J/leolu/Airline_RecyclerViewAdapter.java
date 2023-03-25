package edu.pdx.cs410J.leolu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Airline_RecyclerViewAdapter extends RecyclerView.Adapter<Airline_RecyclerViewAdapter.MyViewHolder> {
    ArrayList<AirlineModel> airlineModels;
    Context context;
    public Airline_RecyclerViewAdapter(Context context, ArrayList<AirlineModel> airlineModels){
        this.context = context;
        this.airlineModels = airlineModels;
    }
    @NonNull
    @Override
    public Airline_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating layout and giving a look to the rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_airline_row,parent,false);
        return new Airline_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Airline_RecyclerViewAdapter.MyViewHolder holder, int position) {
        //assigning values to the views created in the recycler_view_row layout file
        //based on the position of the recycler view
        holder.airlineNameView.setText(airlineModels.get(position).getAirlineName());
    }

    @Override
    public int getItemCount() {
        //just how many items to list as rows
        return airlineModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView airlineNameView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            airlineNameView = itemView.findViewById(R.id.airlineName);
        }
    }
}

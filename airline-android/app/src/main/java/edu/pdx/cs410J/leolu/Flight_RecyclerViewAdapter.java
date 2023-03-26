package edu.pdx.cs410J.leolu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Flight_RecyclerViewAdapter extends RecyclerView.Adapter<Flight_RecyclerViewAdapter.MyViewHolder> {
    ArrayList<FlightModel> flightModels;
    Context context;
    public Flight_RecyclerViewAdapter(Context context, ArrayList<FlightModel> flightModels){
        this.context = context;
        this.flightModels = flightModels;
    }
    @NonNull
    @Override
    public Flight_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating layout and giving a look to the rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_flights_row,parent,false);
        return new Flight_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Flight_RecyclerViewAdapter.MyViewHolder holder, int position) {
        //assigning values to the views created in the recycler_view_row layout file
        //based on the position of the recycler view
        holder.flightNumberView.setText(flightModels.get(position).getFlightNumber());
        holder.sourceView.setText(flightModels.get(position).getSource());
        holder.sourceStringView.setText(flightModels.get(position).getSourceString());
        holder.destinationView.setText(flightModels.get(position).getDestination());
        holder.destinationStringView.setText(flightModels.get(position).getDestinationString());
        holder.departureTimeView.setText(flightModels.get(position).getDepartureTime());
        holder.departureDateView.setText(flightModels.get(position).getDepartureDate());
        holder.arrivalTimeView.setText(flightModels.get(position).getArrivalTime());
        holder.arrivalDateView.setText(flightModels.get(position).getArrivalDate());
        holder.durationView.setText(flightModels.get(position).getDuration());
    }

    public void setFilteredList(ArrayList<FlightModel> filteredList){
        this.flightModels = filteredList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        //just how many items to list as rows
        return flightModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView flightNumberView;
        TextView sourceView;
        TextView sourceStringView;
        TextView destinationView;
        TextView destinationStringView;

        TextView departureTimeView;
        TextView departureDateView;
        TextView arrivalTimeView;
        TextView arrivalDateView;
        TextView durationView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            flightNumberView = itemView.findViewById(R.id.flightNumber);
            sourceView = itemView.findViewById(R.id.source);
            sourceStringView = itemView.findViewById(R.id.sourceString);
            destinationView = itemView.findViewById(R.id.destination);
            destinationStringView = itemView.findViewById(R.id.destinationString);
            departureTimeView = itemView.findViewById(R.id.departureTime);
            departureDateView = itemView.findViewById(R.id.departureDate);
            arrivalTimeView = itemView.findViewById(R.id.arrivalTime);
            arrivalDateView = itemView.findViewById(R.id.arrivalDate);
            durationView = itemView.findViewById(R.id.duration);
        }
    }
}

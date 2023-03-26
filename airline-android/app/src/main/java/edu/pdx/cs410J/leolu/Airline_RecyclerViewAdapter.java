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
    private ArrayList<AirlineModel> airlineModels;
    private ArrayList<AirlineModel> airlineModelsFull;
    Context context;
    private final Airline_RecyclerViewInterface recyclerViewInterface;
    public Airline_RecyclerViewAdapter(Context context, ArrayList<AirlineModel> airlineModels
            , Airline_RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.airlineModels = airlineModels;
        airlineModelsFull = new ArrayList<>(airlineModels);
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public Airline_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating layout and giving a look to the rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_airline_row,parent,false);
        return new Airline_RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Airline_RecyclerViewAdapter.MyViewHolder holder, int position) {
        //assigning values to the views created in the recycler_view_row layout file
        //based on the position of the recycler view
        holder.airlineNameView.setText(airlineModels.get(position).getAirlineName());
    }

    public void setFilteredList(ArrayList<AirlineModel> filteredList){
        this.airlineModels = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        //just how many items to list as rows
        return airlineModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView airlineNameView;
        public MyViewHolder(@NonNull View itemView, Airline_RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            airlineNameView = itemView.findViewById(R.id.airlineName);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){

                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(airlineNameView.getText().toString());
                        }
                    }
                }
            });
        }
    }
}

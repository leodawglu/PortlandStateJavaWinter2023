package edu.pdx.cs410J.leolu;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListAllFlightsActivity extends BaseAirlineActivity {
    private List<Flight> flights = new ArrayList<>();

    ArrayList<FlightModel> flightModels = new ArrayList<>();
    SearchView sourceSearchView;
    SearchView destinationSearchView;
    private Flight_RecyclerViewAdapter adapter;
    private String lastSRC="", lastDEST="";
    /**
     * Called when the activity is first created. Initializes the layout, populates the flight list,
     * sets up the RecyclerView, and initializes search views.
     *
     * @param savedInstanceState The saved state of the activity, if available.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_flights);
        // Get airline name from intent extra.
        String airlineName = getIntent().getStringExtra("Airline");
        // Set airline name in the layout.
        TextView airlineNameHolder = findViewById(R.id.airlineNameHolder);
        airlineNameHolder.setText(airlineName);
        // Populate the flight list and retrieve flights.
        populateAirlineList();
        flights = (List<Flight>) existingAirlineMap.get(airlineName.toLowerCase()).getFlights();
        // Set up RecyclerView for displaying flights.
        RecyclerView recyclerView = findViewById(R.id.flightsRecyclerView);
        setUpFlightModels();
        // Create and set the adapter for the RecyclerView.
        adapter = new Flight_RecyclerViewAdapter(this, flightModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Initialize search views for source and destination.
        initializeSearchView(sourceSearchView, findViewById(R.id.sourceSearchView), "src");
        initializeSearchView(destinationSearchView, findViewById(R.id.destinationSearchView), "dest");
    }

    /**
     * Initializes a SearchView widget, sets its behavior for text input changes, and associates it
     * with a specific type of search (source or destination).
     *
     * @param searchView The SearchView widget to be initialized and configured.
     * @param viewById The parent view containing the SearchView.
     * @param type The type of search (e.g., "src" for source, "dest" for destination).
     */
    private void initializeSearchView(SearchView searchView, View viewById, String type) {
        searchView = (SearchView) viewById;
        searchView.clearFocus();
        String travelDirection = type;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (travelDirection.equals("src")) lastSRC = newText;
                if (travelDirection.equals("dest")) lastDEST = newText;
                filterList();
                return true;
            }
        });
    }


    private void filterList() {
        ArrayList<FlightModel> filteredList = new ArrayList<>();
        for(FlightModel flight: flightModels){
            String src = flight.getSource().toLowerCase();
            String dest = flight.getDestination().toLowerCase();
            if(src.contains(lastSRC.toLowerCase()) && dest.contains(lastDEST.toLowerCase())){
                filteredList.add(flight);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No flights found", Toast.LENGTH_SHORT).show();
        }
        adapter.setFilteredList(filteredList);

    }


    private void setUpFlightModels(){
        if(flights==null || flights.size()==0){
            flightModels.add(new FlightModel("No Flights\nIn System", "",""
            ,"","","","",
                    ""));
            return;
        }
        for(Flight fl: flights){
            int mins = fl.getFlightDuration();
            int hrs = mins/60;
            mins %= 60;
            flightModels.add(new FlightModel(Integer.toString(fl.getNumber()),
                    fl.getSource(),
                    fl.getDestination(),
                    fl.getDepDate(),
                    fl.getDepTime24(),
                    fl.getArrDate(),
                    fl.getArrTime24(),
                    hrs+"HR"+" "+mins+"MIN"
            ));
        }

    }
}

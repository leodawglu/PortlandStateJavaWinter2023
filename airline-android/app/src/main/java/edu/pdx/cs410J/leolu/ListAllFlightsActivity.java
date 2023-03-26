package edu.pdx.cs410J.leolu;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.pdx.cs410J.ParserException;

public class ListAllFlightsActivity extends AppCompatActivity {

    private File appDir;
    private File airlinesDir;
    private Map<String,Airline> existingAirlineMap;
    private List<Flight> flights = new ArrayList<>();

    ArrayList<FlightModel> flightModels = new ArrayList<>();
    SearchView sourceSearchView;
    SearchView destinationSearchView;
    private Flight_RecyclerViewAdapter adapter;
    private String lastSRC="", lastDEST="";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_flights);

        String airlineName = getIntent().getStringExtra("Airline");
        TextView airlineNameHolder = findViewById(R.id.airlineNameHolder);
        airlineNameHolder.setText(airlineName);
        populateAirlineList();
        flights = (List<Flight>) existingAirlineMap.get(airlineName.toLowerCase()).getFlights();

        RecyclerView recyclerView = findViewById(R.id.flightsRecyclerView);
        setUpFlightModels();

        adapter = new Flight_RecyclerViewAdapter(this,
                flightModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initializeSearchView(sourceSearchView,findViewById(R.id.sourceSearchView), "src");
        initializeSearchView(destinationSearchView,findViewById(R.id.destinationSearchView),"dest");
    }

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
                if(travelDirection.equals("src"))lastSRC=newText;
                if(travelDirection.equals("dest"))lastDEST=newText;
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
        }else{
            adapter.setFilteredList(filteredList);
        }
    }


    private void setUpFlightModels(){
        if(flights==null || flights.size()==0){
            flightModels.add(new FlightModel("No Flights In System", "",""
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

    public boolean openAirlineFiles(){
        // Get a reference to the app's internal storage directory
        boolean isEmpty = false;
        this.appDir = getFilesDir();

// Create a directory called "airlines" if it doesn't already exist
        this.airlinesDir = new File(appDir, "airlines");
        if (!airlinesDir.exists()) {
            airlinesDir.mkdir();
        }
        if (airlinesDir.listFiles().length != 0) {
            // The airlines directory is not empty
            isEmpty = true;
        }
        return isEmpty;
    }

    private void populateAirlineList(){
        if(!openAirlineFiles()){
            return; //some message to SHOW EMPTY on VIEW
        }
        File[] airlineFiles = airlinesDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });
        existingAirlineMap = new HashMap<>();
        for(int i=0; i < airlineFiles.length; i++){
            try {
                Airline curr = new XmlParser(airlineFiles[i]).parse();
                existingAirlineMap.put(curr.getName().toLowerCase(),curr);
                System.out.println(curr.getName() +" : " +curr.toString());
            } catch (ParserException e) {
                System.err.println("XML file is null: " + airlineFiles[i].getName());
            }
        }
    }
}

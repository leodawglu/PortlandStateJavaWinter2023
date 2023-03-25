package edu.pdx.cs410J.leolu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_flights);

        String airlineName = getIntent().getStringExtra("Airline");
        populateAirlineList();
        flights = (List<Flight>) existingAirlineMap.get(airlineName.toLowerCase()).getFlights();

        RecyclerView recyclerView = findViewById(R.id.flightsRecyclerView);
        setUpFlightModels();

        Flight_RecyclerViewAdapter adapter = new Flight_RecyclerViewAdapter(this,
                flightModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpFlightModels(){
        if(flights==null || flights.size()==0){
            flightModels.add(new FlightModel("NULL", "XXX","XXX"
            ,"00/00/0000","23:59","00/00/0000","23:59",
                    "00HR00MIN"));
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

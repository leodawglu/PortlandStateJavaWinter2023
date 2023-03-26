package edu.pdx.cs410J.leolu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.pdx.cs410J.ParserException;

public class ListAllAirlinesActivity extends AppCompatActivity implements Airline_RecyclerViewInterface{

    private File appDir;
    private File airlinesDir;
    private Map<String,Airline> existingAirlineMap;
    private Airline_RecyclerViewAdapter adapter;

    ArrayList<AirlineModel> airlineModels = new ArrayList<>();
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_airlines);
        populateAirlineList();
        searchView = findViewById(R.id.airlineSearchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.airlineRecyclerView);
        setUpAirlineModels();

        adapter = new Airline_RecyclerViewAdapter(this,
                airlineModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void filterList(String queryText) {
        ArrayList<AirlineModel> filteredList = new ArrayList<>();
        for(AirlineModel airline: airlineModels){
             if(airline.getAirlineName().toLowerCase().contains(queryText.toLowerCase())){
                 filteredList.add(airline);
             }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No airline found with this name", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(filteredList);
        }
    }

    private void setUpAirlineModels(){
        if(existingAirlineMap == null){
            airlineModels.add(new AirlineModel("NO AIRLINE IN SYSTEM"));
            return;
        }
        for(Map.Entry<String, Airline> entry : existingAirlineMap.entrySet()){
            airlineModels.add(new AirlineModel(entry.getValue().getName()));
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

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ListAllFlightsActivity.class);
        intent.putExtra("Airline",airlineModels.get(position).getAirlineName());
        startActivity(intent);
    }
}

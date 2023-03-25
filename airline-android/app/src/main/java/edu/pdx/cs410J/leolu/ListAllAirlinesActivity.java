package edu.pdx.cs410J.leolu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.pdx.cs410J.ParserException;

public class ListAllAirlinesActivity extends AppCompatActivity {

    private File appDir;
    private File airlinesDir;
    private Map<String,Airline> existingAirlineMap;

    ArrayList<AirlineModel> airlineModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_airlines);

        RecyclerView recyclerView = findViewById(R.id.airlineRecyclerView);
        setUpAirlineModels();

        Airline_RecyclerViewAdapter adapter = new Airline_RecyclerViewAdapter(this,
                airlineModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
}

package edu.pdx.cs410J.leolu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.pdx.cs410J.ParserException;

public class MainActivity extends AppCompatActivity {

    private static final int CREATE_AIRLINE_REQUEST = 1;
    private static final int CREATE_FLIGHT_REQUEST = 2;
    private List<Airline> existingAirlines;
    private List<Airline> newAndModifiedAirlines = new ArrayList<>();
    private File appDir;
    private File airlinesDir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateAirlineList();
    }

    public void displayMsg(View view){
        Toast.makeText(this,"My first message", Toast.LENGTH_SHORT).show();
    }

    private void retrieveAllAirlinesFromXML(){
        /** TO DO
         * */
    }

    public void goToCreateAirlineActivity(View view) throws IOException {
        Intent intent = new Intent(this, CreateAirlineActivity.class);
        startActivity(intent);
    }

    public void goToCreateFlightActivity(View view) throws IOException {
        Intent intent = new Intent(this, CreateFlightActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveAirlineData();
    }

    private void saveAirlineData() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveAirlineData();
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
        if(openAirlineFiles()){
            return; //some message to SHOW EMPTY on VIEW
        }
        File[] airlineFiles = airlinesDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });

        existingAirlines = new ArrayList<>();
        for(int i=0; i < airlineFiles.length; i++){
            try {
                existingAirlines.add(new XmlParser(airlineFiles[i]).parse());
            } catch (ParserException e) {
                System.err.println("XML file is null: " + airlineFiles[i].getName());
            }
        }
    }


}
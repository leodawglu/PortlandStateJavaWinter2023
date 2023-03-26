package edu.pdx.cs410J.leolu;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import edu.pdx.cs410J.ParserException;

public class CreateAirlineActivity extends AppCompatActivity {
    TextView textView;
    EditText inputText;
    private Map<String,Airline> existingAirlineMap;
    private Map<String,Airline> newAirlineMap;

    private File appDir;
    private File airlinesDir;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_airline);
        textView = (TextView) findViewById(R.id.textView2);
        inputText = (EditText) findViewById(R.id.editTextTextAirlineName);
        newAirlineMap = new HashMap<>();
        populateAirlineList();

    }

    public void updateTextViewMessage(View view){
        String airlineName = String.valueOf(inputText.getText());
        if(airlineName==null || airlineName.length()==0){
            Toast.makeText(this, "Airline Name cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        if(airlineExists(airlineName)){
            textView.setText("This airline already exists!");
            Toast.makeText(this, "This airline name already exists, please use another airline name", Toast.LENGTH_SHORT).show();
            return;
        }
        createAirline(airlineName);
        textView.setText("New Airline created: \n" + inputText.getText().toString().toUpperCase());

        System.out.println("New Airline Successfully created: " + inputText.getText());
    }

    private void createAirline(String airlineName) {
        Airline newAirline = new Airline(airlineName);
        newAirlineMap.put(airlineName.toLowerCase(),newAirline);
    }

    private boolean airlineExists(String newAirlineName){
        if((existingAirlineMap!=null && existingAirlineMap.containsKey(newAirlineName.trim().toLowerCase())) ||
        newAirlineMap.containsKey(newAirlineName.toLowerCase()))
            return true;
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveAirlineData();
    }

    private void saveAirlineData() {
        XmlDumper dumper;

        for(Map.Entry<String, Airline> airline : newAirlineMap.entrySet()){
            String airlineName = airline.getKey().trim().replaceAll("[^a-zA-Z]+", "_");
            File airlineFile = new File(airlinesDir, airlineName +".xml");
            dumper = new XmlDumper(airlineFile);
            dumper.dump(airline.getValue());
            if(existingAirlineMap!=null)existingAirlineMap.put(airline.getKey(),airline.getValue());
        }
        newAirlineMap = new HashMap<>();
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

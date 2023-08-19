package edu.pdx.cs410J.leolu;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import edu.pdx.cs410J.ParserException;

public class BaseAirlineActivity extends AppCompatActivity {
  protected File airlinesDir;
  protected File appDir;
  protected Map<String,Airline> existingAirlineMap;
  protected Map<String,Airline> newAirlineMap;

  private boolean openAirlineFiles(){
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

  protected void populateAirlineList(){
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

  protected void saveAirlineData() {
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
}

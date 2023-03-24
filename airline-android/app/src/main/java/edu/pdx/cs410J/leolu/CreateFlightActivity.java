package edu.pdx.cs410J.leolu;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.pdx.cs410J.ParserException;

public class CreateFlightActivity extends AppCompatActivity {
    //TextView textView;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    EditText editAirlineName=null, editFlightNumber=null, editDepCode=null,
            editDepDate=null, editDepTime=null, editArrCode=null,
            editArrDate=null, editArrTime=null;
    private Map<String,Airline> existingAirlineMap;
    private Map<String,Airline> newAirlineMap;

    private Calendar depDate = Calendar.getInstance();
    private Calendar arrDate = Calendar.getInstance();
    private File appDir;
    private File airlinesDir;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_flight);
        //textView = (TextView) findViewById(R.id.textView2);
        editAirlineName = (EditText) findViewById(R.id.editTextAirlineName);
        editFlightNumber = (EditText) findViewById(R.id.editTextFlightNumber);
        editDepCode = (EditText) findViewById(R.id.editTextDepCode);
        editDepDate = (EditText) findViewById(R.id.editTextDepDate);
        departureSelectDate();
        editDepTime = (EditText) findViewById(R.id.editTextDepTime);
        editArrCode = (EditText) findViewById(R.id.editTextArrCode);
        editArrDate = (EditText) findViewById(R.id.editTextArrDate);
        arrivalSelectDate();
        editArrTime = (EditText) findViewById(R.id.editTextArrTime);


        newAirlineMap = new HashMap<>();
        populateAirlineList();

    }

    /*
    public void updateTextViewMessage(View view){
        String airlineName = String.valueOf(inputText.getText());
        if(airlineName==null || airlineName.length()==0){
            Toast.makeText(this, "Airline Name cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        if(airlineExists(airlineName)){
            Toast.makeText(this, "This airline name already exists", Toast.LENGTH_SHORT).show();
            return;
        }
        createAirline(airlineName);
        textView.setText("New Airline created: " + inputText.getText().toString().toUpperCase());

        System.out.println("New Airline Successfully created: " + inputText.getText());
    }
*/
    public void departureSelectDate(){
        selectDate(editDepDate,depDate);
    }

    public void arrivalSelectDate(){
        selectDate(editArrDate,arrDate);
    }

    private void selectDate(EditText pickDate, Calendar depOrArrCalendar){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                depOrArrCalendar.set(Calendar.YEAR,year);
                depOrArrCalendar.set(Calendar.MONTH,month);
                depOrArrCalendar.set(Calendar.DAY_OF_MONTH,day);
                pickDate.setText(updateDate(pickDate));
            }
        };
        pickDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new DatePickerDialog(CreateFlightActivity.this,date,depOrArrCalendar.get(Calendar.MONTH)
                        ,depOrArrCalendar.get(Calendar.DAY_OF_MONTH), depOrArrCalendar.get(Calendar.YEAR));
            }
        });
    }

    private String updateDate(EditText pickDate) {
        return dateFormat.format(pickDate.getText());
    }

    private void createAirline(String airlineName) {
        Airline newAirline = new Airline(airlineName);
        newAirlineMap.put(airlineName.toLowerCase(),newAirline);
    }

    private boolean airlineExists(String newAirlineName){
        if(existingAirlineMap.containsKey(newAirlineName.toLowerCase()) ||
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
            String airlineName = airline.getKey().replaceAll("[^a-zA-Z]+", "_").trim();
            File airlineFile = new File(airlinesDir, airlineName +".xml");
            dumper = new XmlDumper(airlineFile);
            dumper.dump(airline.getValue());
            existingAirlineMap.put(airline.getKey(),airline.getValue());
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

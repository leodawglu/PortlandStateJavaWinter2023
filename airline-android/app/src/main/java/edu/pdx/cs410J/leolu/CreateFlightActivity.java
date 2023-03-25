package edu.pdx.cs410J.leolu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

public class CreateFlightActivity extends AppCompatActivity {
    //TextView textView;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    EditText editAirlineName=null, editFlightNumber=null, editDepCode=null,
            editDepDate=null, editDepTime=null, editArrCode=null,
            editArrDate=null, editArrTime=null;
    private Map<String,Airline> existingAirlineMap;
    private Map<String,Airline> newAirlineMap;

    private final Calendar depCal = Calendar.getInstance();
    private final Calendar arrCal = Calendar.getInstance();
    private File appDir;
    private File airlinesDir;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_flight);
        editAirlineName = (EditText) findViewById(R.id.editTextAirlineName);
        editFlightNumber = (EditText) findViewById(R.id.editTextFlightNumber);
        editDepCode = (EditText) findViewById(R.id.editTextDepCode);
        editDepDate = (EditText) findViewById(R.id.editTextDepDate);
        departureSelectDate();
        editDepTime = (EditText) findViewById(R.id.editTextDepTime);
        editDepTime.setOnClickListener(v->selectTime(editDepTime,depCal));
        editArrCode = (EditText) findViewById(R.id.editTextArrCode);
        editArrDate = (EditText) findViewById(R.id.editTextArrDate);
        arrivalSelectDate();
        editArrTime = (EditText) findViewById(R.id.editTextArrTime);
        editArrTime.setOnClickListener(v->selectTime(editArrTime,arrCal));

        newAirlineMap = new HashMap<>();
        populateAirlineList();

    }
    public void departureSelectDate(){
        selectDate(editDepDate, depCal);
    }

    public void arrivalSelectDate(){
        selectDate(editArrDate, arrCal);
    }

    private void selectDate(EditText editText, Calendar depOrArrCalendar){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                depOrArrCalendar.set(Calendar.YEAR,year);
                depOrArrCalendar.set(Calendar.MONTH,month);
                depOrArrCalendar.set(Calendar.DAY_OF_MONTH,day);
                //editText.setText(updateDate(editText));
                updateDateInputLabel(editText,depOrArrCalendar);
            }
        };
        editText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new DatePickerDialog(CreateFlightActivity.this,date
                        ,depOrArrCalendar.get(Calendar.YEAR)
                        ,depOrArrCalendar.get(Calendar.MONTH)
                        ,depOrArrCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateDateInputLabel(EditText editText, Calendar calendar){
        editText.setText(dateFormat.format(calendar.getTime()));
    }

    private void selectTime(EditText editTime, Calendar calendar){
        int hr = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(CreateFlightActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hr, int min) {
                calendar.set(Calendar.HOUR_OF_DAY,hr);
                calendar.set(Calendar.MINUTE,min);

                editTime.setText(timeFormat.format(calendar.getTime()));
            }
        }, hr, min, true);
        tpd.setTitle("Select Time");
        tpd.show();
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

    public boolean allInputsNotEmpty(View view){
        boolean notEmpty = true;

        if (TextUtils.isEmpty(editAirlineName.getText().toString())) {
            editAirlineName.setError("Please enter an Airline Name");
            notEmpty = false;
        }
        if (TextUtils.isEmpty(editFlightNumber.getText().toString())) {
            editFlightNumber.setError("Please enter a Flight Number");
            notEmpty = false;
        }
        if (TextUtils.isEmpty(editDepCode.getText().toString())) {
            editDepCode.setError("Please enter an IATA Airport Code");
            notEmpty = false;
        }
        if (TextUtils.isEmpty(editDepDate.getText().toString())) {
            editDepDate.setError("Please select a date");
            notEmpty = false;
        }
        if (TextUtils.isEmpty(editDepTime.getText().toString())) {
            editDepTime.setError("Please select a time");
            notEmpty = false;
        }
        if (TextUtils.isEmpty(editArrCode.getText().toString())) {
            editArrCode.setError("Please enter an IATA Airport Code");
            notEmpty = false;
        }
        if (TextUtils.isEmpty(editArrDate.getText().toString())) {
            editArrDate.setError("Please select a date");
            notEmpty = false;
        }
        if (TextUtils.isEmpty(editArrTime.getText().toString())) {
            editArrTime.setError("Please select a time");
            notEmpty = false;
        }
        return notEmpty;
    }

    /**
     * @param input - airport code String to be checked if real
     * @return boolean true when airport code is from a real airport
     * Utilizes {@code AirportNames.getNamesMap()}
     * */
    private boolean isValidIATACode(String input){
        return AirportNames.getNamesMap().containsKey(input);
    }

    public void createFlight(View view){
        boolean good = true;
        if(!allInputsNotEmpty(view))return;
        if(!isValidIATACode(editDepCode.getText().toString())) {
            good = false;
            editDepCode.setError("Please enter a Valid IATA Code");
        }
        if(!isValidIATACode(editArrCode.getText().toString())) {
            good = false;
            editArrCode.setError("Please enter a Valid IATA Code");
        }
        if(arrCal.getTimeInMillis()<depCal.getTimeInMillis()){
            good = false;
            editDepDate.setError("Departure Date & Time cannot be after Arrival");
            editDepTime.setError("Departure Date & Time cannot be after Arrival");
            editArrDate.setError("Arrival Date & Time cannot be before Departure");
            editArrTime.setError("Arrival Date & Time cannot be before Departure");
            Toast.makeText(this,"Departure Date & Time cannot occur after Arrival",Toast.LENGTH_SHORT).show();
        }
        if(!good)return;
        Flight fl = new Flight(
                editFlightNumber.getText().toString(),
                editDepCode.getText().toString(),
                dateFormat.format(depCal.getTime()),
                timeFormat.format(depCal.getTime()),
                editArrCode.getText().toString(),
                dateFormat.format(arrCal.getTime()),
                timeFormat.format(arrCal.getTime()),
                true
        );
        String airlineName = editAirlineName.getText().toString();
        Airline airline;
        if(newAirlineMap.containsKey(airlineName.toLowerCase())){
            airline = newAirlineMap.get(airlineName.toLowerCase());
        }
        else if(existingAirlineMap.containsKey(airlineName.toLowerCase())){
            airline = existingAirlineMap.get(airlineName.toLowerCase());
        }else{
            airline = new Airline(airlineName);
        }

        airline.addFlight(fl);
        newAirlineMap.put(airlineName.toLowerCase(),airline);
        Toast.makeText(this,"New Flight Created",Toast.LENGTH_SHORT).show();
    }
}

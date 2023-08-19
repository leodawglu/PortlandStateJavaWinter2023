package edu.pdx.cs410J.leolu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

    /**
     * Called when the activity is starting. Initializes UI components, sets up listeners,
     * and prepares the necessary data for creating a flight.
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
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

    /**
     * Opens a DatePickerDialog to allow the user to select a departure date and updates the UI accordingly.
     */
    public void departureSelectDate(){
        selectDate(editDepDate, depCal);
    }

    /**
     * Opens a DatePickerDialog to allow the user to select an arrival date and updates the UI accordingly.
     */
    public void arrivalSelectDate(){
        selectDate(editArrDate, arrCal);
    }

    /**
     * Opens a DatePickerDialog for selecting a date and updates the UI with the selected date.
     *
     * @param editText The EditText field that will display the selected date.
     * @param depOrArrCalendar The Calendar object associated with the selected date.
     */
    private void selectDate(EditText editText, Calendar depOrArrCalendar){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                depOrArrCalendar.set(Calendar.YEAR,year);
                depOrArrCalendar.set(Calendar.MONTH,month);
                depOrArrCalendar.set(Calendar.DAY_OF_MONTH,day);
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

    /**
     * Updates the label of an EditText field with the selected date.
     *
     * @param editText The EditText field to update with the selected date.
     * @param calendar The Calendar object containing the selected date.
     */
    private void updateDateInputLabel(EditText editText, Calendar calendar){
        editText.setText(dateFormat.format(calendar.getTime()));
    }

    /**
     * Opens a TimePickerDialog to allow the user to select a time and updates the UI accordingly.
     *
     * @param editTime The EditText field that will display the selected time.
     * @param calendar The Calendar object associated with the selected time.
     */
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

    /**
     * Checks whether an airline with the given name exists in either the existing airline map
     * or the new airline map.
     *
     * @param newAirlineName The name of the airline to check.
     * @return True if an airline with the given name exists, false otherwise.
     */
    private boolean airlineExists(String newAirlineName){
        if((existingAirlineMap!=null && existingAirlineMap.containsKey(newAirlineName.toLowerCase())) ||
                newAirlineMap.containsKey(newAirlineName.toLowerCase()))
            return true;
        return false;
    }

    /**
     * Called when the activity is being paused. Saves the airline data to XML files.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveAirlineData();
    }

    /**
     * Saves the airline data to XML files.
     */
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

    /**
     * Called when the activity is being stopped. Saves the airline data to XML files.
     */
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

    /**
     * Checks if all input fields are not empty and valid.
     *
     * @param view The View associated with the input fields.
     * @return True if all input fields are not empty and valid, false otherwise.
     */
    public boolean allInputsNotEmpty(View view) {
        boolean allInputsValid = true;

        allInputsValid &= validateInputNotEmpty(editAirlineName, "Please enter an Airline Name");
        allInputsValid &= validateInputNotEmpty(editFlightNumber, "Please enter a Flight Number");
        allInputsValid &= validateInputNotEmpty(editDepCode, "Please enter an IATA Airport Code");
        allInputsValid &= validateInputNotEmpty(editDepDate, "Please select a date");
        allInputsValid &= validateInputNotEmpty(editDepTime, "Please select a time");
        allInputsValid &= validateInputNotEmpty(editArrCode, "Please enter an IATA Airport Code");
        allInputsValid &= validateInputNotEmpty(editArrDate, "Please select a date");
        allInputsValid &= validateInputNotEmpty(editArrTime, "Please select a time");

        return allInputsValid;
    }

    /**
     * Validates an input field to ensure it is not empty and sets an error message if needed.
     *
     * @param editText The EditText field to validate.
     * @param errorMessage The error message to set if the input is empty.
     * @return True if the input field is not empty, false otherwise.
     */
    private boolean validateInputNotEmpty(EditText editText, String errorMessage) {
        String inputValue = editText.getText().toString();
        if (TextUtils.isEmpty(inputValue)) {
            editText.setError(errorMessage);
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    /**
     * Handles the creation of a new flight based on user inputs, performs validations, and updates the UI accordingly.
     *
     * @param view The View associated with the method call.
     */
    public void createFlight(View view){
        if(!allInputsNotEmpty(view))return;
        if (!isValidAirportCode(editDepCode)) return;
        if (!isValidAirportCode(editArrCode)) return;
        if (!isDepartureTimeIsBeforeArrivalTime()) return;

        Flight fl = getFlight();
        if (fl == null) return;

        String airlineName = editAirlineName.getText().toString().trim();
        Airline airline = getAirline(airlineName);

        airline.addFlight(fl);
        newAirlineMap.put(airlineName.toLowerCase(),airline);
        Toast.makeText(this,"New Flight Created",Toast.LENGTH_SHORT).show();
        goToFlightCreatedSuccess(airlineName, fl);
    }

    /**
     * Checks if departure time is before arrival time and displays error messages if not.
     *
     * @return True if departure time is before arrival time, false otherwise.
     */
    private boolean isDepartureTimeIsBeforeArrivalTime() {
        if(!Helper.departureBeforeArrival(depCal.getTimeInMillis(), arrCal.getTimeInMillis())){
            editDepDate.setError("Departure Date & Time must be before Arrival");
            editDepTime.setError("Departure Date & Time must be before Arrival");
            editArrDate.setError("Arrival Date & Time must be after Departure");
            editArrTime.setError("Arrival Date & Time must be after Departure");
            Toast.makeText(this,"Departure Date & Time must be before Arrival Date & Time",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     * Validates an airport code input field and displays an error message if invalid.
     *
     * @param editText The EditText field containing the airport code.
     * @return True if the airport code is valid, false otherwise.
     */
    private boolean isValidAirportCode(EditText editText) {
        String airportCode = editText.getText().toString().toUpperCase();
        if (!Helper.isRealAirportCode(airportCode)) {
            editText.setError("Please enter a Valid IATA Code");
            return false;
        }
        return true;
    }

    /**
     * Retrieves an Airline instance based on the provided airline name.
     *
     * @param airlineName The name of the airline.
     * @return An Airline instance associated with the provided name.
     */
    private Airline getAirline(String airlineName) {
        Airline airline;
        if(newAirlineMap.containsKey(airlineName.toLowerCase())){
            airline = newAirlineMap.get(airlineName.toLowerCase());
        }else if(existingAirlineMap.containsKey(airlineName.toLowerCase())){
            airline = existingAirlineMap.get(airlineName.toLowerCase());
        }else{
            airline = new Airline(airlineName);
        }
        return airline;
    }

    /**
     * Constructs a Flight instance based on user input and handles potential exceptions.
     *
     * @return The constructed Flight instance, or null if an exception occurs.
     */
    private Flight getFlight() {
        Flight fl;
        try {
            fl = new Flight(
                    editFlightNumber.getText().toString(),
                    editDepCode.getText().toString(),
                    dateFormat.format(depCal.getTime()),
                    timeFormat.format(depCal.getTime()),
                    editArrCode.getText().toString(),
                    dateFormat.format(arrCal.getTime()),
                    timeFormat.format(arrCal.getTime()),
                    true
            );
        } catch (Exception e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            return null;
        }
        return fl;
    }

    /**
     * Navigates to the FlightCreatedActivity to display information about the newly created flight.
     *
     * @param airlineName The name of the airline associated with the new flight.
     * @param newFlight The newly created Flight instance.
     */
    private void goToFlightCreatedSuccess(String airlineName, Flight newFlight){
        int mins = newFlight.getFlightDuration();
        int hrs = mins/60;
        mins %= 60;
        Intent intent = new Intent(this, FlightCreatedActivity.class);
        intent.putExtra("Airline",airlineName);
        intent.putExtra("FlightNumber",newFlight.getNumber());
        intent.putExtra("Source",newFlight.getSource());
        intent.putExtra("Destination",newFlight.getDestination());
        intent.putExtra("DepartureTime",timeFormat.format(depCal.getTime()));
        intent.putExtra("DepartureDate",dateFormat.format(depCal.getTime()));
        intent.putExtra("ArrivalTime",timeFormat.format(arrCal.getTime()));
        intent.putExtra("ArrivalDate",dateFormat.format(arrCal.getTime()));
        intent.putExtra("Duration",hrs+"HR"+" "+mins+"MIN");
        intent.putExtra("SourceString",AirportNames.getNamesMap().get(newFlight.getSource().toUpperCase()).replace(", ", "\n"));
        intent.putExtra("DestinationString",AirportNames.getNamesMap().get(newFlight.getDestination().toUpperCase()).replace(", ", "\n"));
        startActivity(intent);
    }
}

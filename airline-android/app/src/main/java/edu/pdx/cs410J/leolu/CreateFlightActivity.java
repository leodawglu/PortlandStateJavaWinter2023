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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import edu.pdx.cs410J.AirportNames;

public class CreateFlightActivity extends BaseAirlineActivity {
    //TextView textView;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    EditText editAirlineName=null, editFlightNumber=null, editDepCode=null,
            editDepDate=null, editDepTime=null, editArrCode=null,
            editArrDate=null, editArrTime=null;

    private final Calendar depCal = Calendar.getInstance();
    private final Calendar arrCal = Calendar.getInstance();

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
        if((existingAirlineMap!=null && existingAirlineMap.containsKey(newAirlineName.toLowerCase())) ||
                newAirlineMap.containsKey(newAirlineName.toLowerCase()))
            return true;
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveAirlineData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveAirlineData();
    }

    public boolean allInputsNotEmpty(View view){
        boolean notEmpty = true;

        if (TextUtils.isEmpty(editAirlineName.getText().toString())) {
            editAirlineName.setError("Please enter an Airline Name");
            notEmpty = false;
        }else {
            editAirlineName.setError(null);
        }
        if (TextUtils.isEmpty(editFlightNumber.getText().toString())) {
            editFlightNumber.setError("Please enter a Flight Number");
            notEmpty = false;
        }else {
            editFlightNumber.setError(null);
        }
        if (TextUtils.isEmpty(editDepCode.getText().toString())) {
            editDepCode.setError("Please enter an IATA Airport Code");
            notEmpty = false;
        }else {
            editDepCode.setError(null);
        }
        if (TextUtils.isEmpty(editDepDate.getText().toString())) {
            editDepDate.setError("Please select a date");
            notEmpty = false;
        }else {
            editDepDate.setError(null);
        }
        if (TextUtils.isEmpty(editDepTime.getText().toString())) {
            editDepTime.setError("Please select a time");
            notEmpty = false;
        }else {
            editDepTime.setError(null);
        }
        if (TextUtils.isEmpty(editArrCode.getText().toString())) {
            editArrCode.setError("Please enter an IATA Airport Code");
            notEmpty = false;
        }else {
            editArrCode.setError(null);
        }
        if (TextUtils.isEmpty(editArrDate.getText().toString())) {
            editArrDate.setError("Please select a date");
            notEmpty = false;
        } else {
            editArrDate.setError(null);
        }
        if (TextUtils.isEmpty(editArrTime.getText().toString())) {
            editArrTime.setError("Please select a time");
            notEmpty = false;
        } else {
            editArrTime.setError(null);
        }
        return notEmpty;
    }

    public void createFlight(View view){
        if(!allInputsNotEmpty(view))return;
        if(!Helper.isRealAirportCode(editDepCode.getText().toString().toUpperCase())) {
            editDepCode.setError("Please enter a Valid IATA Code");
            return;
        }
        if(!Helper.isRealAirportCode(editArrCode.getText().toString().toUpperCase())) {
            editArrCode.setError("Please enter a Valid IATA Code");
            return;
        }
        if(!Helper.departureBeforeArrival(depCal.getTimeInMillis(), arrCal.getTimeInMillis())){
            editDepDate.setError("Departure Date & Time must be before Arrival");
            editDepTime.setError("Departure Date & Time must be before Arrival");
            editArrDate.setError("Arrival Date & Time must be after Departure");
            editArrTime.setError("Arrival Date & Time must be after Departure");
            Toast.makeText(this,"Departure Date & Time must be before Arrival Date & Time",Toast.LENGTH_SHORT).show();
            return;
        }
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
            return;
        }

        String airlineName = editAirlineName.getText().toString().trim();
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
        goToFlightCreatedSuccess(airlineName, fl);
    }

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

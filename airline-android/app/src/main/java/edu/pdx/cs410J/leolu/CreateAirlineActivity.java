package edu.pdx.cs410J.leolu;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class CreateAirlineActivity extends BaseAirlineActivity {
    TextView textView;
    EditText inputText;

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

    @Override
    protected void onStop() {
        super.onStop();
        saveAirlineData();
    }
}

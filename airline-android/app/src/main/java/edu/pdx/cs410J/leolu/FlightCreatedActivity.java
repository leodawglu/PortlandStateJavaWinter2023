package edu.pdx.cs410J.leolu;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FlightCreatedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_created_success_dialog);
        String airlineName = getIntent().getStringExtra("Airline");
        String flightNumber = getIntent().getStringExtra("FlightNumber");
        String source = getIntent().getStringExtra("Source");
        String destination = getIntent().getStringExtra("Destination");
        String departureTime = getIntent().getStringExtra("DepartureTime");
        String departureDate = getIntent().getStringExtra("DepartureDate");
        String arrivalTime = getIntent().getStringExtra("ArrivalTime");
        String arrivalDate = getIntent().getStringExtra("ArrivalDate");
        String duration = getIntent().getStringExtra("Duration");
        String sourceString = getIntent().getStringExtra("SourceString");
        String destinationString = getIntent().getStringExtra("DestinationString");

        TextView airlineNameTextView = findViewById(R.id.airlineNameText);
        airlineNameTextView.setText(airlineName);
        TextView flightNumberView = findViewById(R.id.flightNumber);
        flightNumberView.setText(flightNumber);
        TextView sourceView = findViewById(R.id.source);
        sourceView.setText(source);
        TextView destinationView = findViewById(R.id.destination);
        destinationView.setText(destination);
        TextView departureTimeView = findViewById(R.id.departureTime);
        departureTimeView.setText(departureTime);
        TextView departureDateView = findViewById(R.id.departureDate);
        departureDateView.setText(departureDate);
        TextView arrivalTimeView = findViewById(R.id.arrivalTime);
        arrivalTimeView.setText(arrivalTime);
        TextView arrivalDateView = findViewById(R.id.arrivalDate);
        arrivalDateView.setText(arrivalDate);
        TextView durationView = findViewById(R.id.duration);
        durationView.setText(duration);
        TextView sourceStringView = findViewById(R.id.sourceString);
        sourceStringView.setText(sourceString);
        TextView destinationStringView = findViewById(R.id.destinationString);
        destinationStringView.setText(destinationString);
    }
}

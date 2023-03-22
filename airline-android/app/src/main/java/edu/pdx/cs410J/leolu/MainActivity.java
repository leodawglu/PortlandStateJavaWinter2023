package edu.pdx.cs410J.leolu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CREATE_AIRLINE_REQUEST = 1;
    private static final int CREATE_FLIGHT_REQUEST = 2;
    List<Airline> airlines = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(airlines);
        oos.flush();
        byte[] bytes = bos.toByteArray();
        intent.putExtra("airlineList", bytes);

        //intent.putExtra("airlineList", airlines.toArray(new Airline[airlines.size()]));
        startActivity(intent);
    }


}
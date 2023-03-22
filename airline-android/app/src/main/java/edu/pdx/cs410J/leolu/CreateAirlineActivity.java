package edu.pdx.cs410J.leolu;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateAirlineActivity extends AppCompatActivity {
    TextView textView;
    EditText inputText;
    List<Airline> airlines;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_airline);
        textView = (TextView) findViewById(R.id.textView2);
        inputText = (EditText) findViewById(R.id.editTextTextAirlineName);
        //Airline[] airlineArr = getIntent().getParcelableArrayExtra("airlineList");
        byte[] bytes = getIntent().getByteArrayExtra("myList");
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bis);
            airlines = (ArrayList<Airline>) ois.readObject();
        }catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateTextViewMessage(View view){
        textView.setText("New Airline created: " + inputText.getText());

        System.out.println("New Airline Successfully created: " + inputText.getText());
    }
}

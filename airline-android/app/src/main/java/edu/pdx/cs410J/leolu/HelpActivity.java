package edu.pdx.cs410J.leolu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

    }

    public void goToReadMe(View view) throws IOException {
        Intent intent = new Intent(this, ReadMeActivity.class);
        startActivity(intent);
    }
}

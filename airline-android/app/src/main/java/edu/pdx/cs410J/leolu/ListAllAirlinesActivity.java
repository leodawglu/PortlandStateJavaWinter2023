package edu.pdx.cs410J.leolu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ListAllAirlinesActivity extends BaseAirlineActivity implements Airline_RecyclerViewInterface{

    private Airline_RecyclerViewAdapter adapter;

    ArrayList<AirlineModel> airlineModels = new ArrayList<>();
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_airlines);
        populateAirlineList();
        initializeSearchView();

        RecyclerView recyclerView = findViewById(R.id.airlineRecyclerView);
        setUpAirlineModels();

        adapter = new Airline_RecyclerViewAdapter(this,
                airlineModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeSearchView() {
        searchView = findViewById(R.id.airlineSearchView);
        searchView.clearFocus();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchView.setIconified(false);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String queryText) {
        ArrayList<AirlineModel> filteredList = new ArrayList<>();
        for(AirlineModel airline: airlineModels){
             if(airline.getAirlineName().toLowerCase().contains(queryText.toLowerCase())){
                 filteredList.add(airline);
             }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No airline found with this name", Toast.LENGTH_SHORT).show();
        }
        Collections.sort(filteredList,nameComparator);
        adapter.setFilteredList(filteredList);

    }

    private void setUpAirlineModels(){
        if(existingAirlineMap == null){
            airlineModels.add(new AirlineModel("NO AIRLINE IN SYSTEM"));
            return;
        }
        for(Map.Entry<String, Airline> entry : existingAirlineMap.entrySet()){
            airlineModels.add(new AirlineModel(entry.getValue().getName()));
        }
        Collections.sort(airlineModels,nameComparator);
    }
    Comparator<AirlineModel> nameComparator = new Comparator<AirlineModel>() {
        @Override
        public int compare(AirlineModel o1, AirlineModel o2) {
            return o1.getAirlineName().compareTo(o2.getAirlineName());
        }
    };

    @Override
    public void onItemClick(String airlineName) {
        Intent intent = new Intent(this, ListAllFlightsActivity.class);
        Airline selectedAirline = existingAirlineMap.get(airlineName.toLowerCase());
        intent.putExtra("Airline",selectedAirline.getName());
        startActivity(intent);
    }
}

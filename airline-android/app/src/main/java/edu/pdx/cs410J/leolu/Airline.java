/**
 * The {@code Airline} class is designed to create an airline
 * with a String name, and a List of flights.
 * It validates the name to be a non-null,
 * not empty String before setting the name
 *
 * This implementation also includes a constructor that allows
 * an airline to be created with a List of existing flights.
 * The List of flights are validated to not contain
 * any null flights, but can be an empty list, before being created.
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.pdx.cs410J.AbstractAirline;

public class Airline extends AbstractAirline<Flight> implements Serializable {
  private final String name;
  private List<Flight> flights;
  private String error="";
  private static final long serialVersionUID = 1L;


  /**
   * Constructs a new instance of <code>Airline</code> with a given name
   * and sets a new empty ArrayList for flights {@link Flight}
   * @param name Airline Name
   * */
  public Airline(String name){
    validateAirlineName(name);
    this.name = name;
    flights = new ArrayList<Flight>();
  }

  /**
   * Constructs a new instance of <code>Airline</code> with a given name
   * and a given ArrayList of flights
   * @param name Airline Name
   * @param flights ArrayList of flights of Flight class {@link Flight}
   * */
  public Airline(String name, ArrayList<Flight> flights){
    validateAirlineName(name);
    validateFlightList(flights);

    this.name = name;
    this.flights = flights;
  }

  /**
   * @return Airline name
   * */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Adds a flight to the ArrayList
   * Validates that the flight is not null before adding
   * @param flight Flight that is to be added
   * @throws  NullPointerException if flight is null
   * */
  @Override
  public void addFlight(Flight flight) {
    try{
      if(flight == null) throw new NullPointerException("Cannot add a null flight!");
    }catch(Exception e) {
      error = e.getMessage();
      System.out.println(e.getMessage());
    }
    this.flights.add(flight);
  }

  /**
   * @return  ArrayList flights
   * */
  @Override
  public Collection<Flight> getFlights() {
    Collections.sort(flights);
    return flights;
  }

  /**
   * Validates an ArrayList of flights
   * @param flights takes an ArrayList<Flight> to validate contents
   * @throws  NullPointerException when ArrayList is null
   * @throws NullPointerException when a flight within the list is null
   * */
  private void validateFlightList(ArrayList<Flight> flights){
    try{
      if(flights == null)
        throw new NullPointerException("Flight list is null");

      for(Flight fl: flights){
        if(fl==null)
          throw new NullPointerException("Null flight found in flights list");
      }
    }catch(NullPointerException e){
      error = e.getMessage();
      System.out.println(error);
    }
  }

  /**
   * Validates input String
   * Exits program if airline name is not valid
   * @param name Airline name to be validated
   * @throws NullPointerException when name is null or is length 0
   * */
  private void validateAirlineName(String name){
    try{
      if(name==null || name.length()==0){
        throw new NullPointerException();
      }
    }catch(NullPointerException e){
      error = "Airline name cannot be empty";
      System.out.println(error);
    }
  }

  /**
   * Returns error messaged store for unit tests
   * */
  public String getError(){
    return error;
  }
}

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

import edu.pdx.cs410J.AbstractAirline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Airline extends AbstractAirline<Flight> {
  private final String name;
  private List<Flight> flights;
  private String error="";


  /**
   * Constructs a new instance of <code>Airline</code> with a given name
   * and sets a new empty ArrayList for flights {@link Flight}
   * @param name Airline Name
   * */
  public Airline(String name){
    try{
      validateAirlineName(name);
    }catch(NullPointerException e) {
      error = e.getMessage();
      System.err.println(e);
    }
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
    try{
      validateAirlineName(name);
      validateFlightList(flights);
    }catch(NullPointerException e) {
      error = e.getMessage();
      System.err.println(e.getMessage());
    }

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
    }catch(NullPointerException e){
      error = e.getMessage();
      System.err.print(e.getMessage());
    }
    this.flights.add(flight);
  }

  /**
   * @return  ArrayList flights
   * */
  @Override
  public Collection<Flight> getFlights() {
    return flights;
  }

  /**
   * Validates an ArrayList of flights
   * @throws NullPointerException when the ArrayList is null or any of the flights are null
   * */

  private static void validateFlightList(ArrayList<Flight> flights) throws NullPointerException{
    if(flights == null)throw new NullPointerException("Flight list is null");
    for(Flight f: flights){
      if(f==null) throw new NullPointerException("Null flight found in flights list");
    }
  }

  /**
   * Validates input String
   * @throws NullPointerException when the String is null or length is 0
   * */

  private static void validateAirlineName(String name) throws NullPointerException{
    if(name == null) throw new NullPointerException("Airline name cannot be null");
    if(name.length()==0) throw new NullPointerException("Airline name cannot be empty!");
  }

  public String getError(){
    return error;
  }
}

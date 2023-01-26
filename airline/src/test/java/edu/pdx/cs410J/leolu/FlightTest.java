/**
 * The {@code FlightTest} class contains unit tests for the {@link edu.pdx.cs410J.leolu.Flight} class
 *
 *
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Flight} class.
 *
 * You'll need to update these unit tests as you build out you program.
 */
public class FlightTest {

  @Test
  void forProject1ItIsOkayIfGetDepartureTimeReturnsNull() {
    Flight flight = new Flight();
    assertThat(flight.getDeparture(), is(nullValue()));
  }

  @Test
  void nullFlightNumberPrintsNullError(){
    Flight nulls = new Flight(null,"SEA","01/23/2023","23:40","PDX","01/23/2023", "23:40");
    assertThat(nulls.getError(), equalTo("Flight Number cannot be null."));
  }

  @Test
  void inputFlightNumberNotNumberThrowsNumberFormatException(){
    Flight notANumber = new Flight("NotANumber","SEA","01/23/2023", "23:40","PDX","01/23/2023", "23:40");
    assertThat(notANumber.getError(),containsString("Flight Number must be a positive integer"));
  }

  @Test
  void flightNumberMustBeGreaterThanZero(){
    Flight lessThanZero = new Flight("-26","SEA","01/23/2023", "23:40","PDX","01/23/2023", "23:40");
    assertThat(lessThanZero.getError(),containsString("Flight Number must be greater than zero"));
  }

  @Test
  void nullDepartureAirportPrintsNullError(){
    Flight nullAirport = new Flight("26",null,"01/23/2023", "23:40","PDX","01/23/2023", "23:40");
    assertThat(nullAirport.getError(), equalTo("Departure airport code cannot be null."));
  }

  @Test
  void nullArrivalAirportPrintsNullError(){
    Flight nullAirport = new Flight("26","TSA","01/23/2023", "23:40",null,"01/23/2023", "23:40");
    assertThat(nullAirport.getError(), equalTo("Arrival airport code cannot be null."));
  }

  @Test
  void nullDepartureDatePrintsNullError(){
    Flight nullTime = new Flight("26","SEA",null,"23:40","SFO","01/23/2023", "23:40");
    assertThat(nullTime.getError(), equalTo("Departure date cannot be null."));
  }

  @Test
  void nullArrivalDatePrintsNullError(){
    Flight nullTime = new Flight("26","SEA","01/23/2023","23:40","SFO",null,"23:40");
    assertThat(nullTime.getError(), equalTo("Arrival date cannot be null."));
  }

  @Test
  void nullDepartureTimePrintsNullError(){
    Flight nullTime = new Flight("26","SEA","01/23/2023",null,"SFO","01/23/2023", "23:40");
    assertThat(nullTime.getError(), equalTo("Departure time cannot be null."));
  }

  @Test
  void nullArrivalTimePrintsNullError(){
    Flight nullTime = new Flight("26","SEA","01/23/2023","23:40","SFO","01/23/2023",null);
    assertThat(nullTime.getError(), equalTo("Arrival time cannot be null."));
  }
  @Test
  void departureAirportMustBeLengthThree(){
    Flight code = new Flight("26","SLAP","01/23/2023", "23:40","JFK","01/23/2023", "23:40");
    assertThat(code.getError(), containsString("airport code must be a 3-letter alphabetical code"));
  }

  @Test
  void arrivalAirportMustBeLengthThree(){
    Flight airport = new Flight("26","SEA","01/23/2023", "23:40","KJFK","01/23/2023", "23:40");
    assertThat(airport.getError(), containsString("airport code must be a 3-letter alphabetical code"));
  }

  @Test
  void departureAirportCodeMustBeAlphabetic(){
    Flight airport = new Flight("26","*9[","01/23/2023", "23:40","JFK","01/23/2023", "23:40");
    assertThat(airport.getError(), containsString("airport code must be a 3-letter alphabetical code"));
  }

  @Test
  void arrivalAirportCodeMustBeAlphabetic(){
    Flight airport = new Flight("26","SEA","01/23/2023", "23:40","*9[","01/23/2023", "23:40");
    assertThat(airport.getError(), containsString("airport code must be a 3-letter alphabetical code"));
  }

  @Test
  void departureAirportCodeCanBeRetrieved(){
    Flight airport = new Flight("26","SEA","01/23/2023", "23:40","JFK","01/23/2023", "23:40");
    assertThat(airport.getSource(), equalTo("SEA"));
  }

  @Test
  void arrivalAirportCodeCanBeRetrieved(){
    Flight airport = new Flight("26","SEA","01/23/2023", "23:40","JFK","01/23/2023", "23:40");
    assertThat(airport.getDestination(), equalTo("JFK"));
  }

}

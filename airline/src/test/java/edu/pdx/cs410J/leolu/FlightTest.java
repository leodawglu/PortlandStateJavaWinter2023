/**
 * The {@code FlightTest} class contains unit tests for the {@link edu.pdx.cs410J.leolu.Flight} class
 *
 *
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
  void nullFlightNumberThrowsNullPointerException(){
    Flight nulls = new Flight(null,"SEA","01/23/2023 23:40","PDX","01/23/2023 23:40");
    assertThat(nulls.getError(), equalTo("null input"));
  }

  @Test
  void inputFlightNumberNotNumberThrowsNumberFormatException(){
    Flight notANumber = new Flight("NotANumber","SEA","01/23/2023 23:40","PDX","01/23/2023 23:40");
    assertThat(notANumber.getError(),equalTo("Flight Number must be greater than 0!"));
  }

  @Test
  void flightNumberMustBeGreaterThanZero(){
    Flight lessThanZero = new Flight("-26","SEA","01/23/2023 23:40","PDX","01/23/2023 23:40");
    assertThat(lessThanZero.getError(),equalTo("Flight Number must be greater than 0!"));
  }

  @Test
  void nullDepartureAirportThrowsNullPointerException(){
    Flight nullAirport = new Flight("26",null,"01/23/2023 23:40","PDX","01/23/2023 23:40");
    assertThat(nullAirport.getError(), equalTo("null input"));
  }

  @Test
  void nullArrivalAirportThrowsNullPointerException(){
    Flight nullAirport = new Flight("26","TSA","01/23/2023 23:40",null,"01/23/2023 23:40");
    assertThat(nullAirport.getError(), equalTo("null input"));
  }

  @Test
  void nullDepartureTimeThrowsNullPointerException(){
    Flight nullTime = new Flight("26","SEA",null,"SFO","01/23/2023 23:40");
    assertThat(nullTime.getError(), equalTo("null input"));
  }

  @Test
  void nullArrivalTimeThrowsNullPointerException(){
    Flight nullTime = new Flight("26","SEA",null,"SFO","01/23/2023 23:40");
    assertThat(nullTime.getError(), equalTo("null input"));
  }

  @Test
  void departureAirportMustBeLengthThree(){
    Flight code = new Flight("26","SLAP","01/23/2023 23:40","JFK","01/23/2023 23:40");
    assertThat(code.getError(), equalTo("Airport Code must be a 3-letter alphabetical code"));
  }

  @Test
  void arrivalAirportMustBeLengthThree(){
    Flight airport = new Flight("26","SEA","01/23/2023 23:40","KJFK","01/23/2023 23:40");
    assertThat(airport.getError(), equalTo("Airport Code must be a 3-letter alphabetical code"));
  }

  @Test
  void departureAirportCodeMustBeAlphabetic(){
    Flight airport = new Flight("26","*9[","01/23/2023 23:40","JFK","01/23/2023 23:40");
    assertThat(airport.getError(), equalTo("Airport Code must be a 3-letter alphabetical code"));
  }

  @Test
  void arrivalAirportCodeMustBeAlphabetic(){
    Flight airport = new Flight("26","SEA","01/23/2023 23:40","*9[","01/23/2023 23:40");
    assertThat(airport.getError(), equalTo("Airport Code must be a 3-letter alphabetical code"));
  }

}

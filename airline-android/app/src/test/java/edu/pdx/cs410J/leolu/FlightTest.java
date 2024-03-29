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
  void nullFlightNumberPrintsNullError(){
    Flight nulls = new Flight(null,"SEA","01/23/2023","22:40","PDX","01/23/2023", "23:40");
    assertThat(nulls.getError(), equalTo("Flight Number cannot be null."));
  }

  @Test
  void inputFlightNumberNotNumberThrowsNumberFormatException(){
    Flight notANumber = new Flight("NotANumber","SEA","01/23/2023", "22:40","PDX","01/23/2023", "23:40");
    assertThat(notANumber.getError(),containsString("Flight Number must be a positive integer"));
  }

  @Test
  void flightNumberMustBeGreaterThanZero(){
    Flight lessThanZero = new Flight("-26","SEA","01/23/2023", "22:40","PDX","01/23/2023", "23:40");
    assertThat(lessThanZero.getError(),containsString("Flight Number must be greater than zero"));
  }

  @Test
  void nullDepartureAirportPrintsNullError(){
    Flight nullAirport = new Flight("26",null,"01/23/2023", "22:40","PDX","01/23/2023", "23:40");
    assertThat(nullAirport.getError(), equalTo("Departure airport code cannot be null."));
  }

  @Test
  void nullArrivalAirportPrintsNullError(){
    Flight nullAirport = new Flight("26","TSA","01/23/2023", "22:40",null,"01/23/2023", "23:40");
    assertThat(nullAirport.getError(), equalTo("Arrival airport code cannot be null."));
  }

  @Test
  void nullDepartureDatePrintsNullError(){
    Flight nullTime = new Flight("26","SEA",null,"11:40 am","SFO","01/23/2023", "12:40 pm");
    assertThat(nullTime.getError(), containsString("Departure date cannot be null."));
  }

  @Test
  void nullArrivalDatePrintsNullError(){
    Flight nullTime = new Flight("26","SEA","01/23/2023","11:40 am","SFO",null,"12:40 pm");
    assertThat(nullTime.getError(), containsString("Arrival date cannot be null."));
  }

  @Test
  void nullDepartureTimePrintsNullError(){
    Flight nullTime = new Flight("26","SEA","01/23/2023",null,"SFO","01/23/2023", "3:40 pm");
    assertThat(nullTime.getError(), containsString("Departure time cannot be null."));
  }

  @Test
  void nullArrivalTimePrintsNullError(){
    Flight nullTime = new Flight("26","SEA","01/23/2023","11:40am","SFO","01/23/2023",null);
    assertThat(nullTime.getError(), containsString("Arrival time cannot be null."));
  }
  @Test
  void departureAirportMustBeLengthThree(){
    Flight code = new Flight("26","SLAP","01/23/2023", "22:40","JFK","01/23/2023", "23:40");
    assertThat(code.getError(), containsString("airport code must be a 3-letter alphabetical code"));
  }

  @Test
  void arrivalAirportMustBeLengthThree(){
    Flight airport = new Flight("26","SEA","01/23/2023", "22:40","KJFK","01/23/2023", "23:40");
    assertThat(airport.getError(), containsString("airport code must be a 3-letter alphabetical code"));
  }

  @Test
  void departureAirportCodeMustBeAlphabetic(){
    Flight airport = new Flight("26","*9[","01/23/2023", "22:40","JFK","01/23/2023", "23:40");
    assertThat(airport.getError(), containsString("airport code must be a 3-letter alphabetical code"));
  }

  @Test
  void arrivalAirportCodeMustBeAlphabetic(){
    Flight airport = new Flight("26","SEA","01/23/2023", "22:40","*9[","01/23/2023", "23:40");
    assertThat(airport.getError(), containsString("airport code must be a 3-letter alphabetical code"));
  }

  @Test
  void departureAirportCodeCanBeRetrieved(){
    Flight airport = new Flight("26","SEA","01/23/2023", "20:40","JFK","01/23/2023", "23:40");
    assertThat(airport.getSource(), equalTo("SEA"));
  }

  @Test
  void arrivalAirportCodeCanBeRetrieved(){
    Flight airport = new Flight("26","SEA","01/23/2023", "20:40","JFK","01/23/2023", "23:40");
    assertThat(airport.getDestination(), equalTo("JFK"));
  }

  @Test
  void notRealDepartureAirportCodeThrowsErrorMessage(){
    Flight airport = new Flight("26","SXA","01/23/2023", "20:40","JFK","01/23/2023", "23:40");
    assertThat(airport.getError(), containsString("airport code is not a real airport code:"));
  }

  @Test
  void notRealArrivalAirportCodeThrowsErrorMessage(){
    Flight airport = new Flight("26","SEA","01/23/2023", "20:40","JFX","01/23/2023", "23:40");
    assertThat(airport.getError(), containsString("airport code is not a real airport code:"));
  }

  @Test
  void invalid12hrPMTimePMFormatThrowsErrorMessage(){
    Flight twelve = new Flight();
    twelve.setDateTime("01/23/2023","23:40 PM","Departure");
    assertThat(twelve.getError(), containsString("time format incorrect"));
  }

  @Test
  void invalid12hrAMTimePMFormatThrowsErrorMessage(){
    Flight twelve = new Flight();
    twelve.setDateTime("01/23/2023","13:40 AM","Departure");
    assertThat(twelve.getError(), containsString("time format incorrect"));
  }

  @Test
  void invalid12hrXMTimePMFormatThrowsErrorMessage(){
    Flight twelve = new Flight();
    twelve.setDateTime("01/23/2023","12:40 XM","Departure");
    assertThat(twelve.getError(), containsString("time incorrect"));
  }

  @Test
  void invalid24hrFormatThrowsErrorMessage(){
    Flight twelve = new Flight();
    twelve.setDateTime("01/23/2023","23:40","Departure");
    assertThat(twelve.getError(), containsString("time incorrect or missing"));
  }

  @Test
  void invalidTimeFormatThrowsErrorMessage(){
    Flight twelve = new Flight();
    twelve.setDateTime("01/23/2023","123:40 AM","Departure");
    assertThat(twelve.getError(), containsString("time format incorrect"));
  }

  @Test
  void getDepString(){
    Flight twelve = new Flight();
    twelve.setDateTime("02/08/2023","3:40 AM","Departure");
    assertThat(twelve.getDepartureString(), equalTo("2/8/23, 3:40 AM"));
  }

  @Test
  void getArrString(){
    Flight twelve = new Flight();
    twelve.setDateTime("2/8/2023","3:40 AM","Arrival");
    assertThat(twelve.getArrivalString(), equalTo("2/8/23, 3:40 AM"));
  }

  @Test
  void compareFlightsByAirportBiggerToSmaller(){
    Flight taipei = new Flight();
    taipei.setAirportCode("tpe","Departure");
    Flight seattle = new Flight();
    seattle.setAirportCode("sea","Departure");
    assertThat(taipei.compareTo(seattle), equalTo(1));
  }

  @Test
  void compareFlightsByAirportSmallerToBigger(){
    Flight taipei = new Flight();
    taipei.setAirportCode("tpe","Departure");
    Flight seattle = new Flight();
    seattle.setAirportCode("sea","Departure");
    assertThat(seattle.compareTo(taipei), equalTo(-1));
  }

  @Test
  void compareFlightsBySameDateTime(){
    Flight seattle = new Flight();
    seattle.setAirportCode("sea","Departure");
    seattle.setDateTime("2/8/2023","3:40 AM","Departure");
    Flight seattle2 = new Flight();
    seattle2.setAirportCode("sea","Departure");
    seattle2.setDateTime("2/8/2023","3:40 AM","Departure");
    assertThat(seattle.compareTo(seattle2), equalTo(0));
  }

  @Test
  void compareFlightsByDateTimeEarlierToLater(){
    Flight seattle = new Flight();
    seattle.setAirportCode("sea","Departure");
    seattle.setDateTime("2/8/2023","3:40 AM","Departure");
    Flight seattle2 = new Flight();
    seattle2.setAirportCode("sea","Departure");
    seattle2.setDateTime("2/8/2023","3:45 AM","Departure");
    assertThat(seattle.compareTo(seattle2), equalTo(-1));
  }

  @Test
  void compareFlightsByDateTimeLaterToEarlier(){
    Flight seattle = new Flight();
    seattle.setAirportCode("sea","Departure");
    seattle.setDateTime("2/8/2023","4:40 AM","Departure");
    Flight seattle2 = new Flight();
    seattle2.setAirportCode("sea","Departure");
    seattle2.setDateTime("2/8/2023","3:45 AM","Departure");
    assertThat(seattle.compareTo(seattle2), equalTo(1));
  }

  @Test
  void flightArrivalEarlierThanDepartureThrowsError() {
    Flight seattle = new Flight();
    seattle.setAirportCode("sea", "Departure");
    seattle.setDateTime("2/8/2023", "4:40 AM", "Departure");
    seattle.setAirportCode("sfo", "Arrival");
    seattle.setDateTime("2/8/2023", "2:40 AM", "Arrival");
    seattle.setFlightDuration();
    assertThat(seattle.getError(),containsString("cannot be earlier than"));
  }

  @Test
  void flightDuration120minutes() {
    Flight seattle = new Flight();
    seattle.setAirportCode("sea", "Departure");
    seattle.setDateTime("2/8/2023", "2:40 AM", "Departure");
    seattle.setAirportCode("sfo", "Arrival");
    seattle.setDateTime("2/8/2023", "4:40 AM", "Arrival");
    seattle.setFlightDuration();
    assertThat(seattle.getFlightDuration(),equalTo(120));
  }

  @Test
  void canParseFlightDepartureArrival24TimeIntoHoursAndMins() {
    Flight seattle = new Flight();
    seattle.setAirportCode("sea", "Departure");
    seattle.setDateTime("2/8/2023", "2:40 AM", "Departure");
    seattle.setAirportCode("tpe", "Arrival");
    seattle.setDateTime("2/8/2023", "7:01 pM", "Arrival");
    seattle.setFlightDuration();
    String[] departure = seattle.getDepTime24().split(":");
    int dH = Integer.parseInt(departure[0]), dM = Integer.parseInt(departure[1]);
    String[] arrival = seattle.getArrTime24().split(":");
    int aH = Integer.parseInt(arrival[0]), aM = Integer.parseInt(arrival[1]);
    System.out.println(dH + " | " + dM);
    System.out.println(aH + " | " + aM);
    assertThat(dH,equalTo(2));
    assertThat(dM,equalTo(40));
    assertThat(aH,equalTo(19));
    assertThat(aM,equalTo(1));
  }

  @Test
  void canParseFlightDepartureArrivalDateIntoMonthDayYear() {
    Flight seattle = new Flight();
    seattle.setAirportCode("sea", "Departure");
    seattle.setDateTime("2/8/2023", "2:40 AM", "Departure");
    seattle.setAirportCode("tpe", "Arrival");
    seattle.setDateTime("2/8/2023", "7:01 pM", "Arrival");
    seattle.setFlightDuration();
    String[] departure = seattle.getDepDate().split("/");
    int dMonth = Integer.parseInt(departure[0]), dDay = Integer.parseInt(departure[1]), dYear = Integer.parseInt(departure[2]);
    String[] arrival = seattle.getArrDate().split("/");
    int aMonth = Integer.parseInt(arrival[0]), aDay = Integer.parseInt(arrival[1]), aYear = Integer.parseInt(arrival[2]);
    System.out.println(dMonth + " | " + dDay + " | " + dYear);
    System.out.println(aMonth + " | " + aDay + " | " + aYear);
    assertThat(dMonth,equalTo(2));
    assertThat(dDay,equalTo(8));
    assertThat(dYear,equalTo(2023));
    assertThat(aMonth,equalTo(2));
    assertThat(aDay,equalTo(8));
    assertThat(aYear,equalTo(2023));
  }
}

/**
 * The {@code AirlineTest} class contains
 * unit tests for the {@link Airline} class
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AirlineTest {

    @Test
    void nullAirlineNameThrowsNullPointerException(){
        Airline nullAir = new Airline(null);
        assertThat(nullAir.getError(),equalTo("Airline name cannot be empty"));
    }

    @Test
    void emptyAirlineNameThrowsNullPointerException(){
        Airline empty = new Airline("");
        assertThat(empty.getError(),equalTo("Airline name cannot be empty"));
    }

    @Test
    void nullFlightsArrayListThrowsNullPointerException(){
        Airline nullList = new Airline("NullList",null);
        assertThat(nullList.getError(),equalTo("Flight list is null"));
    }

    @Test
    void addingNullFlightToListThrowsNullPointerException(){
        Airline flyaway = new Airline("Flyaway");
        flyaway.addFlight(null);
        assertThat(flyaway.getError(),equalTo("Cannot add a null flight!"));
    }

    @Test
    void creatingAirlineFlightListWithNullFlightThrowsNullPointerException() {
        ArrayList<Flight> flights = new ArrayList<>();
        flights.add(new Flight("26","SEA","01/23/2023", "23:40","JFK","01/23/2023", "23:40"));
        flights.add(null);
        Airline nullFlight = new Airline("nullFlight",flights);
        assertThat(nullFlight.getError(),equalTo("Null flight found in flights list"));
    }

}

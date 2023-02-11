package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PrettyPrinterTest {
    @Test
    void airlineNameIsPrintedInPrettyFormat(){
        String airlineName = "Test Airline";
        Airline airline = new Airline(airlineName);

        StringWriter sw = new StringWriter();
        PrettyPrinter dumper = new PrettyPrinter(sw);
        dumper.dump(airline);

        String text = sw.toString();
        assertThat(text, containsString(airline.getName()));
    }

    @Test
    void canPrintAirlineAndFlightsInPrettyFormat(){
        String airlineName = "Test Airline";
        Airline airline = new Airline(airlineName);
        Flight fl = new Flight("25", "SEA", "1/26/2023", "0:10", "TPE", "1/27/2023" ,"5:30",true);
        airline.addFlight(fl);
        StringWriter sw = new StringWriter();
        PrettyPrinter dumper = new PrettyPrinter(sw);
        dumper.dump(airline);

        String text = sw.toString();
        System.out.println(text);
        assertThat(text, containsString(airline.getName()));
        assertThat(text, containsString("Departing From: " +fl.getSource()));
        assertThat(text, containsString("Bound For     : "+fl.getDestination()));
    }
}

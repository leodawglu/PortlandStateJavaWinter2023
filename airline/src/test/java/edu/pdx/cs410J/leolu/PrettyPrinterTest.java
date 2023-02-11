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
        assertThat(text, containsString("*--------------------*"+airline.getName()+"*-------------------*"));
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
        assertThat(text, containsString("*--------------------*"+airline.getName()+"*-------------------*"));
        assertThat(text, containsString("Departing From: " +fl.getSource()));
        assertThat(text, containsString("Bound For     : "+fl.getDestination()));
    }

    /*
    @Test
    void prettyPrintFlightsShowFlightsAreSortedAlphabetically() throws ParserException, IOException {
        PrintWriter out = new PrintWriter(System.out);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        Project3 proj = new Project3();

        String filePath = getClass().getResource("evaair.txt").getPath();

        String[] input = new String[]{"-textFile", filePath,"-pretty","-","EVA Air",
                "52", "TPE", "05/19/2023", "12:40","am", "IAH", "05/20/2023", "12:10","pm"};
        proj.subMainForTesting(input);



        String output = baos.toString();
        assertThat(output,equalTo("Flight 25 departs SEA at 1/26/23, 12:10 AM arrives TPE at 3/2/23, 5:30 AM\n" +
                "Flight 52 departs SEA at 1/26/23, 12:50 AM arrives SFO at 3/2/23, 6:10 AM\n" +
                "Flight 12 departs SFO at 1/26/23, 12:10 AM arrives TPE at 3/2/23, 5:30 AM\n" +
                "Flight 26 departs TPE at 1/26/23, 11:10 PM arrives SEA at 3/2/23, 6:30 AM\n"));
        System.setOut(System.out);
    }*/

}

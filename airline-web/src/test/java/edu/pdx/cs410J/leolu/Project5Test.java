package edu.pdx.cs410J.leolu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Project5Test {

    @Test
    void noCommandLineArgumentsPrintsUsage(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        Project5.invokedByMain(new String[]{});

        String output = baos.toString();
        assertThat(output, containsString("USAGE"));
        System.setOut(System.out);
    }

    @Test
    void nonIntegerPortPrintsError(){
        Project5 p5 = mock(Project5.class);
        p5.portState=1;
        String[] args = new String[]{"NotAnInteger"};

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        p5.argumentReader(args,p5);
        String output = baos.toString();
        assertThat(output, containsString("Port number must be an integer!" +
                " Provided port is not an integer:"));
        System.setErr(System.err);
    }

    @Test
    void whenSearchOptionInvokedAndArgumentsMoreThan3PrintsError(){
        Project5 p5 = mock(Project5.class);
        p5.argCount=3;
        p5.search=true;
        String[] args = new String[]{"TooManyArgs"};

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        p5.argumentReader(args,p5);
        String output = baos.toString();
        assertThat(output, containsString("is an extraneous argument when using -search function."));
        System.setErr(System.err);
    }

    @Test
    void ArgumentsMoreThan10PrintsError(){
        Project5 p5 = mock(Project5.class);
        p5.argCount=10;
        String[] args = new String[]{"TooManyArgs"};

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        p5.argumentReader(args,p5);
        String output = baos.toString();
        assertThat(output, containsString("is an extraneous argument."));
        System.setErr(System.err);
    }

    @Test
    void missingHostNamePrintsError(){
        Project5 p5 = mock(Project5.class);
        String[] args = new String[]{};

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        p5.argumentReader(args,p5);
        String output = baos.toString();
        assertThat(output, containsString("Hostname is missing!"));
        System.setErr(System.err);
    }

    @Test
    void missingPortPrintsError(){
        Project5 p5 = mock(Project5.class);
        p5.hostNameState=-1;
        String[] args = new String[]{};

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        p5.argumentReader(args,p5);
        String output = baos.toString();
        assertThat(output, containsString("Port number is missing!"));
        System.setErr(System.err);
    }

    @Test
    void invalidOptionPrintsError(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        Project5.invokedByMain(new String[]{"-invalidoption"});

        String output = baos.toString();
        assertThat(output, containsString("is not a valid option"));
        System.setErr(System.err);
    }

    @Test
    void searchAndPrintOptionsInvokedTogetherPrintsError(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        Project5.invokedByMain(new String[]{"-print","-search"});

        String output = baos.toString();
        assertThat(output, containsString("The -search option cannot be invoked with -print option"));
        System.setErr(System.err);
    }

    @Test
    void printAndSearchOptionsInvokedTogetherPrintsError(){
        Project5 p5 = mock(Project5.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        p5.invokedByMain(new String[]{"-search","-print"});

        String output = baos.toString();
        //assertThat(p5.search,equalTo(true));
        assertThat(output, containsString("The -print option cannot be invoked with -search option"));
        System.setErr(System.err);
    }
    @Test
    void hostOptionCalledMoreThanOncePrintsError(){
        Project5 p5 = mock(Project5.class);
        p5.hostNameState=-1;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        p5.optionChecker("-host",p5);
        String output = baos.toString();
        assertThat(output, containsString("The -host option can only be called once"));
        System.setErr(System.err);
    }

    @Test
    void portOptionCalledMoreThanOncePrintsError(){
        Project5 p5 = mock(Project5.class);
        p5.portState=-1;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        p5.optionChecker("-port",p5);
        String output = baos.toString();
        assertThat(output, containsString("The -port option can only be called once"));
        System.setErr(System.err);
    }

    @Test
    void readMeOptionInvokedPrintsReadMeFile(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        Project5.invokedByMain(new String[]{"-print","-README"});

        String output = baos.toString();
        assertThat(output, containsString("Airline-Web Project"));
        System.setOut(System.out);
    }

    @Test
    void noArgsAfterSearchPrintsError(){
        Project5 p5 = mock(Project5.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        Project5.searchForAirlineAndFlights(new String[]{},p5);

        String output = baos.toString();
        assertThat(output, containsString("No arguments were entered for -search option."));
        System.setErr(System.err);
    }

    @Test
    void argsAfterSearchMoreThan3PrintsError(){
        Project5 p5 = mock(Project5.class);
        p5.idx=-4;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
        Project5.searchForAirlineAndFlights(new String[]{},p5);

        String output = baos.toString();
        assertThat(output, containsString("Please enter only the airline name to get all flights from that airline,\n" +
                "OR enter the airline name, departure airport code, and arrival airport code " +
                "to get flights that match the specified itinerary.\n" +
                "Please review usage and try again."));
        System.setErr(System.err);
    }

    @Test
    void searchOnlyAnAirlineNameReturnsAirline(){
        Airline mock = new Airline("mock");
        Project5 p5 = mock(Project5.class);
        AirlineRestClient client = mock(AirlineRestClient.class);
        p5.client = client;
        String[] args = new String[]{"one"};
        when(p5.client.getAirline(args[0],null,null)).thenReturn(mock);
        Project5.searchForAirlineAndFlights(args,p5);
        assertThat(p5.anAirline,equalTo(mock));
    }

    @Test
    void searchAnAirlineNameWithSRCAndDESTReturnsAirline(){
        Airline mock = new Airline("mock");
        Project5 p5 = mock(Project5.class);
        AirlineRestClient client = mock(AirlineRestClient.class);
        p5.client = client;
        String[] args = new String[]{"one","TPE","SIN"};
        when(p5.client.getAirline(args[0],args[1],args[2])).thenReturn(mock);
        Project5.searchForAirlineAndFlights(args,p5);
        assertThat(p5.anAirline,equalTo(mock));
    }

    @Test
    void createAirlineAndFlightWithNoArgsPrintsErrors(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        Project5 p5 = mock(Project5.class);
        String[] args = new String[]{};
        p5.createAirlineAndFlight(args,p5);
        String output = baos.toString();
        assertThat(output, containsString("Arrival Time AM/PM"));
        System.setOut(System.out);
    }

    @Test
    void createAirlineAndFlightSuccessfully(){
        Project5 p5 = mock(Project5.class);
        AirlineRestClient client = mock(AirlineRestClient.class);
        p5.client = client;
        String[] args = new String[]{"EVA Air","52","TPE","02/02/2022","10:40","AM",
                "SIN","02/02/2022","3:40","PM"};
        p5.createAirlineAndFlight(args,p5);
        assertThat(p5.anAirline.toString(), equalTo("EVA Air with 1 flights"));
        assertThat(p5.aFlight.toString(), equalTo("Flight 52 departs TPE at 2/2/22, 10:40 AM " +
                "arrives SIN at 2/2/22, 3:40 PM"));
    }
}

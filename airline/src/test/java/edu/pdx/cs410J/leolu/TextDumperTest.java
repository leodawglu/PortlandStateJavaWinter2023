package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TextDumperTest {

  @Test
  void airlineNameIsDumpedInTextFormat(){
    String airlineName = "Test Airline";
    Airline airline = new Airline(airlineName);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);

    String text = sw.toString();
    assertThat(text, containsString(airlineName));
  }

  @Test
  void canParseTextWrittenByTextDumper(@TempDir File tempDir) throws IOException, ParserException {
    String airlineName = "Test Airline";
    Airline airline = new Airline(airlineName);

    File textFile = new File(tempDir, "airline.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    dumper.dump(airline);

    TextParser parser = new TextParser(new FileReader(textFile));
    Airline read = parser.parse();
    assertThat(read.getName(), equalTo(airlineName));
  }

  @Test
  void canDumpAirlineAndFlightsInTextFormat(){
    String airlineName = "Test Airline";
    Airline airline = new Airline(airlineName);
    Flight fl = new Flight("25", "SEA", "1/26/2023", "0:10", "TPE", "1/27/2023" ,"5:30");
    airline.addFlight(fl);
    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);

    String text = sw.toString();
    System.out.println(text);
    assertThat(text, containsString(airlineName));
    assertThat(text, containsString(fl.getSource()));
    assertThat(text, containsString(fl.getDestination()));
  }

  @Test
  void canParseAirlineFlightWrittenByTextDumper(@TempDir File tempDir) throws IOException, ParserException {
    String airlineName = "Test Airline";
    Airline airline = new Airline(airlineName);
    Flight fl = new Flight("25", "SEA", "1/26/2023", "0:10", "TPE", "1/27/2023" ,"5:30");
    airline.addFlight(fl);
    File textFile = new File(tempDir, "airline.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    dumper.dump(airline);

    TextParser parser = new TextParser(new FileReader(textFile));
    Airline read = parser.parse();
    assertThat(read.getName(), equalTo(airlineName));
    assertThat(read.getFlights().size(), equalTo(1));
  }
}

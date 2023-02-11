package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextParserTest {

  @Test
  void validTextFileCanBeParsed() throws ParserException {
    InputStream resource = getClass().getResourceAsStream("valid-airline.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    Airline airline = parser.parse();
    assertThat(airline.getName(), equalTo("Test Airline"));
  }

  @Test
  void invalidTextFileThrowsParserException() {
    InputStream resource = getClass().getResourceAsStream("empty-airline.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    assertThrows(ParserException.class, parser::parse);
  }

  @Test
  void extraneousInputsReadFlightsThrowsIllegalArgumentException() throws IOException, ParserException {
    InputStream resource = getClass().getResourceAsStream("extra.txt");
    assertThat(resource, notNullValue());
    TextParser parser = new TextParser(new InputStreamReader(resource));
    parser.parse();
    assertThat(parser.getError(),containsString("Extraneous inputs"));
  }
  @Test
  void fileFlightOnlyHasFlightNumberThrowsIllegalArgumentException() throws IOException, ParserException {
    InputStream resource = getClass().getResourceAsStream("departcode.txt");
    assertThat(resource, notNullValue());
    TextParser parser = new TextParser(new InputStreamReader(resource));
    parser.parse();
    assertThat(parser.getError(),containsString("Departure Airport Code"));
  }
  @Test
  void fileFlightMissingInfo1ThrowsIllegalArgumentException() throws IOException, ParserException {
    InputStream resource = getClass().getResourceAsStream("departcode.txt");
    assertThat(resource, notNullValue());
    TextParser parser = new TextParser(new InputStreamReader(resource));
    parser.parse();
    assertThat(parser.getError(),containsString("Departure Airport Code"));
  }
  @Test
  void fileFlightMissingInfo2ThrowsIllegalArgumentException() throws IOException, ParserException {
    InputStream resource = getClass().getResourceAsStream("departdate.txt");
    assertThat(resource, notNullValue());
    TextParser parser = new TextParser(new InputStreamReader(resource));
    parser.parse();
    assertThat(parser.getError(),containsString("Departure Date"));
  }
  @Test
  void fileFlightMissingInfo3ThrowsIllegalArgumentException() throws IOException, ParserException {
    InputStream resource = getClass().getResourceAsStream("arrcode.txt");
    assertThat(resource, notNullValue());
    TextParser parser = new TextParser(new InputStreamReader(resource));
    parser.parse();
    assertThat(parser.getError(),containsString("Arrival Airport Code"));
  }
  @Test
  void fileFlightMissingInfo4ThrowsIllegalArgumentException() throws IOException, ParserException {
    InputStream resource = getClass().getResourceAsStream("arrtime.txt");
    assertThat(resource, notNullValue());
    TextParser parser = new TextParser(new InputStreamReader(resource));
    parser.parse();
    assertThat(parser.getError(),containsString("Arrival Time"));
  }

  @Test
  void emptyLinesAreIgnoredReadFlightsThrowsIllegalArgumentException() throws IOException, ParserException {
    InputStream resource = getClass().getResourceAsStream("empty-line.txt");
    assertThat(resource, notNullValue());
    TextParser parser = new TextParser(new InputStreamReader(resource));
    parser.parse();
    assertThat(parser.getError(),isEmptyString());
  }
  @Test
  void printFlightsShowFlightsAreSortedAlphabetically() throws IOException, ParserException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
    InputStream resource = getClass().getResourceAsStream("valid-airline.txt");
    assertThat(resource, notNullValue());
    TextParser parser = new TextParser(new InputStreamReader(resource));
    Airline air = parser.parse();
    for(Flight f: air.getFlights())System.out.println(f.toString());
    String output = baos.toString();
    assertThat(output,equalTo("Flight 25 departs SEA at 1/26/23, 12:10 AM arrives TPE at 3/2/23, 5:30 AM\n" +
            "Flight 52 departs SEA at 1/26/23, 12:50 AM arrives SFO at 3/2/23, 6:10 AM\n" +
            "Flight 12 departs SFO at 1/26/23, 12:10 AM arrives TPE at 3/2/23, 5:30 AM\n" +
            "Flight 26 departs TPE at 1/26/23, 11:10 PM arrives SEA at 3/2/23, 6:30 AM\n"));
    System.setOut(System.out);
  }


}

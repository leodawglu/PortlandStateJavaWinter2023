package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
  void printFlights() throws IOException, ParserException {
    InputStream resource = getClass().getResourceAsStream("valid-airline.txt");
    assertThat(resource, notNullValue());
    TextParser parser = new TextParser(new InputStreamReader(resource));
    Airline air = parser.parse();
    for(Flight f: air.getFlights())System.out.println(f.toString());
  }


}

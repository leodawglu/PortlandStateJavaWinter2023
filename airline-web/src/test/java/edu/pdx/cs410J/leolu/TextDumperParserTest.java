package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TextDumperParserTest {

  /*
  @Test
  void airlineWithOneFlightCanBeDumpedAndParsed() throws ParserException {
    String airlineName = "Airline";
    int flightNumber = 123;
    Airline airline = new Airline(airlineName);
    Flight fl = new Flight();
    fl.setFlightNumber(String.valueOf(flightNumber));
    Map<String, String> airline = new Airline(airlineName);
    Map<String, String> read = dumpAndParse(map);
    assertThat(read, equalTo(map));
  }

  private Map<String, String> dumpAndParse(Map<String, String> map) throws ParserException {
    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(map);

    String text = sw.toString();

    TextParser parser = new TextParser(new StringReader(text));
    return parser.parse();
  }

  @Test
  void dumpedTextCanBeParsed() throws ParserException {
    Map<String, String> map = Map.of("one", "1", "two", "2");
    Map<String, String> read = dumpAndParse(map);
    assertThat(read, equalTo(map));
  }*/
}

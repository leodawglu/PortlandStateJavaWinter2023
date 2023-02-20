package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXParseException;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class XmlParserTest {

    final String VALID = "src/test/resources/edu/pdx/cs410J/leolu/valid-airline.xml";
    final String INVALID = "src/test/resources/edu/pdx/cs410J/leolu/invalid-airline.xml";

    @Test
    void validAirlineXMLCanBeParsed() throws ParserException {
        XmlParser parser = new XmlParser(VALID);
        Airline airline = parser.parse();
        System.out.println(airline.toString());
        for(Flight f: airline.getFlights()){
            System.out.println(f.toString());
        }
    }

    @Test
    void invalidAirlineXMLCantBeParsed(){
        XmlParser parser = new XmlParser(INVALID);
        assertThrows(ParserException.class, ()->parser.parse());
    }
}

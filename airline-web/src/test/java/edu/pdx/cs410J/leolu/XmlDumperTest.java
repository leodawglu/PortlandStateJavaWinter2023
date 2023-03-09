package edu.pdx.cs410J.leolu;


import org.junit.jupiter.api.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class XmlDumperTest {


    @Test
    void canWriteAirlineToNewXMLFile(){
        Airline airline = new Airline("Test Airline");
        Flight f1 = new Flight("26","SEA","01/23/2023","12:40 pm","SFO","01/23/2023", "3:40 pm");
        Flight f2 = new Flight("27","SEA","01/23/2023","22:40","PDX","01/23/2023", "23:40", true);
        airline.addFlight(f1);
        airline.addFlight(f2);
        XmlDumper dumper = new XmlDumper("src/test/resources/edu/pdx/cs410J/leolu/first-airline");
        dumper.dump(airline);
        dumper.deleteXmlFile();
    }

    @Test
    void nullAirlineInDumpPrintsErrorToSystemErr(){
        XmlDumper dumper = new XmlDumper("src/test/resources/edu/pdx/cs410J/leolu/first-airline.xml");
        dumper.dump(null);
        assertThat(dumper.getErrorMsg(),containsString("XmlDumper dump method does not accept null for airline object!"));
    }

    @Test
    void badFilePathDumpThrowsTransformerException(){
        XmlDumper dumper = new XmlDumper("ources/edu/pdx/cs410J/leolu/first-airline.xml");
        Airline airline = new Airline("Test Airline");
        Flight f1 = new Flight("26","SEA","01/23/2023","12:40 pm","SFO","01/23/2023", "3:40 pm");
        Flight f2 = new Flight("27","SEA","01/23/2023","22:40","PDX","01/23/2023", "23:40", true);
        airline.addFlight(f1);
        airline.addFlight(f2);
        assertThrows(RuntimeException.class,()->dumper.dump(airline));
    }
}

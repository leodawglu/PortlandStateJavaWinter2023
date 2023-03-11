package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static edu.pdx.cs410J.web.HttpRequestHelper.Response;

/**
 * A unit test for the REST client that demonstrates using mocks and
 * dependency injection
 */
public class AirlineRestClientTest {

/*
  @Test
  void getAllFlightsFromAirlineWhenGetAirlineWithNoSRCAndDest() throws ParserException, IOException {
    Airline mock = new Airline("mock");

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of()))).thenReturn(airlinesAsText(mock));
    
    AirlineRestClient client = new AirlineRestClient(http);

    assertThat(client.getAirline("","",""), equalTo(mock));
  }

  @Test
  void addNewFlightAndAirline() throws IOException {
    //AirlineRestClient not using (hostname, port) ! not talking to servlet yet
    Response response = mock(Response.class);
    String[] flightInfo = new String[]{"EVA Air","25","SEA","01/21/2023", "1:40 pm","SIN","01/21/2023", "6:40 pm"};
    HttpRequestHelper http = mock(HttpRequestHelper.class);
    AirlineRestClient client = new AirlineRestClient(http);
    client.addFlightToAirline(flightInfo);
  }
*/
  private HttpRequestHelper.Response airlinesAsText(Airline airline) {
    StringWriter writer = new StringWriter();
    new XmlDumper(writer).dump(airline);
    //new TextDumper(writer).dump(dictionary);

    return new HttpRequestHelper.Response(writer.toString());
  }
}

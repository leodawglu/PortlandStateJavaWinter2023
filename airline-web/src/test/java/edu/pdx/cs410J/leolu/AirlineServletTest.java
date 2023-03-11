package edu.pdx.cs410J.leolu;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AirlineServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
class AirlineServletTest {


  @Test
  void gettingFlightsForNonExistentAirlineReturns404() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn("airline=Flyaway&src=&dest=LAX");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("DOESNOTEXIST");
    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    assertThat(servlet.errorMsgForTesting,containsString("Airline name"));
  }
  @Test
  void getEmptyQueryStringReturns412() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn(null);
    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doGet(request, response);
    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED,"HTTP 412 | Query String was empty." );
  }

  @Test
  void postEmptyQueryStringReturns412() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn(null);
    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);
    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED,"HTTP 412 | Query String was empty." );
  }

  @Test
  void noDefinedAirlineNameReturns412() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn("src=PDX&dest=LAX");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("");
    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
    assertThat(servlet.errorMsgForTesting,
            containsString("Airline name was not specified in query string:"));
  }

  @Test
  void definedDestButNoSrcReturns412() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    servlet.addNewAirlineToMap("Flyaway");

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn("airline=Flyaway&src=&dest=LAX");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("Flyaway");
    when(request.getParameter(AirlineServlet.SOURCE_PARAM)).thenReturn("");
    when(request.getParameter(AirlineServlet.DESTINATION_PARAM)).thenReturn("LAX");

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
    assertThat(servlet.errorMsgForTesting,
            containsString("Query string \"dest\" is defined, but \"src\" was not defined! :"));
  }

  @Test
  void definedSrcButNoDestReturns412() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    servlet.addNewAirlineToMap("Flyaway");
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn("airline=Flyaway&src=PDX&dest=");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("Flyaway");
    when(request.getParameter(AirlineServlet.SOURCE_PARAM)).thenReturn("PDX");
    when(request.getParameter(AirlineServlet.DESTINATION_PARAM)).thenReturn("");

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
    assertThat(servlet.errorMsgForTesting,
            containsString("Query string \"src\" is defined, but \"dest\" was not defined! :"));
  }

  @Test
  void invalidSrcReturns400() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    servlet.addNewAirlineToMap("Flyaway");
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn("airline=Flyaway&src=P1X&dest=LAX");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("Flyaway");
    when(request.getParameter(AirlineServlet.SOURCE_PARAM)).thenReturn("P1X");
    when(request.getParameter(AirlineServlet.DESTINATION_PARAM)).thenReturn("LAX");

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    //verify(response).setStatus(400);
    assertThat(servlet.errorMsgForTesting,
            containsString("Departure airport code is invalid, src:"));
  }

  @Test
  void invalidDestReturns400() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    servlet.addNewAirlineToMap("Flyaway");

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn("airline=Flyaway&src=PDX&dest=L1X");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("Flyaway");

    when(request.getParameter(AirlineServlet.SOURCE_PARAM)).thenReturn("PDX");
    when(request.getParameter(AirlineServlet.DESTINATION_PARAM)).thenReturn("L1X");

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    assertThat(servlet.errorMsgForTesting,
            containsString("Arrival airport code is invalid, dest:"));
  }

  @Test
  void extraneousParamsInGetQueryStringReturns400() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    servlet.addNewAirlineToMap("Flyaway");
    Map<String, String[]> extraneousMap = new HashMap<>();
    extraneousMap.put("Extraneous", new String[]{});
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn("airline=Flyaway&src=PDX&dest=LAX&Extraneous=stuff");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("Flyaway");
    when(request.getParameter(AirlineServlet.SOURCE_PARAM)).thenReturn("PDX");
    when(request.getParameter(AirlineServlet.DESTINATION_PARAM)).thenReturn("LAX");
    when(request.getParameterMap()).thenReturn(extraneousMap);

    HttpServletResponse response = mock(HttpServletResponse.class);
    servlet.doGet(request, response);
    //verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    verify(response).setStatus(400);
    assertThat(servlet.errorMsgForTesting,
            containsString("In search GET request, extraneous parameter was found in query string:"));
  }

  @Test
  void extraneousParamsInPostQueryStringReturns400() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    servlet.addNewAirlineToMap("Flyaway");
    Map<String, String[]> extraneousMap = new HashMap<>();
    extraneousMap.put("Extraneous", new String[]{});
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    when(request.getParameterMap()).thenReturn(extraneousMap);
    servlet.checkQueryStringForExtraneousParams(request, response,"post");
    assertThat(servlet.errorMsgForTesting,
            containsString("In add POST request, extraneous parameter was found in query string:"));
  }

  @Test
  void airlineDoesNotContainFlightsWithMatchingSrcAndDestReturns404() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    servlet.addNewAirlineToMap("Flyaway");
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn("airline=Flyaway&src=PDX&dest=LAX");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("Flyaway");
    when(request.getParameter(AirlineServlet.SOURCE_PARAM)).thenReturn("PDX");
    when(request.getParameter(AirlineServlet.DESTINATION_PARAM)).thenReturn("LAX");

    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);
    //verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    assertThat(servlet.errorMsgForTesting,
            containsString("could not be found for"));
  }

  @Test
  void getWrittenAirlineResponse() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    HttpServletResponse response = mock(HttpServletResponse.class);
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    when(response.getWriter()).thenReturn(pw);

    ArrayList<Flight> flights = new ArrayList<>();
    flights.add(new Flight("26","SEA","01/23/2023", "10:40 am","JFK","01/23/2023", "11:40 pm"));
    Airline airline = new Airline("EVA Air",flights);

    servlet.writeAirlineAndFlightsToResponse(response,airline,HttpServletResponse.SC_OK);
    verify(response).setStatus(HttpServletResponse.SC_OK);
    assertThat(sw.toString(),equalTo("<?xml version=\"1.0\" encoding=\"us-ascii\" standalone=\"no\"?>\n" +
            "<!DOCTYPE airline SYSTEM \"http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd\">\n" +
            "<airline>\n" +
            "    <name>EVA Air</name>\n" +
            "    <flight>\n" +
            "        <number>26</number>\n" +
            "        <src>SEA</src>\n" +
            "        <depart>\n" +
            "            <date day=\"23\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"10\" minute=\"40\"/>\n" +
            "        </depart>\n" +
            "        <dest>JFK</dest>\n" +
            "        <arrive>\n" +
            "            <date day=\"23\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"23\" minute=\"40\"/>\n" +
            "        </arrive>\n" +
            "    </flight>\n" +
            "</airline>\n"));
  }

  @Test
  void getAllFlightsFromAirline() throws IOException{
    AirlineServlet servlet = new AirlineServlet();
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpServletRequest request = mock(HttpServletRequest.class);

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    when(response.getWriter()).thenReturn(pw);
    when(request.getQueryString()).thenReturn("airline=EVA Air");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("EVA Air");

    ArrayList<Flight> flights = new ArrayList<>();
    flights.add(new Flight("26","SEA","01/23/2023", "10:40 am","JFK","01/23/2023", "11:40 pm"));
    flights.add(new Flight("25","TPE","01/22/2023", "10:40 am","JFK","01/22/2023", "11:40 pm"));
    flights.add(new Flight("24","SEA","01/21/2023", "10:40 am","TPE","01/21/2023", "11:40 pm"));
    Airline airline = new Airline("EVA Air",flights);

    servlet.addAirlineToMap(airline);
    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_OK);
    assertThat(sw.toString(),equalTo("<?xml version=\"1.0\" encoding=\"us-ascii\" standalone=\"no\"?>\n" +
            "<!DOCTYPE airline SYSTEM \"http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd\">\n" +
            "<airline>\n" +
            "    <name>EVA Air</name>\n" +
            "    <flight>\n" +
            "        <number>24</number>\n" +
            "        <src>SEA</src>\n" +
            "        <depart>\n" +
            "            <date day=\"21\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"10\" minute=\"40\"/>\n" +
            "        </depart>\n" +
            "        <dest>TPE</dest>\n" +
            "        <arrive>\n" +
            "            <date day=\"21\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"23\" minute=\"40\"/>\n" +
            "        </arrive>\n" +
            "    </flight>\n" +
            "    <flight>\n" +
            "        <number>26</number>\n" +
            "        <src>SEA</src>\n" +
            "        <depart>\n" +
            "            <date day=\"23\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"10\" minute=\"40\"/>\n" +
            "        </depart>\n" +
            "        <dest>JFK</dest>\n" +
            "        <arrive>\n" +
            "            <date day=\"23\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"23\" minute=\"40\"/>\n" +
            "        </arrive>\n" +
            "    </flight>\n" +
            "    <flight>\n" +
            "        <number>25</number>\n" +
            "        <src>TPE</src>\n" +
            "        <depart>\n" +
            "            <date day=\"22\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"10\" minute=\"40\"/>\n" +
            "        </depart>\n" +
            "        <dest>JFK</dest>\n" +
            "        <arrive>\n" +
            "            <date day=\"22\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"23\" minute=\"40\"/>\n" +
            "        </arrive>\n" +
            "    </flight>\n" +
            "</airline>\n"));
  }

  @Test
  void getFilteredFlightsBasedOnSRCAndDESTFromAirline() throws IOException{
    AirlineServlet servlet = new AirlineServlet();
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpServletRequest request = mock(HttpServletRequest.class);

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    when(response.getWriter()).thenReturn(pw);
    when(request.getQueryString()).thenReturn("airline=EVA Air");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("EVA Air");
    when(request.getParameter(AirlineServlet.SOURCE_PARAM)).thenReturn("TPE");
    when(request.getParameter(AirlineServlet.DESTINATION_PARAM)).thenReturn("SIN");

    ArrayList<Flight> flights = new ArrayList<>();
    flights.add(new Flight("26","SEA","01/23/2023", "10:40 am","JFK","01/23/2023", "11:40 pm"));
    flights.add(new Flight("25","TPE","01/22/2023", "10:40 am","JFK","01/22/2023", "11:40 pm"));
    flights.add(new Flight("24","SEA","01/21/2023", "10:40 am","TPE","01/21/2023", "11:40 pm"));
    flights.add(new Flight("52","TPE","01/22/2023", "10:40 am","SIN","01/22/2023", "3:40 pm"));
    flights.add(new Flight("55","TPE","01/21/2023", "1:40 pm","SIN","01/21/2023", "6:40 pm"));
    Airline airline = new Airline("EVA Air",flights);

    servlet.addAirlineToMap(airline);
    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_OK);
    assertThat(sw.toString(),equalTo("<?xml version=\"1.0\" encoding=\"us-ascii\" standalone=\"no\"?>\n" +
            "<!DOCTYPE airline SYSTEM \"http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd\">\n" +
            "<airline>\n" +
            "    <name>EVA Air</name>\n" +
            "    <flight>\n" +
            "        <number>55</number>\n" +
            "        <src>TPE</src>\n" +
            "        <depart>\n" +
            "            <date day=\"21\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"13\" minute=\"40\"/>\n" +
            "        </depart>\n" +
            "        <dest>SIN</dest>\n" +
            "        <arrive>\n" +
            "            <date day=\"21\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"18\" minute=\"40\"/>\n" +
            "        </arrive>\n" +
            "    </flight>\n" +
            "    <flight>\n" +
            "        <number>52</number>\n" +
            "        <src>TPE</src>\n" +
            "        <depart>\n" +
            "            <date day=\"22\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"10\" minute=\"40\"/>\n" +
            "        </depart>\n" +
            "        <dest>SIN</dest>\n" +
            "        <arrive>\n" +
            "            <date day=\"22\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"15\" minute=\"40\"/>\n" +
            "        </arrive>\n" +
            "    </flight>\n" +
            "</airline>\n"));
  }

  @Test
  void addNewAirlineAndNewFlight() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpServletRequest request = mock(HttpServletRequest.class);

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    when(response.getWriter()).thenReturn(pw);
    when(request.getQueryString()).thenReturn("airline=EVA Air&flightNumber=25&src=TPE&depart=10:40%20am&dest=SIN&arrive=3:40%20pm");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("EVA Air");
    when(request.getParameter(AirlineServlet.SOURCE_PARAM)).thenReturn("TPE");
    when(request.getParameter(AirlineServlet.DESTINATION_PARAM)).thenReturn("SIN");
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAM)).thenReturn("25");
    when(request.getParameter(AirlineServlet.DEPARTURE_DATETIME)).thenReturn("01/22/2023 10:40 am");
    when(request.getParameter(AirlineServlet.ARRIVAL_DATETIME)).thenReturn("01/22/2023 3:40 pm");

    servlet.doPost(request, response);
    verify(response).setStatus(HttpServletResponse.SC_CREATED);

    assertThat(sw.toString(),equalTo("<?xml version=\"1.0\" encoding=\"us-ascii\" standalone=\"no\"?>\n" +
            "<!DOCTYPE airline SYSTEM \"http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd\">\n" +
            "<airline>\n" +
            "    <name>EVA Air</name>\n" +
            "    <flight>\n" +
            "        <number>25</number>\n" +
            "        <src>TPE</src>\n" +
            "        <depart>\n" +
            "            <date day=\"22\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"10\" minute=\"40\"/>\n" +
            "        </depart>\n" +
            "        <dest>SIN</dest>\n" +
            "        <arrive>\n" +
            "            <date day=\"22\" month=\"01\" year=\"2023\"/>\n" +
            "            <time hour=\"15\" minute=\"40\"/>\n" +
            "        </arrive>\n" +
            "    </flight>\n" +
            "</airline>\n"));
  }

  @Test
  void addNewFlightToExistingAirline() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpServletRequest request = mock(HttpServletRequest.class);

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    when(response.getWriter()).thenReturn(pw);
    when(request.getQueryString()).thenReturn("airline=EVA Air&flightNumber=25&src=TPE&depart=10:40%20am&dest=SIN&arrive=3:40%20pm");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("EVA Air");
    when(request.getParameter(AirlineServlet.SOURCE_PARAM)).thenReturn("TPE");
    when(request.getParameter(AirlineServlet.DESTINATION_PARAM)).thenReturn("SIN");
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAM)).thenReturn("58");
    when(request.getParameter(AirlineServlet.DEPARTURE_DATETIME)).thenReturn("01/22/2023 10:40 am");
    when(request.getParameter(AirlineServlet.ARRIVAL_DATETIME)).thenReturn("01/22/2023 3:40 pm");

    ArrayList<Flight> flights = new ArrayList<>();
    flights.add(new Flight("26","SEA","01/23/2023", "10:40 am","JFK","01/23/2023", "11:40 pm"));
    flights.add(new Flight("25","TPE","01/22/2023", "10:40 am","JFK","01/22/2023", "11:40 pm"));
    flights.add(new Flight("24","SEA","01/21/2023", "10:40 am","TPE","01/21/2023", "11:40 pm"));
    flights.add(new Flight("52","TPE","01/22/2023", "10:40 am","SIN","01/22/2023", "3:40 pm"));
    flights.add(new Flight("55","TPE","01/21/2023", "1:40 pm","SIN","01/21/2023", "6:40 pm"));
    Airline airline = new Airline("EVA Air",flights);

    servlet.addAirlineToMap(airline);
    assertThat(servlet.getAirline("EVA Air").getFlights().size(),equalTo(5));
    servlet.doPost(request, response);
    verify(response).setStatus(HttpServletResponse.SC_CREATED);
    assertThat(servlet.getAirline("EVA Air").getFlights().size(),equalTo(6));
  }

  @Test
  void badDepartureDateTimeReturns400() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpServletRequest request = mock(HttpServletRequest.class);

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    when(response.getWriter()).thenReturn(pw);
    when(request.getQueryString()).thenReturn("airline=EVA Air&flightNumber=25&src=TPE&depart=10:40%20am&dest=SIN&arrive=3:40%20pm");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("EVA Air");
    when(request.getParameter(AirlineServlet.SOURCE_PARAM)).thenReturn("TPE");
    when(request.getParameter(AirlineServlet.DESTINATION_PARAM)).thenReturn("SIN");
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAM)).thenReturn("58");
    when(request.getParameter(AirlineServlet.DEPARTURE_DATETIME)).thenReturn("01/22/2023 10:40 m");
    when(request.getParameter(AirlineServlet.ARRIVAL_DATETIME)).thenReturn("01/22/2023 3:40 pm");
    servlet.doPost(request, response);
    verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    assertThat(servlet.errorMsgForTesting,
            containsString("is not a correctly formatted datetime"));
  }

  @Test
  void badFlightNumberReturns400() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpServletRequest request = mock(HttpServletRequest.class);

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    when(response.getWriter()).thenReturn(pw);
    when(request.getQueryString()).thenReturn("airline=EVA Air&flightNumber=25&src=TPE&depart=10:40%20am&dest=SIN&arrive=3:40%20pm");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("EVA Air");
    when(request.getParameter(AirlineServlet.SOURCE_PARAM)).thenReturn("TPE");
    when(request.getParameter(AirlineServlet.DESTINATION_PARAM)).thenReturn("SIN");
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAM)).thenReturn("5x8");
    when(request.getParameter(AirlineServlet.DEPARTURE_DATETIME)).thenReturn("01/22/2023 10:40 am");
    when(request.getParameter(AirlineServlet.ARRIVAL_DATETIME)).thenReturn("01/22/2023 3:40 pm");
    servlet.doPost(request, response);
    verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    assertThat(servlet.errorMsgForTesting,
            containsString("is not a integer number"));
  }

}

package edu.pdx.cs410J.leolu;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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

  /*
  @Test
  void initiallyServletContainsNoDictionaryEntries() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    // Nothing is written to the response's PrintWriter
    verify(pw, never()).println(anyString());
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }*/

  /*Corresponds to writeAllDictionaryEntries method*/

  @Test
  void gettingFlightsForNonExistentAirlineReturns404() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn("airline=Flyaway&src=&dest=LAX");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("DOESNOTEXIST");
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    assertThat(servlet.errorMsgForTesting,containsString("Airline name was not found:"));
  }
  @Test
  void emptyQueryStringReturns400() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn(null);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);
    //verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    verify(response).sendError(400,"Query String was empty.");
  }

  @Test
  void noDefinedAirlineNameReturns400() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn("src=PDX&dest=LAX");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("");
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    assertThat(servlet.errorMsgForTesting,
            containsString("Airline name was not specified in query string:"));
  }

  @Test
  void definedDestButNoSrcReturns400() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    servlet.addNewAirlineToMap("Flyaway");

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn("airline=Flyaway&src=&dest=LAX");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("Flyaway");
    when(request.getParameter(AirlineServlet.SOURCE_PARAM)).thenReturn("");
    when(request.getParameter(AirlineServlet.DESTINATION_PARAM)).thenReturn("LAX");

    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);
    //verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    verify(response).setStatus(400);
    assertThat(servlet.errorMsgForTesting,
            containsString("Query string \"dest\" is defined, but \"src\" was not defined! :"));
  }

  @Test
  void definedSrcButNoDestReturns400() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    servlet.addNewAirlineToMap("Flyaway");
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getQueryString()).thenReturn("airline=Flyaway&src=PDX&dest=");
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn("Flyaway");
    when(request.getParameter(AirlineServlet.SOURCE_PARAM)).thenReturn("PDX");
    when(request.getParameter(AirlineServlet.DESTINATION_PARAM)).thenReturn("");

    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    //verify(response).setStatus(400);
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
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

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
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    assertThat(servlet.errorMsgForTesting,
            containsString("Departure airport code is invalid, dest:"));
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
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

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
    verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    //verify(response).setStatus(404);
    assertThat(servlet.errorMsgForTesting,
            containsString("could not be found for"));
  }

  @Test
  void addFlightInNewAirline() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "Airline";
    int flightNumber = 123;
    String flightNumberAsString = String.valueOf(flightNumber);

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAM)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAM)).thenReturn(flightNumberAsString);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    String xml = stringWriter.toString();
    assertThat(xml,containsString(airlineName));
    assertThat(xml,containsString(flightNumberAsString));

    assertThat(stringWriter.toString(), containsString(Messages.definedWordAs(airlineName, flightNumberAsString)));

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));
    Airline airline = servlet.getAirline(airlineName);
    assertThat(airline.getName(), equalTo(airlineName));

    Flight flight = airline.getFlights().iterator().next();
    assertThat(flight.getNumber(),equalTo(flightNumber));
  }
/*
  @Test
  void addOneWordToDictionary() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    String word = "TEST WORD";
    String definition = "TEST DEFINITION";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(word);
    when(request.getParameter(AirlineServlet.DEFINITION_PARAMETER)).thenReturn(definition);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    assertThat(stringWriter.toString(), containsString(Messages.definedWordAs(word, definition)));

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));

    assertThat(servlet.getDefinition(word), equalTo(definition));
  }
*/
}

package edu.pdx.cs410J.leolu;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.AirportNames;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>Airline</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class AirlineServlet extends HttpServlet {

  static final String AIRLINE_NAME_PARAM = "airline", SOURCE_PARAM = "src" , DESTINATION_PARAM = "dest",
          FLIGHT_NUMBER_PARAMETER = "flightNumber", DEPARTURE_DATETIME = "depart", ARRIVAL_DATETIME = "arrive";


    // K,V : airlineName : Airline Object
  private final Map<String, Airline> airlines = new HashMap<>();

  /**
   * Handles an HTTP GET request from a client by writing the definition of the
   * word specified in the "word" HTTP parameter to the HTTP response.  If the
   * "word" parameter is not specified, all of the entries in the dictionary
   * are written to the HTTP response.
   */
  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
      //response.setContentType( "text/plain" );
      Airline air = null;
      String queryString = request.getQueryString();// Use to decide what kind of request it is for
      String airlineName = request.getParameter(AIRLINE_NAME_PARAM);
      String src = request.getParameter(SOURCE_PARAM);
      String dest = request.getParameter(DESTINATION_PARAM);
      if(queryString == null || queryString.length()==0){
          responseSetStatusAndSendError(response,HttpServletResponse.SC_BAD_REQUEST,"Query String was empty." );
          return;
      }

      checkQueryStringForExtraneousParams(request,response,"get");

      if(airlineName == null || airlineName.length()==0){
          responseSetStatusAndSendError(response,HttpServletResponse.SC_BAD_REQUEST,"Airline name " +
                  "was not specified in query string: " + queryString );
          return;
      }
      if(src!=null || dest!=null){
          getFlightsWithSpecificSRCAndDest(response,queryString,airlineName,src,dest);
      }

      /*
      String word = getParameter( AIRLINE_NAME_PARAMETER, request );
      if (word != null) {
          writeDefinition(word, response);

      } else {
          writeAllDictionaryEntries(response);
      }*/
  }

    private void getFlightsWithSpecificSRCAndDest(HttpServletResponse response, String queryString, String airlineName, String src, String dest) throws IOException {
        if((src==null||src.length()==0)&& dest!=null){
            responseSetStatusAndSendError(response,HttpServletResponse.SC_BAD_REQUEST,"Query string \"dest\" is defined, " +
                    "but \"src\" was not defined! : " + queryString);
            return;
        }
        if((dest==null||dest.length()==0)&& src!=null){
            responseSetStatusAndSendError(response,HttpServletResponse.SC_BAD_REQUEST,"Query string \"src\" is defined, " +
                    "but \"dest\" was not defined! : " + queryString);
            return;
        }

    }

    private boolean isRealAirportCode(String code) {
        return AirportNames.getNamesMap().containsKey(code);
    }
    private void checkQueryStringForExtraneousParams(HttpServletRequest request, HttpServletResponse response, String type) throws IOException {
      Map<String, String[]> paramMap = request.getParameterMap();
      Iterator<String> iterate = paramMap.keySet().iterator();
      if(type.equals("get")){
          while(iterate.hasNext()){
              String key = iterate.next();
              if(key!=AIRLINE_NAME_PARAM && key!=SOURCE_PARAM && key!=DESTINATION_PARAM){
                  responseSetStatusAndSendError(response,HttpServletResponse.SC_BAD_REQUEST,
                          "In search request, extraneous parameter was found in query string: " + request.getQueryString());
                  return;
              }
          }
      }else{//POST check params for adding flights

      }
    }

    private void responseSetStatusAndSendError(HttpServletResponse response, int status, String msg) throws IOException {
      response.setStatus(status);
      response.sendError(status,msg);
  }

  /*
  Example http request url
  https://www.example.com/products?category=electronics&price_min=100&price_max=500

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    // Get the request URI
    String uri = request.getRequestURI(); // Returns "/products"

    // Get the query string
    String queryString = request.getQueryString(); // Returns "category=electronics&price_min=100&price_max=500"

    // Get a single parameter value
    String category = request.getParameter("category"); // Returns "electronics"

    // Get all parameter values as a map
    Map<String, String[]> paramMap = request.getParameterMap(); // Returns a map containing all parameters and their values
MAP:
{
    "category": ["electronics"],
    "price_min": ["100"],
    "price_max": ["500"]
}
    // Do something with the retrieved information...
}

  * */

  /**
   * Handles an HTTP POST request by storing the dictionary entry for the
   * "word" and "definition" request parameters.  It writes the dictionary
   * entry to the HTTP response.
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
      response.setContentType( "text/plain" );

      String airlineName = getParameter(AIRLINE_NAME_PARAM, request );
      if (airlineName == null) {
          missingRequiredParameter(response, AIRLINE_NAME_PARAM);
          return;
      }

      String flightNumberAsString = getParameter(FLIGHT_NUMBER_PARAMETER, request );
      if ( flightNumberAsString == null) {
          missingRequiredParameter( response, FLIGHT_NUMBER_PARAMETER );
          return;
      }

      Airline airline = this.airlines.get(airlineName);
      if(airline==null){
          airline = new Airline(airlineName);
          this.airlines.put(airlineName,airline);
      }
      Flight fl = new Flight();
      fl.setFlightNumber(flightNumberAsString);
      airline.addFlight(fl);


      PrintWriter pw = response.getWriter();
      pw.println(Messages.definedWordAs(airlineName, flightNumberAsString));
      pw.flush();

      response.setStatus( HttpServletResponse.SC_OK);
  }

  /**
   * Handles an HTTP DELETE request by removing all dictionary entries.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.setContentType("text/plain");

      this.airlines.clear();

      PrintWriter pw = response.getWriter();
      pw.println(Messages.allDictionaryEntriesDeleted());
      pw.flush();

      response.setStatus(HttpServletResponse.SC_OK);

  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   *
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
      throws IOException
  {
      String message = Messages.missingRequiredParameter(parameterName);
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /**
   * Writes the definition of the given word to the HTTP response.
   *
   * The text of the message is formatted with {@link TextDumper}
   */
  private void writeDefinition(String airlineName, HttpServletResponse response) throws IOException {
    Airline airways = this.airlines.get(airlineName);

    if (airways == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);

    } else {
      PrintWriter pw = response.getWriter();
/*
      Map<String, Airline> wordDefinition = Map.of(airlineName, airways);
      TextDumper dumper = new TextDumper(pw);
      dumper.dump(wordDefinition);
*/
      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  /**
   * Writes all of the dictionary entries to the HTTP response.
   *
   * The text of the message is formatted with {@link TextDumper}
   */
  private void writeAllDictionaryEntries(HttpServletResponse response ) throws IOException
  {
      PrintWriter pw = response.getWriter();
      /*
      TextDumper dumper = new TextDumper(pw);
      dumper.dump(dictionary);*/

      response.setStatus( HttpServletResponse.SC_OK );
  }

  /**
   * Writes all of the airlines to HTTP response
   * */
  private void writeAirlines(HttpServletResponse response, Airline... airlines) throws IOException{

  }

  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }

  @VisibleForTesting
  Airline getAirline(String airlineName) {
      return this.airlines.get(airlineName);
  }
}

package edu.pdx.cs410J.leolu;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:mm a");
    static final String AIRLINE_NAME_PARAM = "airline", SOURCE_PARAM = "src" , DESTINATION_PARAM = "dest",
            FLIGHT_NUMBER_PARAM = "flightNumber", DEPARTURE_DATETIME = "depart", ARRIVAL_DATETIME = "arrive";


    // K,V : airlineName , Airline Object
    private final Map<String, Airline> airlines = new HashMap<>();
    protected String errorMsgForTesting="";

    /**
     * Handles an HTTP GET request from a client by writing the definition of the
     * word specified in the "word" HTTP parameter to the HTTP response.  If the
     * "word" parameter is not specified, all of the entries in the dictionary
     * are written to the HTTP response.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        String queryString = request.getQueryString();// Use to decide what kind of request it is for
        String airlineName = request.getParameter(AIRLINE_NAME_PARAM);
        String src = request.getParameter(SOURCE_PARAM);
        String dest = request.getParameter(DESTINATION_PARAM);
        if(queryString == null || queryString.length()==0){
            responseSetStatusAndSendError(response,HttpServletResponse.SC_PRECONDITION_FAILED,"Query String was empty." );
            return;
        }

        if(!checkQueryStringForExtraneousParams(request,response,"get"))return;

        if (!validateParam(response, queryString, airlineName, AIRLINE_NAME_PARAM)) return;

        if(!airlines.containsKey(airlineName)){
            responseSetStatusAndSendError(response,HttpServletResponse.SC_NOT_FOUND,"Airline name " +
                    "was not found: " + airlineName );
            return;
        }
        if(src!=null || dest!=null){
            getFlightsWithSpecificSRCAndDest(response,queryString,airlineName,src,dest);
            return;
        }
        //When SRC & DEST not specified, get all flights from airline:
        writeAirlineAndFlightsToResponse(response,airlines.get(airlineName),HttpServletResponse.SC_OK);
    }

    private void getFlightsWithSpecificSRCAndDest(HttpServletResponse response, String queryString, String airlineName, String src, String dest) throws IOException {
        if (validateSRCAndDEST(response, queryString, src, dest)) return;
        Airline requestedAirline = airlines.get(airlineName);
        Airline filteredAirlineWithMatchingFlights = new Airline(airlineName);
        for(Flight fl: requestedAirline.getFlights()){
            if(fl.getSource().equals(src)&& fl.getDestination().equals(dest))
                filteredAirlineWithMatchingFlights.addFlight(fl);
        }
        if(filteredAirlineWithMatchingFlights.getFlights().isEmpty()){
            responseSetStatusAndSendError(response,HttpServletResponse.SC_NOT_FOUND,
                    "Flights with departure airport " + src +
                            " and arrival airport " + dest +
                            " could not be found for " + airlineName );
            return;
        }
        writeAirlineAndFlightsToResponse(response,filteredAirlineWithMatchingFlights,HttpServletResponse.SC_OK);
    }

    private boolean validateSRCAndDEST(HttpServletResponse response, String queryString, String src, String dest) throws IOException {
        if((src ==null|| src.length()==0)&& dest !=null){
            responseSetStatusAndSendError(response,HttpServletResponse.SC_PRECONDITION_FAILED,"Query string \"dest\" is defined, " +
                    "but \"src\" was not defined! : " + queryString);
            return true;
        }
        if((dest ==null|| dest.length()==0)&& src !=null){
            responseSetStatusAndSendError(response,HttpServletResponse.SC_PRECONDITION_FAILED,"Query string \"src\" is defined, " +
                    "but \"dest\" was not defined! : " + queryString);
            return true;
        }

        if(!isRealAirportCode(src)){
            responseSetStatusAndSendError(response,HttpServletResponse.SC_BAD_REQUEST,
                    "Departure airport code is invalid, src: " + src);
            return true;
        }
        if(!isRealAirportCode(dest)){
            responseSetStatusAndSendError(response,HttpServletResponse.SC_BAD_REQUEST,
                    "Arrival airport code is invalid, dest: " + dest);
            return true;
        }
        return false;
    }

    protected void writeAirlineAndFlightsToResponse(HttpServletResponse response, Airline airline, int status) throws IOException {
        response.setContentType("application/xml;charset=us-ascii");
        PrintWriter pw = response.getWriter();
        XmlDumper dumper = new XmlDumper(pw);
        dumper.dump(airline);

        response.setStatus(status);
    }

    protected void addNewAirlineToMap(String airlineName){
        airlines.put(airlineName,new Airline(airlineName));
    }
    protected void addAirlineToMap(Airline airline){airlines.put(airline.getName(),airline);}

    private boolean isRealAirportCode(String code) {
        return AirportNames.getNamesMap().containsKey(code);
    }
    protected boolean checkQueryStringForExtraneousParams(HttpServletRequest request, HttpServletResponse response, String type) throws IOException {
        Map<String, String[]> paramMap = request.getParameterMap();
        Iterator<String> iterate = paramMap.keySet().iterator();
        while(iterate.hasNext()){
            String key = iterate.next();
            if(type.equals("get")&&key!=AIRLINE_NAME_PARAM && key!=SOURCE_PARAM && key!=DESTINATION_PARAM){
                responseSetStatusAndSendError(response,HttpServletResponse.SC_BAD_REQUEST,
                        "In search GET request, extraneous parameter was found in query string: " + key);
                return false;
            }
            if(type.equals("post")&&key!=AIRLINE_NAME_PARAM && key!=SOURCE_PARAM && key!=DESTINATION_PARAM
                    &&key!= FLIGHT_NUMBER_PARAM && key!=DEPARTURE_DATETIME && key!=ARRIVAL_DATETIME){
                responseSetStatusAndSendError(response,HttpServletResponse.SC_BAD_REQUEST,
                        "In add POST request, extraneous parameter was found in query string: " + key);
                return false;
            }
        }
        return true;
    }

    private void responseSetStatusAndSendError(HttpServletResponse response, int status, String msg) throws IOException {
        response.setStatus(status);
        response.sendError(status,msg);
        errorMsgForTesting = msg;
    }

    /**
     * Handles an HTTP POST request by storing the dictionary entry for the
     * "word" and "definition" request parameters.  It writes the dictionary
     * entry to the HTTP response.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        String queryString = request.getQueryString();// Use to decide what kind of request it is for
        if(queryString == null || queryString.length()==0){
            responseSetStatusAndSendError(response,HttpServletResponse.SC_PRECONDITION_FAILED,"Query String was empty." );
            return;
        }
        if(!checkQueryStringForExtraneousParams(request,response,"post"))return;

        String airlineName = request.getParameter(AIRLINE_NAME_PARAM);
        if (!validateParam(response, queryString, airlineName, AIRLINE_NAME_PARAM)) return;

        String src = request.getParameter(SOURCE_PARAM);
        String dest = request.getParameter(DESTINATION_PARAM);
        if (validateSRCAndDEST(response, queryString, src, dest)) return;

        String flightNumber = request.getParameter(FLIGHT_NUMBER_PARAM);
        if (!validateParam(response, queryString, flightNumber, FLIGHT_NUMBER_PARAM)) return;
        String departureDateTime = request.getParameter(DEPARTURE_DATETIME);
        if (!validateParam(response, queryString, departureDateTime, DEPARTURE_DATETIME)) return;

        String arrivalDateTime = request.getParameter(ARRIVAL_DATETIME);
        if (!validateParam(response, queryString, arrivalDateTime, ARRIVAL_DATETIME)) return;

        String[] depDateTime = departureDateTime.split(" ");
        String[] arrDateTime = arrivalDateTime.split(" ");
        Flight newFlight = new Flight(flightNumber,src,depDateTime[0],depDateTime[1]+" "+depDateTime[2],
                dest,arrDateTime[0],arrDateTime[1]+" "+arrDateTime[2]);

        Airline responseAirline = new Airline(airlineName);
        responseAirline.addFlight(newFlight);
        writeAirlineAndFlightsToResponse(response,responseAirline ,HttpServletResponse.SC_CREATED);
        if(airlines.containsKey(airlineName))airlines.get(airlineName).addFlight(newFlight);
        else airlines.put(airlineName,responseAirline);
    }

    private boolean validateParam(HttpServletResponse response, String queryString, String param, String paramType) throws IOException {
        String type = "";
        if(paramType.equals(AIRLINE_NAME_PARAM)) type = "Airline name ";
        else if(paramType.equals(FLIGHT_NUMBER_PARAM)) type = "Flight number ";
        else if(paramType.equals(DEPARTURE_DATETIME)) type = "Departure datetime ";
        else if(paramType.equals(ARRIVAL_DATETIME)) type = "Arrival datetime ";

        if(param == null || param.length()==0){
            responseSetStatusAndSendError(response,HttpServletResponse.SC_PRECONDITION_FAILED,type +
                    "was not specified in query string: " + queryString);
            return false;
        }
        if(paramType.equals(DEPARTURE_DATETIME) || paramType.equals(ARRIVAL_DATETIME)){
            try{
                formatter.parse(param);
            }catch(ParseException e){
                responseSetStatusAndSendError(response,HttpServletResponse.SC_BAD_REQUEST,"\""+ param +"\""+
                        " is not a correctly formatted datetime: MM/DD/YYYY HH:MM AM|PM \n" +
                        "Please note only a single space is needed to separate MM/DD/YYYY, HH:MM, and AM|PM !");
                return false;
            }
        }
        if(paramType.equals(FLIGHT_NUMBER_PARAM)){
            try{
                Integer.parseInt(param);
            }catch(NumberFormatException e){
                responseSetStatusAndSendError(response,HttpServletResponse.SC_BAD_REQUEST,"\""+param +"\""+
                        " is not a integer number, and cannot be set as flight number.");
                return false;
            }
        }
        return true;
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

    @VisibleForTesting
    Airline getAirline(String airlineName) {
        return this.airlines.get(airlineName);
    }
}

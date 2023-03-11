package edu.pdx.cs410J.leolu;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.AirportNames;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    protected int codesCaught = 0;

    /**
     * Handles an HTTP GET request from a client by writing the definition of the
     * word specified in the "word" HTTP parameter to the HTTP response.  If the
     * "word" parameter is not specified, all of the entries in the dictionary
     * are written to the HTTP response.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        errorMsgForTesting="";
        boolean goodRequest = true;
        String queryString = request.getQueryString();// Use to decide what kind of request it is for
        String airlineName = request.getParameter(AIRLINE_NAME_PARAM);
        String src = request.getParameter(SOURCE_PARAM);
        String dest = request.getParameter(DESTINATION_PARAM);
        if(queryString == null || (queryString!=null&&queryString.length()==0)){
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED,"HTTP 412 | Query String was empty." );
            return;
        }

        goodRequest &= checkQueryStringForExtraneousParams(request,response,"get");
        //if(!checkQueryStringForExtraneousParams(request,response,"get"))return;
        goodRequest &= validateParam(response, queryString, airlineName, AIRLINE_NAME_PARAM);
        //if (!validateParam(response, queryString, airlineName, AIRLINE_NAME_PARAM)) return;

        if(!airlines.containsKey(airlineName)){
            responseSetStatusAndAddErrorMsg(response,HttpServletResponse.SC_NOT_FOUND,"Airline name " +
                    "was not found: " + airlineName );
            goodRequest = false;
        }
        if(src!=null || dest!=null){
            goodRequest &= validateSRCAndDEST(response, queryString, src, dest);
            if(!goodRequest){
                isBadRequest(response);
                return;
            }
            getFlightsWithSpecificSRCAndDest(response,queryString,airlineName,src.toUpperCase(),dest.toUpperCase());
        }else{
            //When SRC & DEST not specified, get all flights from airline:
            if(!goodRequest){
                isBadRequest(response);
                return;
            }
            writeAirlineAndFlightsToResponse(response,airlines.get(airlineName),HttpServletResponse.SC_OK);
        }

    }

    /**
     * Invoked when the GET requests is for specific SRC and DEST codes
     * */
    private void getFlightsWithSpecificSRCAndDest(HttpServletResponse response, String queryString, String airlineName, String src, String dest) throws IOException {
        Airline requestedAirline = airlines.get(airlineName);
        Airline filteredAirlineWithMatchingFlights = new Airline(airlineName);
        for(Flight fl: requestedAirline.getFlights()){
            if(fl.getSource().equals(src)&& fl.getDestination().equals(dest))
                filteredAirlineWithMatchingFlights.addFlight(fl);
        }
        if(filteredAirlineWithMatchingFlights.getFlights().isEmpty()){
            errorMsgForTesting += "HTTP 404 | Flights with departure airport " + src +
                    " and arrival airport " + dest +
                    " could not be found for " + airlineName +"\n";
            response.sendError(HttpServletResponse.SC_NOT_FOUND,errorMsgForTesting);
            return;
        }
        writeAirlineAndFlightsToResponse(response,filteredAirlineWithMatchingFlights,HttpServletResponse.SC_OK);
    }

    /**
     * Checks both SRC and DEST together for GET requests
     * Checks if either or both are null and tests if the airport codes are valid
     * */
    private boolean validateSRCAndDEST(HttpServletResponse response, String queryString, String src, String dest) throws IOException {
        boolean state = true;
        if(src==null && dest==null){
            responseSetStatusAndAddErrorMsg(response,HttpServletResponse.SC_PRECONDITION_FAILED,"Query string both \"src\" and \"dest\" " +
                    "are not defined! : " + queryString);
            state = false;
        }
        if((src ==null|| (src!=null&&src.length()==0))&& dest !=null){
            responseSetStatusAndAddErrorMsg(response,HttpServletResponse.SC_PRECONDITION_FAILED,"Query string \"dest\" is defined, " +
                    "but \"src\" was not defined! : " + queryString);
            state = false;
        }
        if((dest ==null|| (dest!=null&&dest.length()==0))&& src !=null){
            responseSetStatusAndAddErrorMsg(response,HttpServletResponse.SC_PRECONDITION_FAILED,"Query string \"src\" is defined, " +
                    "but \"dest\" was not defined! : " + queryString);
            state = false;
        }

        if(src!=null&&src.length()!=0 && !isRealAirportCode(src.toUpperCase())){
            responseSetStatusAndAddErrorMsg(response,HttpServletResponse.SC_BAD_REQUEST,
                    "Departure airport code is invalid, src: " + src);
            state = false;
        }
        if(dest!=null &&dest.length()!=0 && !isRealAirportCode(dest.toUpperCase())){
            responseSetStatusAndAddErrorMsg(response,HttpServletResponse.SC_BAD_REQUEST,
                    "Arrival airport code is invalid, dest: " + dest);
            state = false;
        }
        return state;
    }

    /**
     * Invoked when request is valid
     * Writes requested airline and flight(s) info to response in XML format using XmlDumper
     * */
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

    /**
     * Checks if the provided SRC or DEST airport codes are real airport codes
     * */
    private boolean isRealAirportCode(String code) {
        return AirportNames.getNamesMap().containsKey(code);
    }
    /**
     * Checks for extraneous parameter values in the request
     * */
    protected boolean checkQueryStringForExtraneousParams(HttpServletRequest request, HttpServletResponse response, String type) throws IOException {
        boolean state = true;
        Map<String, String[]> paramMap = request.getParameterMap();
        Iterator<String> iterate = paramMap.keySet().iterator();
        while(iterate.hasNext()){
            String key = iterate.next();
            if(type.equals("get")&&!key.equals(AIRLINE_NAME_PARAM)&&!key.equals(SOURCE_PARAM)&&!key.equals(DESTINATION_PARAM)){
                responseSetStatusAndAddErrorMsg(response,HttpServletResponse.SC_BAD_REQUEST,
                        "In search GET request, extraneous parameter was found in query string: " + key);
                state = false;
            }
            if(type.equals("post")&&!key.equals(AIRLINE_NAME_PARAM)&&!key.equals(SOURCE_PARAM)&&!key.equals(DESTINATION_PARAM)
                    &&!key.equals(FLIGHT_NUMBER_PARAM)&&!key.equals(DEPARTURE_DATETIME)&&!key.equals(ARRIVAL_DATETIME)){
                responseSetStatusAndAddErrorMsg(response,HttpServletResponse.SC_BAD_REQUEST,
                        "In add POST request, extraneous parameter was found in query string: " + key);
                state = false;
            }
        }
        return state;
    }

    /**
     * Sets error status and adds error message to errorMsgForTesting log
     * */
    private void responseSetStatusAndAddErrorMsg(HttpServletResponse response, int status, String msg) throws IOException {
        response.setStatus(status);
        codesCaught++;
        errorMsgForTesting += (new StringBuilder("HTTP " + status + " | " + msg + "\n")).toString();
    }

    /**
     * Handles an HTTP POST request by checking each parameter value for validity
     * Creates a new flight along with a temporary airline if valid
     * Temporary airline is added to map if airline does not exist yet
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        errorMsgForTesting="";
        boolean goodRequest = true;
        String queryString = request.getQueryString();// Use to decide what kind of request it is for
        /*
        if(queryString == null || queryString.length()==0){
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED,"HTTP 412 | Query String was empty." );
            return;
        }*/
        goodRequest &= checkQueryStringForExtraneousParams(request,response,"post");
        //if(!checkQueryStringForExtraneousParams(request,response,"post"))return;

        String airlineName = request.getParameter(AIRLINE_NAME_PARAM);
        goodRequest &= validateParam(response, queryString, airlineName, AIRLINE_NAME_PARAM);
        //if (!validateParam(response, queryString, airlineName, AIRLINE_NAME_PARAM)) return;

        String src = request.getParameter(SOURCE_PARAM);
        String dest = request.getParameter(DESTINATION_PARAM);
        goodRequest &= validateSRCAndDEST(response, queryString, src, dest);
        //if (validateSRCAndDEST(response, queryString, src, dest)) return;

        String flightNumber = request.getParameter(FLIGHT_NUMBER_PARAM);
        goodRequest &= validateParam(response, queryString, flightNumber, FLIGHT_NUMBER_PARAM);
        //if (!validateParam(response, queryString, flightNumber, FLIGHT_NUMBER_PARAM)) return;
        String departureDateTime = request.getParameter(DEPARTURE_DATETIME);
        goodRequest &= validateParam(response, queryString, departureDateTime, DEPARTURE_DATETIME);
        //if (!validateParam(response, queryString, departureDateTime, DEPARTURE_DATETIME)) return;

        String arrivalDateTime = request.getParameter(ARRIVAL_DATETIME);
        goodRequest &= validateParam(response, queryString, arrivalDateTime, ARRIVAL_DATETIME);
        //if (!validateParam(response, queryString, arrivalDateTime, ARRIVAL_DATETIME)) return;

        if(!goodRequest){
            isBadRequest(response);
            return;
        }

        String[] depDateTime = departureDateTime.split(" ");
        String[] arrDateTime = arrivalDateTime.split(" ");
        Flight newFlight = new Flight(flightNumber,src.toUpperCase(),depDateTime[0],depDateTime[1]+" "+depDateTime[2],
                dest.toUpperCase(),arrDateTime[0],arrDateTime[1]+" "+arrDateTime[2]);

        Airline responseAirline = new Airline(airlineName);
        responseAirline.addFlight(newFlight);
        writeAirlineAndFlightsToResponse(response,responseAirline ,HttpServletResponse.SC_CREATED);
        if(airlines.containsKey(airlineName))airlines.get(airlineName).addFlight(newFlight);
        else airlines.put(airlineName,responseAirline);
    }

    /**
     * Invoked when the Get or Post request is bad
     * Chooses the appropriate error to send within the response
     * @param response HttpServletResponse object
     * */
    private void isBadRequest(HttpServletResponse response) throws IOException {
        if(codesCaught>1) response.sendError(HttpServletResponse.SC_BAD_REQUEST,errorMsgForTesting);
        else{
            response.sendError(response.getStatus(),errorMsgForTesting);
        }
    }

    /**
     * This method validates the value of the specified parameter value in the request
     * If the value is not acceptable, a HTTP status and a message is added to the HTTP response
     * @param response HttpServletResponse
     * @param queryString String request.queryString()
     * @param param String specified parameter value
     * @param paramType String, example: "Airline name "
     * */
    private boolean validateParam(HttpServletResponse response, String queryString, String param, String paramType) throws IOException {
        String type = "";
        if(paramType.equals(AIRLINE_NAME_PARAM)) type = "Airline name ";
        else if(paramType.equals(FLIGHT_NUMBER_PARAM)) type = "Flight number ";
        else if(paramType.equals(DEPARTURE_DATETIME)) type = "Departure datetime ";
        else if(paramType.equals(ARRIVAL_DATETIME)) type = "Arrival datetime ";

        if(param == null || param.length()==0){
            responseSetStatusAndAddErrorMsg(response,HttpServletResponse.SC_PRECONDITION_FAILED,type +
                    "was not specified in query string: " + queryString);
            return false;
        }
        if(paramType.equals(DEPARTURE_DATETIME) || paramType.equals(ARRIVAL_DATETIME)){
            try{
                formatter.parse(param);
            }catch(ParseException e){
                responseSetStatusAndAddErrorMsg(response,HttpServletResponse.SC_BAD_REQUEST,"\""+ param +"\""+
                        " is not a correctly formatted datetime: MM/DD/YYYY HH:MM AM|PM \n" +
                        "Please note only a single space is needed to separate MM/DD/YYYY, HH:MM, and AM|PM !");
                return false;
            }
        }
        if(paramType.equals(FLIGHT_NUMBER_PARAM)){
            try{
                Integer.parseInt(param);
            }catch(NumberFormatException e){
                responseSetStatusAndAddErrorMsg(response,HttpServletResponse.SC_BAD_REQUEST,"\""+param +"\""+
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
        pw.println("All dictionary entries have been deleted");
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);

    }

    @VisibleForTesting
    Airline getAirline(String airlineName) {
        return this.airlines.get(airlineName);
    }
}

package edu.pdx.cs410J.leolu;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.util.Map;

import static edu.pdx.cs410J.web.HttpRequestHelper.Response;
import static edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send dictionary entries.
 */
public class AirlineRestClient
{
    private static final String WEB_APP = "airline";
    private static final String SERVLET = "flights";

    private final HttpRequestHelper http;


    /**
     * Creates a client to the airline REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AirlineRestClient( String hostName, int port )
    {
        //request url is : http://localhost:8080/airline/flights
        this(new HttpRequestHelper(String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET)));
    }

    @VisibleForTesting
    AirlineRestClient(HttpRequestHelper http) {
        this.http = http;
    }

    /**
     * getAirline() method
     * @return Airline object retrieved from Http Response using XmlParser to parse the XML content
     * */
    public Airline getAirline(String airlineName, String src, String dest) {
        Airline found = null;
        Response response = null;
        try{

            if(src==null && dest ==null)
                response = http.get(Map.of(AirlineServlet.AIRLINE_NAME_PARAM,airlineName));
            else
                response = http.get(Map.of(AirlineServlet.AIRLINE_NAME_PARAM,airlineName,
                        AirlineServlet.SOURCE_PARAM,src,
                        AirlineServlet.DESTINATION_PARAM,dest));

            String xmlInStringFormat = response.getContent();
            XmlParser parser = new XmlParser(xmlInStringFormat, true);
            found = parser.parse();

        }catch(IOException e){
            System.err.println("IOException thrown when getting data from response output: "
                    + e.getMessage());
        }catch(ParserException e){
            System.out.println(response.getContent());
        }
        return found;
    }

    /**
     * Invoked for POST requests
     * Maps flight information into HTTP POST Request
     * Then the request is sent to servlet
     * */
    public void addFlightToAirline(String[] flightInfo) {
        Response response;
        try{
            response = http.post(
                    Map.of(AirlineServlet.AIRLINE_NAME_PARAM, flightInfo[0],
                            AirlineServlet.FLIGHT_NUMBER_PARAM, flightInfo[1],
                            AirlineServlet.SOURCE_PARAM, flightInfo[2],
                            AirlineServlet.DEPARTURE_DATETIME, flightInfo[3],
                            AirlineServlet.DESTINATION_PARAM, flightInfo[4],
                            AirlineServlet.ARRIVAL_DATETIME, flightInfo[5]));
            throwExceptionIfNotOkayHttpStatus(response);
        }catch(IOException e){
            System.err.println("IOException thrown when writing data to response output stream: "
                    + e.getMessage());
        }
        //System.out.println(response.getContent());
    }

    public void removeAllAirlines() throws IOException {
        Response response = http.delete(Map.of());
        throwExceptionIfNotOkayHttpStatus(response);
    }

    private void throwExceptionIfNotOkayHttpStatus(Response response) {
        int code = response.getHttpStatusCode();
        if (code != HTTP_OK) {
            String message = response.getContent();
            throw new RestException(code, message);
        }
    }

}

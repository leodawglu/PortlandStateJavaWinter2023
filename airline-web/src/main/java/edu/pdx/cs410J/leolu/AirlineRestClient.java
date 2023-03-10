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
     * @return Airline object parsed from
     * */
    public Airline getAirline(String airlineName) throws IOException, ParserException {
        Response response = http.get(Map.of("airline",airlineName));
        String xmlInStringFormat = response.getContent();

        XmlParser parser = new XmlParser(xmlInStringFormat, true);

        return parser.parse();
    }


    public void addFlightToAirline(String word, String definition) throws IOException {
        Response response = http.post(Map.of(AirlineServlet.AIRLINE_NAME_PARAM, word,
                AirlineServlet.FLIGHT_NUMBER_PARAM, definition));
        throwExceptionIfNotOkayHttpStatus(response);
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

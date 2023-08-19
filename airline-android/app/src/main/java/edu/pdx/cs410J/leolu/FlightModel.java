package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.AirportNames;

public class FlightModel {
    String flightNumber;
    String source;
    String sourceString;
    String destination;

    public String getSourceString() {
        return sourceString;
    }

    public String getDestinationString() {
        return destinationString;
    }

    String destinationString;
    String departureDate;
    String departureTime;
    String arrivalDate;
    String arrivalTime;
    String duration;

    /**
     * Constructs a FlightModel instance with provided flight information.
     *
     * @param flightNumber The flight number.
     * @param source The source airport code.
     * @param destination The destination airport code.
     * @param departureDate The departure date.
     * @param departureTime The departure time.
     * @param arrivalDate The arrival date.
     * @param arrivalTime The arrival time.
     * @param duration The flight duration.
     */
    public FlightModel(String flightNumber, String source, String destination, String departureDate,
                       String departureTime, String arrivalDate, String arrivalTime, String duration) {
        this.flightNumber = flightNumber;
        this.source = source;
        this.sourceString = getFormattedAirportString(source);
        this.destination = destination;
        this.destinationString = getFormattedAirportString(destination);
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
    }

    /**
     * Formats an airport code into a display string with line breaks.
     *
     * @param airportCode The airport code to format.
     * @return The formatted airport string.
     */
    private String getFormattedAirportString(String airportCode) {
        if (airportCode != null && !airportCode.isEmpty()) {
            return AirportNames.getNamesMap().get(airportCode.toUpperCase()).replace(", ", "\n");
        } else {
            return "";
        }
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDuration() {
        return duration;
    }
}

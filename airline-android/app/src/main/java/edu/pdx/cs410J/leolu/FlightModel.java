package edu.pdx.cs410J.leolu;

public class FlightModel {
    String flightNumber;
    String source;
    String destination;
    String departureDate;
    String departureTime;
    String arrivalDate;
    String arrivalTime;
    String duration;

    public FlightModel(String flightNumber, String source, String destination, String departureDate,
                       String departureTime, String arrivalDate, String arrivalTime, String duration) {
        this.flightNumber = flightNumber;
        this.source = source;
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
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

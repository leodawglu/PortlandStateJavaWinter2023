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

    public FlightModel(String flightNumber, String source, String destination, String departureDate,
                       String departureTime, String arrivalDate, String arrivalTime, String duration) {
        this.flightNumber = flightNumber;
        this.source = source;
        if(source!=null && source.length()!=0){
            this.sourceString = AirportNames.getNamesMap().get(source.toUpperCase()).replace(", ", "\n");
        }else{
            this.sourceString="";
        }
        this.destination = destination;
        if(destination!=null && destination.length()!=0){
            this.destinationString = AirportNames.getNamesMap().get(destination.toUpperCase()).replace(", ", "\n");
        }else{
            this.destinationString="";
        }
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

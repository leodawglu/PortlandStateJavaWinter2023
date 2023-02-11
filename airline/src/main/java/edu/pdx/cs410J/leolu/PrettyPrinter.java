/**
 * The {code PrettyPrinter} class
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.AirlineDumper;

import java.io.Writer;
import java.io.PrintWriter;

public class PrettyPrinter implements AirlineDumper<Airline> {
    private final Writer writer;

    public PrettyPrinter(Writer writer) {
        this.writer = writer;
    }

    /**
     * @param airline Accepts an airline object and pretty prints the airline and its flights into file
     * */
    @Override
    public void dump(Airline airline) {
        try (PrintWriter pw = new PrintWriter(this.writer))
        {
            pw.println("*---------------------------------------------------------*");
            pw.println("Airline       : "+airline.getName());
            for(Flight fl: airline.getFlights()){
                pw.println("*---------------------------------------------------------*");
                pw.println("Flight Number : " +fl.getNumber());
                pw.println("Departing From: " + fl.getSource());
                pw.println("Departure Date: " + fl.getDepDate());
                pw.println("Departure Time: " + fl.getDepTime());
                pw.println("Bound For     : " + fl.getDestination());
                pw.println("Arrival Date  : " + fl.getArrDate());
                pw.println("Arrival Time  : " + fl.getArrTime());
                pw.println("Duration      : " + fl.getFlightDuration()/60 + "hrs " + fl.getFlightDuration()%60 + "mins");
            }
            pw.println("*---------------------------END---------------------------*");
            pw.flush();
        }
    }
}

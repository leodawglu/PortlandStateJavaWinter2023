/**
 * The {code TextDumper} class
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.AirlineDumper;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * A skeletal implementation of the <code>TextDumper</code> class for Project 2.
 */
public class TextDumper implements AirlineDumper<Airline> {
  private final Writer writer;
  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  /**
   * @param airline Accepts an airline object and dumps the airline and its flight information into file
   * */
  @Override
  public void dump(Airline airline) {
    try (
      PrintWriter pw = new PrintWriter(this.writer)
      ) {
      pw.println(airline.getName());
      /*each flight is added to file with the following format:
      * FlightNumber DepartureAirportCode DepartureDate DepartureTime ArrivalAirportCode ArrivalDate Arrival Time
      * */
      for(Flight f: airline.getFlights()){
        pw.println(f.getNumber() + " "+f.getSource()+" " +f.getDepartureString() +
                " " + f.getDestination() + " " + f.getArrivalString());
      }
      pw.flush();
    }
  }
}

package edu.pdx.cs410J.leolu;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

public class TextDumper {
  private final Writer writer;

  public TextDumper(Writer writer) {
    this.writer = writer;
  }


  public void dump(Map<String, String> dictionary) {
    try (
      PrintWriter pw = new PrintWriter(this.writer)
    ){
      for (Map.Entry<String, String> entry : dictionary.entrySet()) {
        pw.println(entry.getKey() + " : " + entry.getValue());
      }

      pw.flush();
    }
  }
  /**
   * @param airline Accepts an airline object and dumps the airline and its flight information into file
   * */
  /*
  @Override
  public void dump(Airline airline) {
    try (
            PrintWriter pw = new PrintWriter(this.writer)
    ) {
      pw.println(airline.getName());
      //each flight is added to file with the following format:
      //FlightNumber DepartureAirportCode DepartureDate DepartureTime ArrivalAirportCode ArrivalDate Arrival Time

      for(Flight f: airline.getFlights()){
        pw.println(f.getNumber() + " "+f.getSource()+" " +f.getDepDate() +
                " " +f.getDepTime24()+" "+ f.getDestination() + " " +f.getArrDate() +
                " " +f.getArrTime24());
      }
      pw.flush();
    }
  }*/
}

/**
 * The {code TextParser} class
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import java.io.*;

/**
 *  <code>TextParser</code> class
 *
 * Read flights line by line
 * flight number, departure, departure date, departure time, arrival, arrival date, arrival time
 * should any details be incorrect, print errors, then exit according to specification
 * perhaps print the entire line that's wrong, and use a pointer to locate what's wrong
 */
public class TextParser implements AirlineParser<Airline> {
  private Reader reader;
  private Airline airline;
  private String path;
  private StringBuilder err= new StringBuilder();

  /**
   * Constructor
   * @param reader An initialized Reader with a resource for parsing
   * */
  public TextParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * Uses Try With Resource and Catch Block
   * BufferedReader is the resource for reading the file line by line
   * An airline is initialized with a String from file
   * Should the file contain more lines of possible valid flights,
   * each candidate flight information is validated before adding to airline
   * Error messages are printed should any information be missing from flight
   * @throws ParserException should any errors occur during parsing
   * */
  public Airline parse() throws ParserException{
    try (//Try with resource: BufferedReader br
         BufferedReader br = new BufferedReader(this.reader)
    ) {

      String airlineName = br.readLine();
      if (airlineName == null) {
        throw new ParserException("Missing airline name");
      }
      this.airline = new Airline(airlineName);
      int lineNumber=1;
      String line;
      while((line = br.readLine()) != null){

        String[] s = line.split("\\s+");
        if(s.length==0) continue; //ignore empty lines;
        if(s.length>7){
          err.append("Extraneous inputs found in line " + lineNumber + ": \n");
          for(int i=7; i<s.length; i++){
            err.append(s[i]+" ");
          }
          throw new IllegalArgumentException(err.toString());
        }else if(s.length<7){
          err.append("The following arguments are missing: \n");
          if(s.length<1) err.append("Flight Number\n");
          if(s.length<2) err.append("Departure Airport Code\n");
          if(s.length<3) err.append("Departure Date\n");
          if(s.length<4) err.append("Departure Time\n");
          if(s.length<5) err.append("Arrival Airport Code\n");
          if(s.length<6) err.append("Arrival Date\n");
          if(s.length<7) err.append("Arrival Time\n");
          throw new IllegalArgumentException(err.toString());
        }
        Flight fl = new Flight(s[0],s[1],s[2],s[3],s[4],s[5],s[6],true);
        this.airline.addFlight(fl);
        if(fl.getError().length()!=0){
          System.out.println("Flight information in line " + lineNumber +
                  " of txt file is not formatted correctly, please review information above.");
          System.err.println("Provided txt file is malformed.");
          return null;
        }
        ++lineNumber;
      }

      return this.airline;

    }catch (ParserException e) {
      throw new ParserException("Malformed airline txt file", e);
    }catch(IOException e){
      System.err.println("An error occurred: " + e.getMessage());
    }catch(IllegalArgumentException e){
      System.out.println(e.getMessage());
      System.out.println("Please revise the flight formats in the airline file accordingly.");
    }
    return null;
  }


  public String getError(){
    return err.toString();
  }
}

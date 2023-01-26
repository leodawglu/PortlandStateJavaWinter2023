/**
 * The main {@code Project1} class for the CS410J Airline Project
 * @author edited by Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import com.google.common.annotations.VisibleForTesting;
import java.io.InputStream;
import java.util.Scanner;

public class Project1 {

  boolean printFlight = false, readMe = false;
  Airline anAirline;
  int argCount=0,idx=0;


  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    return true;
  }

  /**
   * This main method creates a new Project1 object
   * which is used for reading the user argument.
   * It checks if -README or -print are invoked
   * Ensures that airline and flight arguments are valid
   * before creating the airline and flight objects
   * @param args Command line argument
   * -README Overrides all inputs and prints README.txt on console
   * -input prints airline and flight information on console if valid
   * Input order:
   * airline -> Create Airline(name)
   * flightNumber (int)
   * src (3-letter alphabetical code of departure airport)
   * depart datetime
   * dest (3-letter alphabetical code of arrival airport)
   * arrive (3-letter alphabetical code of departure airport)
   * */
  public static void main(String[] args) {
    if(args.length==0) System.err.println("Missing command line arguments. Try -README for more details");
    Project1 ex = new Project1();

    argumentReader(args,ex);
    //If "-README" is called, print README.txt and exit
    if(ex.readMe){
      printReadMeFile();
      return;
    }
    if(ex.argCount<6){
      System.err.println("Not enough arguments. Try -README for more details");
      return;
    }
    //creates airline
    ex.anAirline = new Airline(args[ex.idx]);
    //creates flight
    ex.anAirline.addFlight(new Flight(args[ex.idx+1],args[ex.idx+2],args[ex.idx+3],args[ex.idx+4],args[ex.idx+5]));
    //If "-print" is called, prints Airline and Flight to console
    if(ex.printFlight){
      for(int i=ex.idx; i<args.length; i++){
        System.out.print(args[i]+ " ");
      }
    }
  }

  /**
   * Reads command line String[] args
   * Sets readMe to true if "-README" is present, then returns
   * Sets printFlight to true if "-print" is present
   * Increments argCount to be used for checking if there's enough
   * arguments for airline and flight creation
   * */
  private static void argumentReader(String[] args, Project1 curr){
    for(String arg: args){
      if(arg.contains("-README")) {
        curr.readMe = true;
        return;//ends method
      }
      else if(arg.contains("-print")){
        curr.printFlight = true;
        curr.idx+=1;
      }
      else curr.argCount++;
    }
  }

  /**
  * Prints README.txt file to console when -README is present in command-line arguments
  * */
  private static void printReadMeFile(){
    InputStream readme = Project1.class.getResourceAsStream("README.txt");
    Scanner scanner = new Scanner(readme);
    String line ="";
    while(scanner.hasNextLine()){
      line = scanner.nextLine();
      System.out.println(line);
    }
    scanner.close();
  }
}
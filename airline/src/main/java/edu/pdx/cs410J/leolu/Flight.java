/**
 * The {code Flight} class
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */

package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.AbstractFlight;


public class Flight extends AbstractFlight {
  private int flightNumber;
  private String dep; //Departure Airport 3-letter Code
  private String arr; //Arrival Airport 3-letter Code
  private String depDate;
  private String depTime;
  private String arrDate;
  private String arrTime;
  private String error = "";


  /**
   * Default Constructor
   * Creates a new <code>Flight</code>
   *
   * */
  public Flight(){}

  /**
   * Constructs a new instance of <code>Flight</code>
   * with a given flight number, departure airport code, arrival airport code,
   * departure datetime, and arrival datetime
   * Flight Constructor that accepts flight details as arguments
   * Creates a new <code>Flight</code>
   * @param fN Flight Number
   * @param dep Departure Airport 3-letter code
   * @param dDate Flight departure Date
   * @param dTime Flight departure Time
   * @param arr Arrival Airport 3-letter code
   * @param aDate Flight arrival Date
   * @param aTime Flight arrival Time
   *
   *
   * */
  public Flight(String fN, String dep, String dDate, String dTime,  String arr, String aDate, String aTime) {
    setDate(dDate,"Departure");
    setTime(dTime,"Departure");
    setDate(aDate, "Arrival");
    setTime(aTime, "Arrival");
    setAirportCode(dep,"Departure");
    setAirportCode(arr, "Arrival");
    setFlightNumber(fN);

  }

  /**
   * @return integer Flight Number
   */
  @Override
  public int getNumber() {
    return flightNumber;
  }

  /**
   * Ensures String can be parsed into an integer
   * Ensures that the integer is greater than zero
   * @param input - input flight number String from Constructor
   * @throws NullPointerException if input String is null
   * @throws NumberFormatException if String cannot be parsed to int
   * @throws IllegalArgumentException if int is less than or equal to 0
   * */
  public void setFlightNumber(String input){
    try{
      if(input==null||input.length()==0)
        throw new NullPointerException("Flight Number cannot be null.");

      int temp = Integer.parseInt(input);//Throws NumberFormatException if not digits

      if(temp<=0)
        throw new IllegalArgumentException("Flight Number must be greater than zero: ");
      this.flightNumber = temp;
    }catch(NullPointerException e){
      error = e.getMessage();
      System.out.println(error);
    }catch(NumberFormatException e){
      error = "Flight Number must be a positive integer: " +input;
      System.out.println(error);
    }catch(IllegalArgumentException e){
      error = e.getMessage() + input;
      System.out.println(error);
    }
  }

  /**
   * Ensures input is only of length 3
   * Ensures input is alphabetical
   * @param input - airport code String from Constructor
   * @param type - Departure or Arrival
   * @throws  NullPointerException if input is null or of length 0
   * @throws IllegalArgumentException if input is not a 3-letter alphabetical code
   * */
  public void setAirportCode(String input, String type){
    try{
      if(input==null||input.length()==0)
        throw new NullPointerException(" airport code cannot be null.");

      if(input.length()!=3||!isAirportCodeAlphabetic(input))
        throw new IllegalArgumentException(" airport code must be a 3-letter alphabetical code: ");

      if(type.equals("Departure")) this.dep=input.toUpperCase();
      if(type.equals("Arrival")) this.arr=input.toUpperCase();
    }catch(NullPointerException e){
      error = type + e.getMessage();
      System.out.println(error);
    }catch(IllegalArgumentException e){
      error = type + e.getMessage() + input;
      System.out.println(error);
    }
  }
  /**
   * @param input - airport code String from Constructor
   * @return boolean true when airport code is alphabetic
   * */
  private boolean isAirportCodeAlphabetic(String input)
  {
    return input.matches("^[a-zA-Z]*$");
  }

  /**
   * Ensures date is not null and is of correct format
   * mm/dd/yyyy - leading zeros are ignored
   * */
  private boolean isValidDate(String input){
    return input.matches("^(?:(1[0-2]|0?[1-9])/(3[01]|[12][0-9]|0?[1-9])|↵\n" +
            "(3[01]|[12][0-9]|0?[1-9])/(1[0-2]|0?[1-9]))/(?:[0-9]{2})?[0-9]{2}$");
  }

  /**
   * Set Date only if valid
   * @param input - date String from Constructor
   * @param type - Departure or Arrival
   * @throws NullPointerException if input String is null or of length 0
   * @throws IllegalArgumentException if provided date format is not mm/dd/yyyy
   * */

  public void setDate(String input, String type){
    try{
      if(input == null || input.length()==0)
        throw new NullPointerException(" date cannot be null.");
      if(!isValidDate(input))
        throw new IllegalArgumentException(" date format incorrect (mm/dd/yyyy): ");
      if(type.equals("Departure")) this.depDate=input;
      if(type.equals("Arrival")) this.arrDate=input;
    }catch(NullPointerException e){
      error = type + e.getMessage();
      System.out.println(error);
    }catch(IllegalArgumentException e){
      error = type + e.getMessage() +input;
      System.out.println(error);
    }
  }

  /**
   * Ensures time is correct format
   * hh:mm - leading zeros can be ignored
   * */
  private boolean isValidTime(String input){
    return input.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
  }

  /**
   * Set Time only if valid
   * @param input - Time String from Constructor
   * @param type - Departure or Arrival
   * @throws NullPointerException if input String is null or of length 0
   * @throws IllegalArgumentException if provided time format is not hh:mm
   * */

  public void setTime(String input, String type){
    try{
      if(input == null || input.length()==0)
        throw new NullPointerException(" time cannot be null.");
      if(!isValidTime(input))
        throw new IllegalArgumentException(" time format incorrect (hh:mm): ");
      if(type.equals("Departure")) this.depTime=input;
      if(type.equals("Arrival")) this.arrTime=input;
    }catch(NullPointerException e){
      error = type + e.getMessage();
      System.out.println(error);
    }catch(IllegalArgumentException e){
      error = type + e.getMessage() +input;
      System.out.println(error);
    }
  }
  /**
   * @return String Departure Airport 3-letter code
   **/
  @Override
  public String getSource() {
    return dep;
    //
  }
  @Override
  public String getDepartureString() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  /**
   * @return String Arrival Airport 3-letter code
   **/
  @Override
  public String getDestination() {
    return arr;
  }

  @Override
  public String getArrivalString() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  public String getError(){
    return error;
  }
}

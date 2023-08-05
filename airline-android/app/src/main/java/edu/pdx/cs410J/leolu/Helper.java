package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.AirportNames;

public class Helper {
  /**
   * @return  boolean True if departure time comes before arrival time
   * */
  public static boolean departureBeforeArrival(long depTime, long arrTime){
    return depTime < arrTime;
  }

  /**
   * @param input - airport code String to be checked if real
   * @return boolean true when airport code is from a real airport
   * Utilizes {@code AirportNames.getNamesMap()}
   * */
  public static boolean isRealAirportCode(String input){
    return AirportNames.getNamesMap().containsKey(input);
  }
}

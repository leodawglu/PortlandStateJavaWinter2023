package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.lang.Human;

import java.util.ArrayList;
import java.util.List;

/**                                                                                 
 * This class is represents a <code>Student</code>.                                 
 */                                                                                 
public class Student extends Human {
  private ArrayList<String> classes;
  private double gpa;
  private String gender;
                                                                                    
  /**                                                                               
   * Creates a new <code>Student</code>                                             
   *                                                                                
   * @param name                                                                    
   *        The student's name                                                      
   * @param classes                                                                 
   *        The names of the classes the student is taking.  A student              
   *        may take zero or more classes.                                          
   * @param gpa                                                                     
   *        The student's grade point average                                       
   * @param gender                                                                  
   *        The student's gender ("male", "female", or "other", case insensitive)
   */                                                                               
  public Student(String name, ArrayList<String> classes, double gpa, String gender) {
    super(name);
    this.classes = classes;
    this.gpa = gpa;
    this.gender = gender;
  }

  /**                                                                               
   * All students say "This class is too much work"
   */
  @Override
  public String says() {                                                            
    return "This class is too much work";
    //throw new UnsupportedOperationException("Not implemented yet");
  }
                                                                                    
  /**                                                                               
   * Returns a <code>String</code> that describes this                              
   * <code>Student</code>.                                                          
   */                                                                               
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Student Name: " + name +"\n" + "Gender: " + gender + "\n" + "GPA: "+gpa+"\n");
    sb.append("Current classes:" +"\n");
    for(String cls: classes){
      sb.append("                "+cls+"\n");
    }
    return sb.toString();
    //throw new UnsupportedOperationException("Not implemented yet");
  }

  public double getGPA(){return gpa;}

  public String getGender(){return gender;}

  /**
   * Main program that parses the command line, creates a
   * <code>Student</code>, and prints a description of the student to
   * standard out by invoking its <code>toString</code> method.
   */
  public static void main(String[] args) {
    if(args.length==0)System.err.println("Missing command line arguments");
    else{
      ArrayList<String> cls = new ArrayList<>();
      cls.add("CS563");
      cls.add("CS510P");
      cls.add("CS554");
      Student chad = new Student("Chad",cls,3.5,"other");
      System.out.println(chad.toString());
      System.out.println(chad.says());
    }
  }
}